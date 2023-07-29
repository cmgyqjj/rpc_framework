package com.qjj.registry;

import com.qjj.connection.ZkConnection;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
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

    public ZkRpcRegistry() {
        try {
            ZkConnection zkConnection = new ZkConnection();
            zooKeeper = zkConnection.getConnection();
        } catch (IOException e) {
            log.error("Can't get connection for zookeeper,registryAddress:{}", e);
            throw new RuntimeException(e);
        }
    }

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
            String path = "/" + request.getServiceName() +"/"+ request.getServiceAddr()+":"+request.getServicePort();
            // 临时节点，服务器下线就删除节点
//            可以启动后在/zkRpc/类名 下找到对应的地址
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
    public RpcRegistryRequest discovery(String className) throws Exception {
        try {
//            TODO 这里好像会报错，因为有会话过期？
            List<String> strings = zooKeeper.getChildren().forPath("/" + className);
            // 这里默认用的第一个，后面加负载均衡
//            7.29 再经过上次修改之后，这里已经可以成功找到对应的地址和端口了
            String string = strings.get(0);
            RpcRegistryRequest rpcRegistryRequest = new RpcRegistryRequest();
//            TODO 这个传递过程还是得找个统一的来规范一下，之前注册的时候ServiceAddr只是地址，但是这里把端口号也带上了，纯粹是懒了
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
