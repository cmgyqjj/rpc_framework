package com.qjj.client;

import com.qjj.service.HelloService;
import com.qjj.service.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;

import java.rmi.RemoteException;

/**
 * @author:qjj
 * @create: 2023-07-14 15:27
 * @Description: RPC客户端调用请求类
 */
@Slf4j
public class RpcClientBootstrap {
    /**
    *@Param:
    *@return:
    *@Author: qjj
    *@date:
     * 一个测试方法
    */
    public static void main(String[] args) throws RemoteException {
//    创建一个RpcClient对象，用于发送请求
        final RpcClient rpcClient = new RpcClient("127.0.0.1:2181");
//        通过RpcClient对象创建一个HelloService的代理对象
        final HelloService syncClient = rpcClient.createService(HelloService.class, "1.0");
//        调用代理对象的方法
        String result = syncClient.sayHello("qjj");
        log.info("调用结果:{}",result);
//        关闭RpcClient对象
        rpcClient.stop();
    }
}
