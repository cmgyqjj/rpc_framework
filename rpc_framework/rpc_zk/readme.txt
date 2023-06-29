尚学堂教程讲的rpc
框架核心:
    服务端启动的时候，提供默认的服务端口号。如9090
    服务端启动的时候，必须指定Zookeeper所在位置。也就是zk的ip和端口
    服务端启动的时候，提供要发布的服务的接口的实现类的包路径。如com.qjj.service.impl
    服务端启动的时候，RMI的URI地址，由固定的逻辑拼接，不是随机自定义
        如Remote接口的实现是：com.qjj.service.impl.HelloServiceImpl。接口是HelloService
            URI自动拼接为：rmi://ip:port/HelloServiceImpl
    服务端启动的时候，服务端的IP由启动代码指定，或自动获取。

    客户端启动的时候，必须指定Zookeeper所在地址
    客户端启动的时候，必须提供要创建代理的接口类型。如com.qjj.service.HelloService
    自动连接zookeeper，拼接一个有规则的节点名称，如：/qjj/com.qjj.service.HelloService
        节点中保存的数据data，就是服务端提供的服务的URI地址，如：rmi://ip:port/HelloServiceImpl
    客户端使用的代理对象，是由规则生成，不需要客户端开发者来记忆RMI地址
