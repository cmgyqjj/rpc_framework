package com.qjj.hander;

import com.qjj.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:qjj
 * @create: 2023-07-17 23:42
 * @Description: Rpc客户端处理器
 */
@Slf4j
public class RpcClientHander extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse resp) {
        // 接收到response, 给channel设计别名，让sendRequest里读取response
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(resp);
        log.info("打印RPC远程调用结果:"+resp.getCode());
        if(resp.getCode() == 200) {
            log.info("打印RPC远程调用结果:" + resp.getResult());
        }else{
            System.out.println("打印RPC远程调用结果:" + resp.getError());
        }
    }
}
