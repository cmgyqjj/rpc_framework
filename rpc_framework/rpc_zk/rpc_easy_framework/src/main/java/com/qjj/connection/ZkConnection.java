package com.qjj.connection;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

//zk连接对象
public class ZkConnection {
    private String zkServer;
    private int sessionTimeout;
    public ZkConnection(){
        super();
        this.zkServer="localhost:2181";
        this.sessionTimeout=10000;
    }
    public ZkConnection(String zkServer, int sessionTimeout) {
        this.zkServer = zkServer;
        this.sessionTimeout = sessionTimeout;
    }
    public ZooKeeper getConnection() throws IOException {
        return new ZooKeeper(zkServer, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }

}
