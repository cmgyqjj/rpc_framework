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

    /**
    *@Param: SocketChannel ch
    *@return: void
    *@Author: qjj
    *@date:
     * 初始化管道，添加编码器，解码器，处理器
    */
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
//        用于检测通道的空闲状态，如果连续 30 秒没有读写操作，就会触发一个 USER_EVENT_TRIGGERED 事件。
        p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
//        将实现了 RpcRequest 接口的 Java 对象编码成二进制数据，以便网络传输。
        p.addLast(new RpcEncoder(RpcRequest.class));
//        将从网络中接收到的二进制数据解码成实现了 RpcResponse 接口的 Java 对象。
        p.addLast(new RpcDecoder(RpcResponse.class));
//        自定义的处理器（handler），用于处理客户端发送的 RPC 请求。
//        该处理器需要一个参数 handlerMap，它是一个 Map 对象，用于将每个 RPC 请求的方法名与对应的处理器函数进行映射。
        p.addLast(new NettyRpcServerHandler(handlerMap));
    }
}
