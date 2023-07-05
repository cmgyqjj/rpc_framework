package com.qjj;


import com.qjj.common.URL;
import com.qjj.protocol.HttpServer;
import com.qjj.register.LocalRegister;
import com.qjj.register.MapRemoteRegister;
import com.qjj.service.HelloService;
import com.qjj.service.impl.HelloServiceImpl;

/**
 * @author:qjj
 * @create: 2023-06-29 22:52
 * @Description: rpc服务端
 */

public class RpcServer {
    public static void main(String[] args) {
//        本地注册
        LocalRegister.register(HelloService.class.getName(),"1.0", HelloServiceImpl.class);
//        注册中心注册
//        TODO 这里需要通过代码从本机拿到
        URL url=new URL("localhost",7070);
        MapRemoteRegister.register(HelloService.class.getName(),url);
        // 1.启动tomcat
        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostname(),url.getPort());
    }
}
