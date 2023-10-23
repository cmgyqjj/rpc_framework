package com.qjj.V0.client;

import com.qjj.V0.client.req.RequestFutureV1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author:qjj
 * @create: 2023-10-23 00:38
 * @Description: 主线程异步类，主线程模拟并发请求，开启额外线程模拟获取响应结果
 * 并异步的将响应结果返回给主线程
 */

public class FutureMainV2 {
    public static void main(String[] args) {
//        请求列表
        List<RequestFutureV1> requestFutureList =new ArrayList<>();
        /*
        此处用for循环模拟连续发送100个请求
        异步构建100调线程获取请求，并返回响应结果
        当然，此处还可以用线程次模拟构建100条线程发送请求
        然后主线程等待所有子线程获取对应的响应请求。希望读者对代码进行响应的修改。
         */
        for(int i=0;i<100;i++){
            long id=i;
            RequestFutureV1 req=new RequestFutureV1();
            req.setId(id);
//            设置请求内容
            req.setRequest("请求参数"+id);
//            缓存请求
            RequestFutureV1.addFuture(req);
//            把请求加入请求列表
            requestFutureList.add(req);
//            模拟发送请求
            sendMsg(req);
//            模拟线程获取对应的请求
            SubThreadV2 subThread=new SubThreadV2(req);
            subThread.start();
        }
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for(RequestFutureV1 req: requestFutureList){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Object result=req.get();
                    System.out.println("获取响应结果："+result);
                }
            });
        }
    }
    private static void sendMsg(RequestFutureV1 req){
        System.out.println("发送请求："+req.getRequest());
    }

}
