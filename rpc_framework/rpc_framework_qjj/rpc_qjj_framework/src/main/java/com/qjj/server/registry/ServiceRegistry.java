package com.qjj.server.registry;

import com.qjj.protocol.RpcServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author:qjj
 * @create: 2023-07-11 17:07
 * @Description: 服务注册类
 */

@Slf4j
public class ServiceRegistry {

    private ZooKeeper zk;

    public ServiceRegistry(String registryAddress) {
        zk = connectServer(registryAddress,5000);
    }

    private CountDownLatch latch = new CountDownLatch(1);

    public void registerService(String host, int port, Map<String, Object> serviceMap) {
//        List<RpcServiceInfo> serviceInfoList = new ArrayList<>();
        for (String key : serviceMap.keySet()) {
//            因为在刚刚的NettyServer中使用的是#作为分隔符，所以这里也要使用#作为分隔符
            String[] serviceInfo = key.split("#");
            if (serviceInfo.length > 0) {
                RpcServiceInfo rpcServiceInfo = new RpcServiceInfo();
                rpcServiceInfo.setServiceName(serviceInfo[0]);
                if (serviceInfo.length == 2) {
                    rpcServiceInfo.setVersion(serviceInfo[1]);
                } else {
                    rpcServiceInfo.setVersion("");
                }
                log.info("Register new service: {} ", key);
//                处理好之后放到List里面
                createNode(zk,rpcServiceInfo.toString());
//                serviceInfoList.add(rpcServiceInfo);
            } else {
                log.warn("Can not get service name and version: {} ", key);
            }
        }
    }


    private ZooKeeper connectServer(String registryAddress, int sessionTimeout) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, sessionTimeout, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            log.info("Can't connect to zookeeper"+registryAddress,e);
        }
        return zk;
    }

    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create("/data", bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            log.debug("create zookeeper node ({} => {})", path, data);
        } catch (KeeperException | InterruptedException e) {
            log.error("", e);
        }
    }

    public void unregisterService() {
        log.info("Unregister all service");
//        TODO 未完待续
    }
}
