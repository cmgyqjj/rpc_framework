package com.qjj.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.swagger.models.auth.In;

import java.net.InetSocketAddress;
import java.util.Random;

/**
 * @author:qjj
 * @create: 2023-07-15 10:14
 * @Description: rpc客户端的启动器,使用Netty实现数据的传输
 */

public class RPCClientNetty {

    public static void main(String[] args) throws InterruptedException {
        Integer id = new Random().nextInt(10);
        ChannelFuture future = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println("打印RPC远程调用结果:"+msg);
                            }
                        });
                    }
                }).connect(new InetSocketAddress("localhost", 8899));
        future.sync().channel().writeAndFlush(id.toString());
        System.out.println("客户端发送数据:"+id);
    }
}
