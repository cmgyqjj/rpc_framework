package com.qjj.registry;

import com.qjj.connection.ZkConnection;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * @author:qjj
 * @create: 2023-07-21 21:57
 * @Description: Rpc注册中心
 *  TODO 使用统一的逐渐
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
        try {
            String registerName = request.getServiceName() + request.getServiceVersion();
            String path = zooKeeper.create("/registry/data", registerName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.info("create zookeeper node ({} => {})", path, registerName);
        } catch (KeeperException | InterruptedException e) {
            log.error("", e);
        }
    }

    @Override
    public void unRegister(RpcRegistryRequest request) throws Exception {
//    TODO 取消服务注册
    }

    @Override
    public RpcRegistryRequest discovery(RpcRegistryRequest request) throws Exception {
//        TODO 服务发现
        return null;
    }

    @Override
    public void destroy() throws IOException {
//        TODO 销毁
    }
}
