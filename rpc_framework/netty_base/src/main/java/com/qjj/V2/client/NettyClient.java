package com.qjj.V2.client;

import com.alibaba.fastjson.JSONObject;
import com.qjj.V2.client.handler.ClientHandler;
import com.qjj.V2.client.req.RequestFuture;
import com.qjj.V2.client.resp.Response;
import com.qjj.V2.server.handler.ServerHandler;
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

/**
 * @author:qjj
 * @create: 2023-10-23 09:58
 * @Description: Netty客户端
 */

public class NettyClient {
    public static EventLoopGroup group = null;
    public static Bootstrap bootstrap = null;

    public static ChannelFuture future = null;

    //    静态代码块实现单例模式
    static {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
//        设置socket通道
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
//        设置内存分配器
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        final ClientHandler handler = new ClientHandler();
        //            把Handler对象放入管道中
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                        0, 4, 0, 4));
//                            把接受到的ByteBuf数据包转换成String
                channel.pipeline().addLast(new StringDecoder());
//                            数据包处理器
                channel.pipeline().addLast(handler);
//                            在消息体前面增加4个字节的长度值，的哥参数是长度值占用的字节数
//                            第二个参数是长度值的调节，表明是否包含长度值本身
                channel.pipeline().addLast(new LengthFieldPrepender(4, false));
//                            把字符消息转换成ByteBuf
                channel.pipeline().addLast(new StringEncoder(Charset.forName("utf-8")));
            }
        });
        try{
            future = bootstrap.connect("127.0.0.1", 8081).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public Object sendRequest(Object msg){
        try{
            //            构建request请求
            RequestFuture request = new RequestFuture();
//            可以采用AtomicLong类的incrementAndGet()方法来实现
            request.setRequest(msg);
//            转成JSON格式发送给编码器StringEncoder
//            StringEncode编码器再发送给LengthFieldPrepender长度编码器，最终写到TCP缓存中，并传送给客户端
            String requestStr = JSONObject.toJSONString(request);
//        在进行单个连接时，future可以静态话，但是在多个连接的情况下，future不可以静态化
            future.channel().writeAndFlush(requestStr);
//            异步获取响应结果
            Object result = request.get();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        NettyClient client = new NettyClient();
        for(int i=0;i<100;i++){
            Object result = client.sendRequest("hello"+i);
            System.out.println("响应结果："+result);
        }
    }
}
