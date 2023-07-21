package com.qjj.registry;

import com.qjj.connection.ZkConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * @author:qjj
 * @create: 2023-07-21 21:57
 * @Description: Rpc注册中心
 */
@Slf4j
public class ZkRpcRegistry implements RpcRegistry{

    private ZooKeeper zooKeeper;

    public ZkRpcRegistry(String registryAddr) {
        try {
            ZkConnection zkConnection = new ZkConnection(registryAddr);
            zooKeeper = zkConnection.getConnection();
        } catch (IOException e) {
            log.error("Can't get connection for zookeeper,registryAddress:{}", registryAddr, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(RpcRegistryRequest request) throws Exception {

    }

    @Override
    public void unRegister(RpcRegistryRequest request) throws Exception {

    }

    @Override
    public RpcRegistryRequest discovery(String serviceName, int invokerHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy() throws IOException {

    }
}
