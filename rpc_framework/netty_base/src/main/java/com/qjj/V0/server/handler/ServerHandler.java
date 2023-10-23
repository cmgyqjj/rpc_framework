package com.qjj.V0.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.Charset;

/**
 * @author:qjj
 * @create: 2023-10-22 15:16
 * @Description: Netty的业务处理器，当NioEventLoop线程从Channel读取数据时，
 * 执行绑定在Channel的ChannelInboundHandler对象上，并执行其channelRead()方法
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        if(msg instanceof String){
//            把二进制数据转换成字符串，默认编码UTF-8
            System.out.println(((ByteBuf)msg).toString(Charset.defaultCharset()));
        }
        ctx.channel().writeAndFlush("msg has recived!");
        System.out.println(1);
    }
}
