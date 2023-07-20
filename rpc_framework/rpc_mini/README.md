# 一个极短代码实现的RPC调用过程，基于Socket实现

通过运行RPCServer和RPCClient1来模拟RPC调用过程

RPCServer启动后，会监听端口，等待RPCClient1的连接

RPCClient1启动后，会连接RPCServer，然后发送请求

RPCServer接收到请求后，会解析请求，然后调用相应的方法，得到结果

RPCServer将结果返回给RPCClient1

RPCClient1接收到结果后，将结果打印出来

## TODO List

1. 尝试更好的写法，在保证功能的情况下，尽量使用最通俗的语法来实现各功能