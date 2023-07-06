package com.qjj.service.impl;

import com.qjj.service.HelloService;

import java.rmi.RemoteException;

/**
 * @author:qjj
 * @create: 2023-07-06 23:37
 * @Description: 服务实现类第二个版本
 */

public class HelloServiceImpl2 implements HelloService {
    public HelloServiceImpl2() {

    }

    @Override
    public String sayHello(String name) {
        return "hello:"+name;
    }
}
