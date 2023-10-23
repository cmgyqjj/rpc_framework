package com.qjj.V0.client;

import com.qjj.V0.client.req.RequestFutureV1;
import com.qjj.V0.client.resp.Response;

/**
 * @author:qjj
 * @create: 2023-10-23 00:59
 * @Description:
 */

public class SubThreadV2 extends Thread{

    private RequestFutureV1 request;
    public SubThreadV2(RequestFutureV1 request) {
        this.request=request;
    }

    @Override
    public void run() {
//        模拟额外线程获取响应结果
        Response resp=new Response();
        resp.setId(request.getId());
        resp.setResult("响应结果"+Thread.currentThread()+"------"+request.getId());
//        子线程模拟睡眠1s
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        异步将结果返回给主线程
        RequestFutureV1.receive(resp);
    }
}
