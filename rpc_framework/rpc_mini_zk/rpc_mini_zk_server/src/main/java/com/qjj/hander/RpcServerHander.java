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

    public RpcServerHander(Map<String, Object> interfaceMap) {
        this.interfaceMap=interfaceMap;
    }

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



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
