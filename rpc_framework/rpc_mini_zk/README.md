# 一个极短代码实现的RPC调用过程，基于Netty实现

通过运行RPCServerNetty和RPCClientNetty来模拟RPC调用过程

本例相比较rpc_mini_req_resp使用netty实现了基础的rpc调用以外

还额外添加来Zookeeper作为注册中心

这是因为在rpc调用过程中，需要调用的方法和需要传输的数据是不确定的

所以需要提供服务注册和服务发现的功能


## TODO List

1. ~~使用基于NIO的Netty替换的基于BIO的Socket~~
2. ~~需要通用消息格式来处理不同的方法的调用~~
3. ~~支持不同方法的调用~~
4. ~~对多个类进行管理，而不是只能调用一个类的方法~~
5. 支持同方法不同版本的调用 
6. 因为代码量的增加，需要重构整个项目结构 (***重要)
7. ~~没有实现服务器注册与发现,需要整合注册中心~~
7. 实现不同的负载均衡策略 
8. 尝试更好的写法，在保证功能的情况下，尽量使用最通俗的语法来实现各功能
9. 支持不同的注册中心，例如zookeeper，Eureka等等
10. 支持注解标识需要注册的接口
11. 支持更多的代理方式，例如cglib
12. 不使用固定通信管道，而使用回调函数的方式来接受远程调用返回值
