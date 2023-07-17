package com.qjj.hander;

import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author:qjj
 * @create: 2023-07-17 16:50
 * @Description: 服务端处理器
 */

public class RpcServerHander extends SimpleChannelInboundHandler<RpcRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest req) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setCode(200);
        try{
            System.out.println("接受到客户端发送的数据："+req.getRequestId());
            Class<?>[] parameterTypes = req.getParameterTypes();
            Object[] parameters = req.getParameters();
            String methodName = req.getMethodName();
            String className = req.getClassName();
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
}
