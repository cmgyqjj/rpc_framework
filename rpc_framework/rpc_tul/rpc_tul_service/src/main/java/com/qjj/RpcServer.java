package com.qjj;


import com.qjj.protocol.HttpServer;
import com.qjj.register.LocalRegister;
import com.qjj.service.HelloService;
import com.qjj.service.impl.HelloServiceImpl;

/**
 * @author:qjj
 * @create: 2023-06-29 22:52
 * @Description: rpc服务端
 */

public class RpcServer {
    public static void main(String[] args) {

        LocalRegister.register(HelloService.class.getName(),"1.0", HelloServiceImpl.class);
        // 1.启动tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost",7070);
    }
}
