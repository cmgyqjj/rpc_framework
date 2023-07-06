package com.qjj.service.impl;

import com.qjj.service.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author:qjj
 * @create: 2023-06-29 13:08
 * @Description: 服务实现类
 */

public class HelloServiceImpl implements HelloService {
    public HelloServiceImpl(){
    }

    @Override
    public String sayHello(String name){
        return "hi:"+name;
    }
}
