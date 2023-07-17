package com.qjj.server;

import com.qjj.common.RpcDecoder;
import com.qjj.common.RpcEncoder;
import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import com.qjj.hander.RpcServerHander;
import com.qjj.service.impl.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author:qjj
 * @create: 2023-07-15 10:13
 * @Description: rpc服务器端的启动器，这里使用Netty实现
 */


public class RpcServerNetty {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try{
            new ServerBootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcDecoder(RpcRequest.class));
                            ch.pipeline().addLast(new RpcEncoder(RpcResponse.class));
                            ch.pipeline().addLast(new RpcServerHander());
                        }
                    }).bind(8899);
        }catch (Exception e) {
            if (e instanceof InterruptedException) {
                System.out.println("Rpc server remoting server stop");
            } else {
                System.out.println("Rpc server remoting server error");
            }
        }
    }
}
