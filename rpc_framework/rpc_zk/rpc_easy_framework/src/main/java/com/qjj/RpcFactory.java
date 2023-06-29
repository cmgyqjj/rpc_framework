package com.qjj;

import com.qjj.connection.ZkConnection;
import com.qjj.registry.RpcRegistry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Properties;

/**
 * @author:qjj
 * @create: 2023-06-29 11:44
 * @Description: RPC框架入口
 */

public class RpcFactory {

//    用来保存配置信息
    private static final Properties config=new Properties();

    private static final ZkConnection connection;
    private static final RpcRegistry registry;

    /**
     * 初始化过程
     * 固定逻辑。在classpath下，提供配置文件，命名为rpc.properties
     * 配置文件结构固定化
     * registry.ip=服务端ip，默认Localhost
     * registry.port=服务端端口，默认9090
     * zk.server=Zookeeper访问地址，默认localhost:2181
     * zk.sessionTimeout=Zookeeper会话超时时间，默认10000
    */
    static{
        try {
            InputStream input = RpcFactory.class.getClassLoader().getResourceAsStream("rpc.properties");
            config.load(input);
            String serverIp = config.getProperty("registry.ip", "localhost");
            int serverPort = Integer.parseInt(config.getProperty("registry.port", "9090"));
            String zkServer = config.getProperty("zk.server", "localhost:2181");
            int sessionTimeout = Integer.parseInt(config.getProperty("zk.sessionTimeout", "10000"));
            connection = new ZkConnection(zkServer, sessionTimeout);
            registry = new RpcRegistry(connection, serverIp, serverPort);
            System.out.println("静态代码块被调用");
            LocateRegistry.createRegistry(serverPort);
//            初始化zk中的父节点/qjj/rpc
            List<String> children = connection.getConnection().getChildren("/", false);
            if(!children.contains("qjj")){
                connection.getConnection().create("/qjj",null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            List<String> qjjChildren = connection.getConnection().getChildren("/qjj", false);
            if(!qjjChildren.contains("rpc")){
                connection.getConnection().create("/qjj/rpc",null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (KeeperException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 根据服务接口类型，服务对象，访问zk，将服务接口类型和服务对象存储到zk中
    *@Param:
     * serviceInterface 服务接口类型
     * serviceObject 服务对象
    *@return:
    *@Author: qjj
    *@date:
    */
    public static void registryService(Class<? extends Remote> serviceInterface, Remote serviceObject) throws IOException, InterruptedException, KeeperException {
        registry.register(serviceInterface,serviceObject);
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
    public static <T extends Remote> T getServiceProxy(Class<T> serviceInterface) throws IOException, InterruptedException, KeeperException, NotBoundException {
        return registry.getServiceProxy(serviceInterface);
    }

}
