package com.qjj;


import com.qjj.common.Invocation;
import com.qjj.protocol.HttpClient;
import com.qjj.proxy.ProxyFactory;
import com.qjj.service.HelloService;

/**
 * @author:qjj
 * @create: 2023-06-30 10:46
 * @Description:Rpc客户端请求
 */

public class RpcClient {

    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String result = helloService.hello("qjj");
        System.out.println(result);
    }
}
