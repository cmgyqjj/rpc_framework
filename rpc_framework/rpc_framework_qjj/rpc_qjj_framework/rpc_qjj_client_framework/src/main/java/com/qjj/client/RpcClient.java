package com.qjj.client;

import com.qjj.service.HelloService;

/**
 * @author:qjj
 * @create: 2023-07-14 15:56
 * @Description: RPC客户端类
 */

public class RpcClient {



    public RpcClient(String s) {
//        TODO 服务发现
    }

    public HelloService createService(Class<HelloService> helloServiceClass, String s) {
//    TODO 获取代理对象
        return null;
    }

    public void stop() {
//        TODO 关闭客户端
    }
}
