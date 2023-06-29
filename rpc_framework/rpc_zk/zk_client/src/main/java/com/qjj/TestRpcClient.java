package com.qjj;

import com.qjj.registry.RpcRegistry;
import com.qjj.service.HelloService;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.rmi.NotBoundException;

/**
 * @author:qjj
 * @create: 2023-06-29 13:28
 * @Description: 测试给予自定义RPC框架的客户端
 */

public class TestRpcClient {
    public static void main(String[] args) throws NotBoundException, IOException, InterruptedException, KeeperException {
        HelloService helloService = RpcFactory.getServiceProxy(HelloService.class);
        System.out.println(helloService.getClass().getName());
        String result = helloService.sayHello("qjj");
        System.out.println(result);
    }
}
