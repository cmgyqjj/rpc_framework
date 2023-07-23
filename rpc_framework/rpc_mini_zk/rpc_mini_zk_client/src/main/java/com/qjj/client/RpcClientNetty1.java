package com.qjj.client;

import com.qjj.client.hander.RpcClientHander;
import com.qjj.common.RpcDecoder;
import com.qjj.common.RpcEncoder;
import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @author:qjj
 * @create: 2023-07-15 10:14
 * @Description: rpc客户端的启动器,使用Netty实现数据的传输
 */

public class RpcClientNetty1 {

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcEncoder(RpcRequest.class));
                        ch.pipeline().addLast(new RpcDecoder(RpcResponse.class));
                        ch.pipeline().addLast(new RpcClientHander());
                    }
                }).connect(new InetSocketAddress("localhost", 8899));
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName("com.qjj.service.impl.HelloServiceImpl");
        rpcRequest.setMethodName("Hello");
        rpcRequest.setParameters(new Object[]{"qjj"});
        rpcRequest.setParameterTypes(new Class[]{String.class});
        future.sync().channel().writeAndFlush(rpcRequest);
        System.out.println("客户端发送数据:"+rpcRequest.getRequestId());
    }
}
