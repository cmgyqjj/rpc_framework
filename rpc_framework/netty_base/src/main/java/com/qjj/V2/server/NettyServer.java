package com.qjj.V2.server;

import com.qjj.V2.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author:qjj
 * @create: 2023-10-22 15:00
 * @Description: Netty服务端
 */
//TODO 使用Apache Commons Pool重构代码，不知道要把什么东西放到Pool里面
public class NettyServer {
    public static void main(String[] args) {
        /*
        新建两个线程组，bossGroup和workerGroup启动一条线程，监听OP_ACCEPT事件
        worker线程组默认启动cpu核心数*2的线程数
         */
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
//            serverBootstrap是Netty的服务启动辅助类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boosGroup, workerGroup)
//                    这里是因为用TCP协议，所以用NioServerSocketChannel
//                    如果是UDP，用DatagramChannel.class
                    .channel(NioServerSocketChannel.class)
//                    这里是设置TCP连接的缓冲区参数
                    .option(ChannelOption.SO_BACKLOG, 128)
//                    当有客户端注册读写事件时，初始化handler
//                    并将Handler加入管道中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            /*
                            新增一个前缀为4B的int类型作为长度解码器
                            第一个参数是包的最大长度，第二个参数是长度偏移量，由于编码时长度值在最前面
                            无偏移，所以此处设置为0，第三个参数是长度值的字节数，此处设置为4
                            第四个参数是长度的调整值，假设请求包的大小是20B
                            若长度值不包含本身则是20B，若长度值包含本身则是24B，需要4个字节
                            第五个参数是在解析时需要跳过的字节数(此处为4)

                            这里做的事情相当于在封装请求包时，先把请求包的长度写入到请求包的最前面
                            这是为了解决TCP粘包问题，而且也能更好管理请求包的长度
                             */
                            channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                                    0, 4, 0, 4));
//                            把接受到的ByteBuf数据包转换成String
                            channel.pipeline().addLast(new StringDecoder());
//                            数据包处理器
                            channel.pipeline().addLast(new ServerHandler());
//                            在消息体前面增加4个字节的长度值，的哥参数是长度值占用的字节数
//                            第二个参数是长度值的调节，表明是否包含长度值本身
                            channel.pipeline().addLast(new LengthFieldPrepender(4, false));
//                            把字符消息转换成ByteBuf
                            channel.pipeline().addLast(new StringEncoder());
//                            注意编码器解码器顺序
                        }
                    });
//            同步绑定端口
//            TODO 是否这一步可以放到Pool里面？
            ChannelFuture future = serverBootstrap.bind(8081).sync();
//            阻塞主线程，直到Socket通道被关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            优雅关闭
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
