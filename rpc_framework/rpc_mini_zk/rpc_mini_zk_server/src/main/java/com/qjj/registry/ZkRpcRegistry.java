package com.qjj.registry;

import com.qjj.connection.ZkConnection;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
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
 *  TODO 使用统一的注解
 */
@Slf4j
public class ZkRpcRegistry implements RpcRegistry{


    private CuratorFramework zooKeeper;

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
            // serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            if(zooKeeper.checkExists().forPath("/" + request.getServiceName()) == null){
                zooKeeper.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + request.getServiceName());
            }
            // 路径地址，一个/代表一个节点
            String path = "/" + request.getServiceName() +"/"+ request.getServiceAddr();
            // 临时节点，服务器下线就删除节点
            zooKeeper.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e) {
            System.out.println("此服务已存在--");
        }
    }

    @Override
    public void unRegister(RpcRegistryRequest request) throws Exception {
//    TODO 取消服务注册
    }

    @Override
    public RpcRegistryRequest discovery(RpcRegistryRequest request) throws Exception {
        try {
            List<String> strings = zooKeeper.getChildren().forPath("/" + request.getServiceName());
            // 这里默认用的第一个，后面加负载均衡
            String string = strings.get(0);
            RpcRegistryRequest rpcRegistryRequest = new RpcRegistryRequest();
            rpcRegistryRequest.setServiceAddr(string);
            return rpcRegistryRequest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void destroy() throws IOException {
//        TODO 销毁
    }
}
