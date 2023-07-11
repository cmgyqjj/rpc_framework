package com.qjj.server.core;

import com.qjj.server.registry.ServiceRegistry;
import com.qjj.utils.ServiceUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;


import java.util.HashMap;
import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-07-07 14:47
 * @Description: Netty实现Rpc框架核心
 */
@Slf4j
public class NettyServer extends Server{

//    服务地址，格式为：ip:port
    private String serverAddress;

//    存放接口名与服务对象之间的映射关系
    private Map<String, Object> serviceMap = new HashMap<>();

//    服务注册类，用于将服务注册到注册中心
    private ServiceRegistry serviceRegistry;


    @Override
    public void start() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(workerGroup,bossGroup)
                    .channel(NioServerSocketChannel.class)
//                boss 负责处理连接，worker(child) 负责处理读写，这里主要决定了worker需要处理什么逻辑
                    .childHandler(new NettyRpcServerInitializer(serviceMap))
//                ChannelOption.SO_BACKLOG 配置就是控制TCP中未完成握手的线程队列+正在执行握手的队列总长度的和的参数
//                这里配置的128相当于是一共只能有128个线程可以参与握手和等待握手
                    .option(ChannelOption.SO_BACKLOG, 128)
//                ChannelOption.SO_KEEPALIVE 配置就是控制TCP连接中的心跳检测机制的参数
//                当设置为true的时候，TCP会实现监控连接是否有效，当连接处于空闲状态的时候，
//                超过了2个小时，本地的TCP实现会发送一个数据包给远程的 socket，如果远程没有发回响应，
//                TCP会持续尝试11分钟，直到响应为止，如果在12分钟的时候还没响应，TCP尝试关闭socket连接。
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
//            绑定端口，开始接收进来的连接
//            这里的sync()方法是等待绑定操作完成，sync()方法会阻塞到bind()操作完成为止
            ChannelFuture future = bootstrap.bind(host, port).sync();
            if (serviceRegistry != null) {
                serviceRegistry.registerService(host, port, serviceMap);
            }
            log.info("Server started on port {}", port);
            future.channel().closeFuture().sync();
        }catch (Exception e) {
            if (e instanceof InterruptedException) {
                log.info("Rpc server remoting server stop");
            } else {
                log.error("Rpc server remoting server error", e);
            }
        }finally {
            serviceRegistry.unregisterService();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }

    @Override
    public void stop() throws Exception {
//        TODO 先不写这个逻辑吧，后面再补
        System.out.println("RpcServer stop");
    }

//    本地注册服务
    protected void addService(String interfaceName, String version, Object serviceBean) {
        log.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
//        先直接简单的装进去
        serviceMap.put(interfaceName+version, serviceBean);
//       使用工具类加密之后再存进去
//        String serviceKey = ServiceUtil.makeServiceKey(interfaceName, version);
//        serviceMap.put(serviceKey, serviceBean);
    }
}
