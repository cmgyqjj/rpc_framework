package com.qjj.V2.server.handler;

import com.alibaba.fastjson.JSONObject;
import com.qjj.V0.client.req.RequestFuture;
import com.qjj.V2.client.resp.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author:qjj
 * @create: 2023-10-22 15:16
 * @Description: Netty的业务处理器，当NioEventLoop线程从Channel读取数据时，
 * 执行绑定在Channel的ChannelInboundHandler对象上，并执行其channelRead()方法
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        if(msg instanceof String){
////            把二进制数据转换成字符串，默认编码UTF-8
//            System.out.println(((ByteBuf)msg).toString(Charset.defaultCharset()));
//        }
//        ctx.channel().writeAndFlush("msg has recived!");
//        获取客户端发送的请求，将其转换成RequestFuture对象
//        由于已经使用了解码器，所以msg这里已经是String类型了
        RequestFuture request = JSONObject.parseObject(msg.toString(), RequestFuture.class);
        long id = request.getId();
        System.out.println("请求消息===" + msg.toString());
//        构建响应结果
        Response response = new Response();
        response.setId(id);
        response.setResult("服务器响应ok");
        ctx.channel().writeAndFlush(JSONObject.toJSONString(response));
    }
}
