package com.qjj.proxy;

import com.qjj.common.Invocation;
import com.qjj.common.URL;
import com.qjj.loadbalance.LoadBalance;
import com.qjj.protocol.HttpClient;
import com.qjj.register.MapRemoteRegister;

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

public class ProxyFactory {

    public static <T> T getProxy(Class interfaceClass){
//           用户配置
        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(),
                        method.getParameterTypes(), args);
                HttpClient httpClient = new HttpClient();
//                这里不能写死，写成这个样子 String result = httpClient.send("localhost", 7070, invocation);
//                服务发现
                List<URL> urls = MapRemoteRegister.get(interfaceClass.getName());

//                服务调用
                String result=null;
                List<URL> invokedUrls = new ArrayList<>();
                int max=3;
                while(max>0){
//                负载均衡
                    urls.remove(invokedUrls);
                    URL url = LoadBalance.random(urls);
                    invokedUrls.add(url);
                    try{
                        result = httpClient.send(url.getHostname(), url.getPort(), invocation);
                        System.out.println(result);
                        return result;
                    }catch (Exception e) {
//                    服务容错
                        if(max--!=0){
                            continue;
                        }
                        e.printStackTrace();
                        return "报错了";
                    }
                }
                return result;
            }
        });
        return (T) proxyInstance;
    }

}
