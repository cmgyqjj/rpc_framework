package com.qjj.server;

import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import com.qjj.service.impl.UserServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
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
//                            TODO 需要改造成通用的，而不是StringDecoder，StringEncoder
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg){
                                    try{
                                        RpcRequest rpcRequest = (RpcRequest) msg;
                                        System.out.println("接受到客户端发送的数据："+rpcRequest.getRequestId());
                                        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
                                        Object[] parameters = rpcRequest.getParameters();
                                        String methodName = rpcRequest.getMethodName();
                                        String className = rpcRequest.getClassName();
                                        Class reqClass = Class.forName(className);
                                        Object invoke = reqClass.getMethod(methodName, parameterTypes).invoke(reqClass.newInstance(), parameters);
                                        String res = invoke.toString();
                                        System.out.println("返回给客户端的数据："+res);
                                        RpcResponse rpcResponse = new RpcResponse();
                                        rpcResponse.setCode(200);
                                        rpcResponse.setResult(res);
                                        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);
                                    }catch (Exception e) {
                                        RpcResponse rpcResponse = new RpcResponse();
                                        rpcResponse.setCode(500);
                                        rpcResponse.setError(e.toString());
                                        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);
                                        e.printStackTrace();
                                    }
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
