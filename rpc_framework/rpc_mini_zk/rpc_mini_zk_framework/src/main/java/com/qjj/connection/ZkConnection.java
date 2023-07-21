package com.qjj.connection;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author:qjj
 * @create: 2023-07-21 21:55
 * @Description: zk连接器
 */

@Slf4j
public class ZkConnection {
    private String zkServer;
    private int sessionTimeout;
    private CountDownLatch latch = new CountDownLatch(1);
    public ZkConnection(){
        super();
        this.zkServer="localhost:2181";
        this.sessionTimeout=10000;
    }

    public ZkConnection(String zkServer) {
        this.zkServer = zkServer;
        this.sessionTimeout=10000;
    }
    public ZkConnection(String zkServer, int sessionTimeout) {
        this.zkServer = zkServer;
        this.sessionTimeout = sessionTimeout;
    }
    public ZooKeeper getConnection() throws IOException {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(zkServer, sessionTimeout, event -> {
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            log.error("Can't get connection for zookeeper,registryAddress:{}", zkServer, e);
        }
        return zk;
    }

}
