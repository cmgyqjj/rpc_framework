package com.qjj.connection;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
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

    private CuratorFramework client;
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
    public CuratorFramework getConnection() throws IOException {
        // 指数时间重试
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 3);
        // zookeeper的地址固定，不管是服务提供者还是，消费者都要与之建立连接
        // sessionTimeoutMs 与 zoo.cfg中的tickTime 有关系，
        // zk还会根据minSessionTimeout与maxSessionTimeout两个参数重新调整最后的超时值。默认分别为tickTime 的2倍和20倍
        // 使用心跳监听状态
        this.client = CuratorFrameworkFactory.builder().connectString(this.zkServer)
                .sessionTimeoutMs(this.sessionTimeout).retryPolicy(policy).namespace("ZkRpc").build();
        this.client.start();
        log.info("zookeeper连接成功");
        return this.client;
    }

}
