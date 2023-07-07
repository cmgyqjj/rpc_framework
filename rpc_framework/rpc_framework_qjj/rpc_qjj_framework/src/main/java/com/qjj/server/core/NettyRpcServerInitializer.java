package com.qjj.server.core;

import com.qjj.codec.RpcDecoder;
import com.qjj.codec.RpcEncoder;
import com.qjj.codec.RpcRequest;
import com.qjj.codec.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author:qjj
 * @create: 2023-07-07 15:00
 * @Description: Rpc服务端的netty启动器
 */

public class NettyRpcServerInitializer extends ChannelInitializer<SocketChannel> {


    private Map<String, Object> handlerMap;

    public NettyRpcServerInitializer(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
        p.addLast(new RpcEncoder(RpcRequest.class));
        p.addLast(new RpcDecoder(RpcResponse.class));
        p.addLast(new NettyRpcServerHandler(handlerMap));
    }
}
