package com.qjj.server.core;

import com.qjj.codec.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author:qjj
 * @create: 2023-07-07 15:50
 * @Description: rpc的Netty服务处理器
 */

public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map<String, Object> handlerMap;

    public NettyRpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

    }
}
