package com.qjj.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * 服务端基本配置，通过一个静态单例类，保证启动时候只被加载一次
 */
@Component
public class WsServer {
	
	/**
	 * 单例静态内部类
	 */
	public static class SingletionWServer{
		static final WsServer instance = new WsServer(); 
	}
	
	public static WsServer getInstance(){
		return SingletionWServer.instance;
	}
	
	private EventLoopGroup mainGroup ;
	private EventLoopGroup subGroup;
	private ServerBootstrap server;
	private ChannelFuture future;
	
	public WsServer(){
		mainGroup = new NioEventLoopGroup();
		subGroup = new NioEventLoopGroup();
		server = new ServerBootstrap();
		server.group(mainGroup, subGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new WsServerInitialzer());//添加自定义初始化处理器
	}
	public void start(){
		future = this.server.bind(8081);
		System.err.println("netty 服务端启动完毕 .....");
	}
}	
