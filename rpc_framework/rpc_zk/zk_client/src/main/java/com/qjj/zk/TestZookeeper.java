package com.qjj.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class TestZookeeper {
    public static void main(String[] args) {

    }

    /**
     * 什么是会话？
     *  持久、长期、有状态的
     * 使用java远程访问zk，步骤如下：
     * 1.创建客户端
     * 2.使用客户端发送命令
     * 3.处理返回结果
     * 4.回收资源
     */
    public static void create(){
        try {
            ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 10000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("触发了"+watchedEvent.getType()+"事件");
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
