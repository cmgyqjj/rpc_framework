package com.qjj.hander;

import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import com.qjj.factory.RpcFactory;
import com.qjj.registry.RpcRegistry;
import com.qjj.registry.RpcRegistryRequest;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-07-17 16:50
 * @Description: 服务端处理器
 */
@Slf4j
public class RpcServerHander extends SimpleChannelInboundHandler<RpcRequest> {

    //    在本地维护一个接口映射
    private Map<String, Object> interfaceMap;

    //    服务注册
    private RpcFactory rpcFactory;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest req) throws Exception {
        RpcResponse response = new RpcResponse();
        System.out.println("接受到客户端发送的数据：" + req.getRequestId());
        String interfaceName = req.getClassName();
        // 得到服务端相应服务实现类
        Object service = interfaceMap.get(interfaceName);
        // 反射调用方法
        Method method = null;
        try {
            method = service.getClass().getMethod(req.getMethodName(), req.getParameterTypes());
            Object invoke = method.invoke(service, req.getParameters());
            response.setCode(200);
            response.setResult(invoke);
            System.out.println("返回给客户端的数据：" + response.getResult());
            ctx.writeAndFlush(response);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            response.setCode(500);
            response.setError(e.toString());
            ctx.writeAndFlush(response);
        } finally {
            ctx.close();
        }
    }

    public void provideServiceInterface(Object service) {
        Class<?>[] interfaces = service.getClass().getInterfaces();

        for (Class clazz : interfaces) {
            // 本机的映射表
            interfaceMap.put(clazz.getName(), service);
            try {
//                使用注册中心工厂初始化对应的注册中心
                RpcRegistry zookeeper = rpcFactory.getInstance("localhost:2181", "zookeeper");
//                在注册中心注册服务
                RpcRegistryRequest rpcRegistryRequest = new RpcRegistryRequest();
                rpcRegistryRequest.setServiceName(clazz.getName());
                rpcRegistryRequest.setServiceVersion("0");
                zookeeper.register(rpcRegistryRequest);
                log.info("注册成功----");
            } catch (Exception e) {
                log.error("注册失败----");
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
