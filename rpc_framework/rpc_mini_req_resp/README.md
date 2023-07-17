# 一个极短代码实现的RPC调用过程，基于Netty实现

通过运行RPCServerNetty和RPCClientNetty来模拟RPC调用过程

本例相比较rpc_mini_netty使用netty实现了基础的rpc调用以外

还额外添加了统一的Request和Response类

为什么需要统一的Request和Response类？

因为在rpc调用过程中，需要调用的方法和需要传输的数据是不确定的

所以需要一个通用的类来管理方法和事物

## TODO List

1. ~~使用基于NIO的Netty替换的基于BIO的Socket~~
2. ~~需要通用消息格式来处理不同的方法的调用~~
3. ~~支持不同方法的调用~~
4. ~~对多个类进行管理，而不是只能调用一个类的方法~~
5. 支持同方法不同版本的调用 
6. 没有实现服务器注册与发现,需要整合注册中心 
7. 实现不同的负载均衡策略
