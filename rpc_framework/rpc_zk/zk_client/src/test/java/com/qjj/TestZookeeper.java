package com.qjj;

import lombok.SneakyThrows;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestZookeeper {
    public static void main(String[] args) throws Exception {

    }

    public static void delete() throws IOException, InterruptedException, KeeperException {
        ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("触发了"+watchedEvent.getType()+"事件");
            }
        });
        Stat stat = new Stat();
        System.out.println(stat.getVersion());
        zookeeper.getData("/parent/sequence0000000001", false, stat);
        System.out.println(stat.getVersion());
        zookeeper.delete("/parent/sequence0000000001", stat.getCversion());

    }

    public static void get() throws IOException, InterruptedException, KeeperException {
        ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("触发了"+watchedEvent.getType()+"事件");
            }
        });
        byte[] bytes = zookeeper.getData("/parent", false, null);
        System.out.println(new String(bytes));
    }


    public static void listAll(ZooKeeper zooKeeper,String path) throws Exception{
        List<String> children = zooKeeper.getChildren(path, false);
        for(String child:children){
            String childPath = path.equals("/")?path+child:path+"/"+child;
            System.out.println(childPath);
            listAll(zooKeeper,childPath);
        }
    }

    public static void list() throws Exception{
        ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 10000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("触发了"+watchedEvent.getType()+"事件");
            }
        });
        List<String> children = zookeeper.getChildren("/", false);
        for(String child:children){
            System.out.println(child);
        }
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
//            创建客户端
            ZooKeeper zookeeper = new ZooKeeper("localhost:2181", 10000, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("触发了"+watchedEvent.getType()+"事件");
                }
            });
//            创建节点ZooDefs.Ids.CREATOR_ALL_ACL表示全部的权限
            String result = zookeeper.create("/parent", "parent data".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("创建节点成功"+result);
            String tmpResult = zookeeper.create("/parent/tmp", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            System.out.println("创建临时节点成功"+tmpResult);
            String SeqResult = zookeeper.create("/parent/sequence", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            System.out.println("创建顺序节点成功"+SeqResult);
            Thread.sleep(6000);
            zookeeper.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        }

    }
}
