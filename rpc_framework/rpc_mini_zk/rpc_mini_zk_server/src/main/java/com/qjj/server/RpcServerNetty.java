package com.qjj.server;

import com.qjj.common.RpcDecoder;
import com.qjj.common.RpcEncoder;
import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;

import com.qjj.factory.RpcFactory;
import com.qjj.hander.RpcServerHander;
import com.qjj.registry.RpcRegistry;
import com.qjj.registry.RpcRegistryRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-07-15 10:13
 * @Description: rpc服务器端的启动器，这里使用Netty实现
 */

@Slf4j
public class RpcServerNetty {

    //    服务注册
    private RpcFactory rpcFactory;

//    存放接口名与服务对象之间的映射关系
    private Map<String, Object> interfaceMap = new HashMap<>();
//    启动的默认端口
    private static Integer port=8898;

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap()
                    .group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcDecoder(RpcRequest.class));
                            ch.pipeline().addLast(new RpcEncoder(RpcResponse.class));
                            ch.pipeline().addLast(new RpcServerHander(interfaceMap));
                        }
                    });
            // 同步阻塞
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            // 死循环监听
            channelFuture.channel().closeFuture().sync();
            System.out.println("Netty服务端启动了...");
        }catch (Exception e) {
            if (e instanceof InterruptedException) {
                System.out.println("Rpc server remoting server stop");
            } else {
                System.out.println("Rpc server remoting server error");
            }
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public void provideServiceInterface(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class clazz : interfaces) {
            // 本机的映射表
            interfaceMap.put(clazz.getName(), service);
            try {
//                使用注册中心工厂初始化对应的注册中心
//                TODO 这里应该从外部传入
                RpcRegistry zookeeper = rpcFactory.getInstance("localhost:2181", "zookeeper");
//                在注册中心注册服务
                RpcRegistryRequest rpcRegistryRequest = new RpcRegistryRequest();
                rpcRegistryRequest.setServiceName(clazz.getName());
//                rpcRegistryRequest.setServiceVersion("0");
                zookeeper.register(rpcRegistryRequest);
                log.info("注册成功----");
            } catch (Exception e) {
                log.error("注册失败----");
                throw new RuntimeException(e);
            }
        }
    }

}
