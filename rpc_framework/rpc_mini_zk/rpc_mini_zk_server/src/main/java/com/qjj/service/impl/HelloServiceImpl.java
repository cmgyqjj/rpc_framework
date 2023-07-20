package com.qjj.service.impl;

import com.qjj.service.HelloService;

/**
 * @author:qjj
 * @create: 2023-07-18 02:32
 * @Description: Hello远程调用接口实现类
 */

public class HelloServiceImpl implements HelloService {

    @Override
    public String Hello(String name) {
        return "hello:"+name;
    }
}
