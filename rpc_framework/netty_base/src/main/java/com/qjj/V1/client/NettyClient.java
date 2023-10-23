package com.qjj.V1.client;

import com.alibaba.fastjson.JSONObject;
import com.qjj.V1.client.handler.ClientHandler;
import com.qjj.V1.client.req.RequestFuture;
import com.qjj.V1.client.resp.Response;
import com.qjj.V1.server.handler.ServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author:qjj
 * @create: 2023-10-23 09:58
 * @Description: Netty客户端
 */

public class NettyClient {
    public static EventLoopGroup group=null;
    public static Bootstrap bootstrap=null;


//    静态代码块实现单例模式
    static{
        bootstrap=new Bootstrap();
        group=new NioEventLoopGroup();
//        设置socket通道
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
//        设置内存分配器
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        try{
//            新建一个Promise对象
            Promise<Response> promise = new DefaultPromise<>(group.next());
//            业务Handler
            final ClientHandler handler = new ClientHandler();
//            把promise对象赋给handler，用于获取返回服务端的响应结果
            handler.setPromise(promise);
//            把Handler对象放入管道中
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                            0,4,0,4));
//                            把接受到的ByteBuf数据包转换成String
                    channel.pipeline().addLast(new StringDecoder());
//                            数据包处理器
                    channel.pipeline().addLast(handler);
//                            在消息体前面增加4个字节的长度值，的哥参数是长度值占用的字节数
//                            第二个参数是长度值的调节，表明是否包含长度值本身
                    channel.pipeline().addLast(new LengthFieldPrepender(4,false));
//                            把字符消息转换成ByteBuf
                    channel.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
                }
            });
//            连接服务端
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8081).sync();
//            构建request请求
            RequestFuture request = new RequestFuture();
//            可以采用AtomicLong类的incrementAndGet()方法来实现
            request.setId(1);
            request.setRequest("请求参数1");
//            转成JSON格式发送给编码器StringEncoder
//            StringEncode编码器再发送给LengthFieldPrepender长度编码器，最终写到TCP缓存中，并传送给客户端
            String requestStr = JSONObject.toJSONString(request);
            future.channel().writeAndFlush(requestStr);
//            阻塞主线程，直到Channel关闭
            Response response=promise.get();
            System.out.println(JSONObject.toJSONString(response));
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
