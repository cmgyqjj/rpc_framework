package com.qjj.service;

public interface HelloService {
    // 客户端通过这个接口调用服务端的实现类
    String Hello(String name);
}
