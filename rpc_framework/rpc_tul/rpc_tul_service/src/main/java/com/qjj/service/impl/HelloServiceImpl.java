package com.qjj.service.impl;


import com.qjj.service.HelloService;

/**
 * @author:qjj
 * @create: 2023-06-29 20:01
 * @Description: 服务实现类
 */

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name + "!";
    }
}
