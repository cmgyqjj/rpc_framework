# 一个极短代码实现的RPC调用过程，基于Socket实现

通过运行RPCServer和RPCClient1来模拟RPC调用过程

RPCServer启动后，会监听端口，等待RPCClient1的连接

RPCClient1启动后，会连接RPCServer，然后发送请求

RPCServer接收到请求后，会解析请求，然后调用相应的方法，得到结果

RPCServer将结果返回给RPCClient1

RPCClient1接收到结果后，将结果打印出来

## TODO List

1. 使用基于NIO的Netty替换的基于BIO的Socket
2. 需要通用消息格式来处理不同的方法的调用
3. 支持不同方法的调用，并且支持同方法不同版本的调用 
4. 没有实现服务器注册与发现
5. 实现不同的负载均衡策略