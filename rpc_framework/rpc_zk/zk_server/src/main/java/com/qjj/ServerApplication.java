package com.qjj;

import com.qjj.service.HelloService;
import com.qjj.service.impl.HelloServiceImpl;

/**
 * @author:qjj
 * @create: 2023-06-29 13:12
 * @Description: 服务端启动
 */

public class ServerApplication {
    public static void main(String[] args) {
        try {
//            创建服务对象，并且注册到注册中心
            RpcFactory.registryService(HelloService.class,new HelloServiceImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
