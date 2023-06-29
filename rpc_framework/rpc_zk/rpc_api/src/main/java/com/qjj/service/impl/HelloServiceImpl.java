package com.qjj.service.impl;

import com.qjj.service.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author:qjj
 * @create: 2023-06-29 13:08
 * @Description: 服务实现类
 */

public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {
    public HelloServiceImpl() throws RemoteException {
    }

    @Override
    public String sayHello(String name) throws RemoteException{
        return "hello"+name;
    }
}
