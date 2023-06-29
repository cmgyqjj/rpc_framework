package com.qjj.registry;

import com.qjj.connection.ZkConnection;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//注册器工具
//通过zk连接对象，和传入的Remote接口实现对象，完成RMI地址的拼接，和保存
//缺少LocateRegistry对象，缺少当前类型中属性的赋值过程，缺少ZkConnection的创建过程
public class RpcRegistry {

    private String ip;
    private int port;

//    连接对象
    private ZkConnection zkConnection;

    public RpcRegistry(ZkConnection connection, String serverIp, int serverPort) {
        this.zkConnection = connection;
        this.ip = serverIp;
        this.port = serverPort;
    }

    /**
    *@Param:
     * @param serviceInterface
     * @param serviceObject
    *@return:
     * void
    *@Author: qjj
    *@date:
    */
    public void register(Class<? extends Remote> serviceInterface, Remote serviceObject) throws IOException, InterruptedException, KeeperException {
        String rmi="rmi://"+ip+":"+port+"/"+serviceInterface.getName();
        String path="/qjj/rpc/"+serviceInterface.getName();
//        在zk里面创建节点的时候，必须先创建父节点
        zkConnection.getConnection().create(path,rmi.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        把服务对象在RMI的registry中注册
//        LocateRegistry.createRegistry(9090);
        Naming.rebind(rmi,serviceObject);

    }
    /**
     * 根据服务接口类型，访问zk，访问RMI的远程代理对象
     * 拼接一个zk中的节点名称
     * 访问zk，查询节点中存储的数据
     * 根据查询结果，创建代理对象
    *@Param:
     * serviceInterface
    *@return:
     * Object
    *@Author: qjj
    *@date:
    */
    public <T extends Remote> T getServiceProxy(Class<? extends Remote> serviceInterface) throws IOException, InterruptedException, KeeperException, NotBoundException {
        String path="/qjj/rpc/"+serviceInterface.getName();
        byte[] datas = zkConnection.getConnection().getData(path, false, null);
        String rmi=new String(datas);
        Object obj = Naming.lookup(rmi);
        return (T) obj;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ZkConnection getZkConnection() {
        return zkConnection;
    }

    public void setZkConnection(ZkConnection zkConnection) {
        this.zkConnection = zkConnection;
    }
}
