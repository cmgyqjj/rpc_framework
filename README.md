## RPC Implementation Frameworks
**[中文版](READMEChina.md)｜[English](README.md)**

This project contains several implementations of Remote 
Procedure Call (RPC) frameworks, including:

1. rpc_zk by Shanguigu
2. rpc_tul by Tuling
3. rpc_mini, a simple RPC call process implemented using only sockets
4. rpc_mini_netty, a simple RPC call process implemented using only Netty
5. rpc_min_req_resp, which encapsulates Request and Response classes and adds dynamic proxy and reflection to support calling different methods with different parameters.
6. rpc_mini_zk, which adds ZooKeeper to implement service registration and discovery on the basis of rpc_mini_req_resp (TODO)


# Other Work
We are currently developing several other RPC frameworks.

To facilitate learning, we plan to split each improvement 
into separate parts, similar to a melting experiment. 

We will also supplement the documentation to better explain 
the contributions made by each improvement and to facilitate 
further improvement.

## TODO LIST

- [ ] Add supporting documentation, including comments for each method
- [ ] Continue to add more complex RPC frameworks
- [ ] Refactor existing RPC frameworks for better understanding
- [ ] Support multiple encoders and decoders
- [ ] Support multiple registry centers
- [ ] Any other extension or improvement

## Latest Update: July 30, 2023

If you have any questions, please feel free to raise an issue. 
If you find this helpful for your learning, would you kindly consider giving it a star? 
Thank you, and I will continue to update it in the future.
