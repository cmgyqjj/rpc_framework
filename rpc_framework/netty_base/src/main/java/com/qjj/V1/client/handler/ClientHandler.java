package com.qjj.V1.client.handler;

import com.alibaba.fastjson.JSONObject;
import com.qjj.V1.client.resp.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Promise;
import lombok.Data;

/**
 * @author:qjj
 * @create: 2023-10-23 10:20
 * @Description: 客户端处理类
 */

@Data
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private Promise<Response> promise;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response = JSONObject.parseObject(msg.toString(), Response.class);
//        设置响应结果并唤醒主线程
        promise.setSuccess(response);
    }
}
