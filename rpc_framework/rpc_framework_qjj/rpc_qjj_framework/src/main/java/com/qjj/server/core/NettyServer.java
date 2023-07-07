package com.qjj.server.core;

import io.netty.bootstrap.ServerBootstrap;
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

    private String serverAddress;
//    private ServiceRegistry serviceRegistry;
//    存放接口名与服务对象之间的映射关系
    private Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public void start() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

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
    }

    @Override
    public void stop() throws Exception {
//        TODO 先不写这个逻辑吧，后面再补
        System.out.println("RpcServer stop");
    }

    protected void addService(String interfaceName, String version, Object serviceBean) {
        log.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
//        先直接简单的装进去
        serviceMap.put(interfaceName+version, serviceBean);
//        TODO 使用工具类加密之后再存进去
    }
}
