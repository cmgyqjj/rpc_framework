package com.qjj;

import com.qjj.client.RpcClientNetty;

/**
 * @author:qjj
 * @create: 2023-07-28 21:31
 * @Description: 客户端启动器
 */

public class ClientStart {
    public static void main(String[] args) {
        RpcClientNetty rpcClientNetty = new RpcClientNetty();
        rpcClientNetty.start();
    }

}
