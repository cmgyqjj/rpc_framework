package com.qjj;

import com.qjj.server.RpcServerNetty;
import com.qjj.service.impl.HelloServiceImpl;
import com.qjj.service.impl.UserServiceImpl;

/**
 * @author:qjj
 * @create: 2023-07-28 21:05
 * @Description: Rpc启动类
 */

public class NettyStart {
    public static void main(String[] args) {
        RpcServerNetty rpcServerNetty = new RpcServerNetty();
        rpcServerNetty.provideServiceInterface(new HelloServiceImpl());
        rpcServerNetty.provideServiceInterface(new UserServiceImpl());
        rpcServerNetty.start();
    }
}
