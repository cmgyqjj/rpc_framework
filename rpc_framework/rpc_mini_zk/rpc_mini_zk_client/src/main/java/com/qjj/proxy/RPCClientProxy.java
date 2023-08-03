package com.qjj.proxy;


import com.qjj.client.RpcClientNetty;
import com.qjj.common.RpcRequest;
import com.qjj.common.RpcResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:qjj
 * @create: 2023-07-01 23:51
 * @Description: 代理对象工厂
 */

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {

    private RpcClientNetty rpcClientNetty;

    // jdk 动态代理
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = RpcRequest.builder().className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args).parameterTypes(method.getParameterTypes()).build();
        RpcResponse response = rpcClientNetty.sendRequest(request);
        return response.getResult();
    }
    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}


