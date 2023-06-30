package com.qjj;


import com.qjj.common.Invocation;
import com.qjj.protocol.HttpClient;
import com.qjj.service.HelloService;

/**
 * @author:qjj
 * @create: 2023-06-30 10:46
 * @Description:Rpc客户端请求
 */

public class RpcClient {

    public static void main(String[] args) {
        Invocation invocation = new Invocation(HelloService.class.getName(), "hello",
                new Class[]{String.class}, new Object[]{"qjj"}, "1.0");
        HttpClient httpClient = new HttpClient();
        String result = httpClient.send("localhost", 7070, invocation);
        System.out.println(result);

    }
}
