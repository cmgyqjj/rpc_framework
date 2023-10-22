package com.qjj.server;

import com.qjj.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author:qjj
 * @create: 2023-10-22 15:00
 * @Description: Netty服务端
 */

public class NettyServer {
    public static void main(String[] args) {
        /*
        新建两个线程组，bossGroup和workerGroup启动一条线程，监听OP_ACCEPT事件
        worker线程组默认启动cpu核心数*2的线程数
         */
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try{
//            serverBootstrap是Netty的服务启动辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup,workGroup)
//                    这里是因为用TCP协议，所以用NioServerSocketChannel
//                    如果是UDP，用DatagramChannel.class
                    .channel(NioServerSocketChannel.class)
//                    这里是设置TCP连接的缓冲区参数
                    .option(ChannelOption.SO_BACKLOG,128)
//                    当有客户端注册读写事件时，初始化handler
//                    并将Handler加入管道中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            /*
                            向Worker管道的双向链表中插入一个Handler
                             */
                            channel.pipeline().addLast(new ServerHandler());
                        }
                    });
//            同步绑定端口
            ChannelFuture future = serverBootstrap.bind(8081).sync();
//            阻塞主线程，直到Socket通道被关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
//            优雅关闭
            boosGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
