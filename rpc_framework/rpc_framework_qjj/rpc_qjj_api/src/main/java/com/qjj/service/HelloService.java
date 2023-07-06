package com.qjj.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author:qjj
 * @create: 2023-06-29 13:07
 * @Description: 服务接口
 */

public interface HelloService extends Remote {
    String sayHello(String name) throws RemoteException;
}
