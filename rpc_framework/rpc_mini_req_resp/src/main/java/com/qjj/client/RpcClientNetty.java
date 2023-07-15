package com.qjj.client;

import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
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
//                            TODO 需要改造成通用的，而不是StringDecoder，StringEncoder
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                RpcResponse rpcResponse = (RpcResponse) msg;
                                System.out.println("打印RPC远程调用结果:"+rpcResponse.getCode());
                                if(rpcResponse.getCode() == 200) {
                                    System.out.println("打印RPC远程调用结果:" + rpcResponse.getResult());
                                }else{
                                    System.out.println("打印RPC远程调用结果:" + rpcResponse.getError());
                                }
                            }
                        });
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