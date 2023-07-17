package com.qjj.hander;

import com.qjj.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author:qjj
 * @create: 2023-07-17 23:42
 * @Description: Rpc客户端处理器
 */

public class RpcClientHander extends SimpleChannelInboundHandler<RpcResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse resp) throws Exception {
        System.out.println("打印RPC远程调用结果:"+resp.getCode());
        if(resp.getCode() == 200) {
            System.out.println("打印RPC远程调用结果:" + resp.getResult());
        }else{
            System.out.println("打印RPC远程调用结果:" + resp.getError());
        }
    }
}
