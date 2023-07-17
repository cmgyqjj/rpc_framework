package com.qjj.client;

import com.qjj.common.RpcDecoder;
import com.qjj.common.RpcEncoder;
import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import com.qjj.hander.RpcClientHander;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.UUID;

/**
 * @author:qjj
 * @create: 2023-07-15 10:14
 * @Description: rpc客户端的启动器,使用Netty实现数据的传输
 */

public class RpcClientNetty {

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
        Integer id = new Random().nextInt(10);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setClassName("com.qjj.service.impl.UserServiceImpl");
        rpcRequest.setMethodName("getUserByUserId");
        rpcRequest.setParameters(new Object[]{id});
        rpcRequest.setParameterTypes(new Class[]{Integer.class});
        future.sync().channel().writeAndFlush(rpcRequest);
        System.out.println("客户端发送数据:"+rpcRequest.getRequestId());
    }
}
