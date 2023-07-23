package com.qjj.factory;

import com.qjj.registry.RpcRegistry;
import com.qjj.registry.ZkRpcRegistry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:qjj
 * @create: 2023-07-21 21:57
 * @Description: Rpc工厂，想要用工厂来创建不同的注册中心，比如zk的，eurak的
 */
@Slf4j
public class RpcFactory {

    private static volatile RpcRegistry rpcRegistry;

    public static RpcRegistry getInstance(String registryAddr, String registryType) throws Exception {
        if("zookeeper".equals(registryType)){
            rpcRegistry = new ZkRpcRegistry(registryAddr);
        }else{
            log.info("不支持的注册中心类型");
        }
        return rpcRegistry;
    }
}
