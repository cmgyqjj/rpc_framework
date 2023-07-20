package com.qjj.service.impl;


import com.qjj.service.UserService;

/**
 * @author:qjj
 * @create: 2023-07-14 23:26
 * @Description: User远程调用的接口实现类
 */
public class UserServiceImpl implements UserService {
    @Override
    public String getUserByUserId(Integer id) {
        return "用户:"+id;
    }
}
