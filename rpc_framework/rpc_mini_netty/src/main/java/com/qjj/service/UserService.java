package com.qjj.service;

/**
 * @author:qjj
 * @create: 2023-07-14 23:25
 * @Description: 需要远程调用的接口
 */

public interface UserService {
    // 客户端通过这个接口调用服务端的实现类
    String getUserByUserId(Integer id);
}
