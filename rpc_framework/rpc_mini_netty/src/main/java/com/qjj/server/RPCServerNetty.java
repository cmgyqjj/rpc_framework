package com.qjj.server;


import com.qjj.service.impl.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author:qjj
 * @create: 2023-07-15 10:13
 * @Description: rpc服务器端的启动器，这里使用Netty实现
 */

public class RPCServerNetty {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        try{
            new ServerBootstrap()
                    .group(new NioEventLoopGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    System.out.println("接受到客户端发送的数据："+msg);
                                    String userByUserId = userService.getUserByUserId(Integer.valueOf((String) msg));
                                    System.out.println("返回给客户端的数据："+userByUserId);
                                    ctx.writeAndFlush(userByUserId).addListener(ChannelFutureListener.CLOSE);
                                }
                            });
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
