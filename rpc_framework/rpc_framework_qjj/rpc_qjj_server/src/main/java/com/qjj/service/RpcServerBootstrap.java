package com.qjj.service;

/**
 * @author:qjj
 * @create: 2023-07-06 23:45
 * @Description: rpc服务器端的启动器,负责组装rpc组件，协调他们的工作
 */

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcServerBootstrap {
    public static void main(String[] args) {
//
        new ClassPathXmlApplicationContext("server-spring.xml");
    }
}
