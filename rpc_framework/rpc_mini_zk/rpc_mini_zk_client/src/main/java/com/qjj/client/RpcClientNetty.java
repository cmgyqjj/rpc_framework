package com.qjj.client;

import com.qjj.factory.RpcFactory;
import com.qjj.hander.RpcClientHander;
import com.qjj.common.RpcDecoder;
import com.qjj.common.RpcEncoder;
import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import com.qjj.proxy.RPCClientProxy;
import com.qjj.registry.RpcRegistry;
import com.qjj.registry.RpcRegistryRequest;
import com.qjj.registry.ZkRpcRegistry;
import com.qjj.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author:qjj
 * @create: 2023-07-15 10:14
 * @Description: rpc客户端的启动器, 使用Netty实现数据的传输
 */
@Slf4j
public class RpcClientNetty {

    private RpcRegistry rpcRegistry;
    private static RpcFactory rpcFactory;

    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;


    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcEncoder(RpcRequest.class));
                        ch.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                        ch.pipeline().addLast(new RpcClientHander());
                    }
                });
        rpcFactory = new RpcFactory();
    }

    public RpcClientNetty() {
        try {
//            TODO 客户端会知道地址吗？感觉这里的逻辑是不是有问题啊，回头去再检查检查吧
            this.rpcRegistry = rpcFactory.getInstance("localhost:2181", "zookeeper");
        } catch (Exception e) {
            log.error("初始化Client端zk失败");
            throw new RuntimeException(e);
        }

    }

    public void start() {
        Integer id = new Random().nextInt(10);
        RPCClientProxy rpcClientProxy = new RPCClientProxy(this);
        UserService userService = rpcClientProxy.getProxy(UserService.class);
        userService.getUserByUserId(id);
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回一个值， 而不是想要的相应的response
     */
    public RpcResponse sendRequest(RpcRequest request) throws Exception {
        RpcRegistryRequest rpcRegistryRequest = rpcRegistry.discovery(request.getClassName());
        try {
//            TODO 因为前面没有规范的地址传输，所以这里需要手动做一个分割的操作，后续更改
            String[] parts = rpcRegistryRequest.getServiceAddr().split(":");
            String host= parts[0];
            Integer port= Integer.valueOf(parts[1]);
            ChannelFuture channelFuture = bootstrap.connect(host,port).sync();
            Channel channel = channelFuture.channel();
            // 发送数据
            request.setRequestId("test");
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在hanlder中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数，后面可以再进行优化
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
            RpcResponse response = channel.attr(key).get();
            log.info("接受到的消息-------");
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
