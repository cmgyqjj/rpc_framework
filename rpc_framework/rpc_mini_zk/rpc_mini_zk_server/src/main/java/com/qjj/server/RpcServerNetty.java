package com.qjj.server;

import com.qjj.common.RpcDecoder;
import com.qjj.common.RpcEncoder;
import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;

import com.qjj.hander.RpcServerHander;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author:qjj
 * @create: 2023-07-15 10:13
 * @Description: rpc服务器端的启动器，这里使用Netty实现
 */


public class RpcServerNetty {

//    启动的默认端口
    private static Integer port=8898;

    public static void main(String[] args) {
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
                            ch.pipeline().addLast(new RpcServerHander());
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
}
