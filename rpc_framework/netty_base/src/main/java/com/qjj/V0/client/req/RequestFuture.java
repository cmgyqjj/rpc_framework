package com.qjj.V0.client.req;

import com.qjj.V0.client.resp.Response;
import lombok.Data;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:qjj
 * @create: 2023-10-23 00:44
 * @Description: 请求包装类
 */
@Data
public class RequestFuture {
//    请求缓存类，key为每次请求id，value为请求对象
    public static Map<Long,RequestFuture> futureMap=new ConcurrentHashMap<>();
//    对于每次请求可以设置原子性增长
    private long id;
//    请求参数
    private Object request;
//    响应结果
    private Object result;
//    超时时间，默认5s
    private long timeout=5000;
//    把请求放入缓存
    public static void addFuture(RequestFuture future){
        futureMap.put(future.getId(),future);
    }
//    同步获取响应结果
    public Object get(){
//        此处可以把同步块与wait换成ReentrantLock与Condition
        synchronized (this){
            try {
//                主线程默认等待5s，然后查看是否能获取到结果
//                这里实际上相当于用户下单之后，弹出来一个框一直在转，然后转的同时，就时不时去轮训，看看有没有结果
//                不过这个是在同一个服务上面，正常应该是在不同服务里面吧
                this.wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.result;
    }
//    异步线程将结果返回到主线程,目前如果服务器跑太慢，超时会发生什么呢？
    public static void receive(Response resp){
        RequestFuture future=futureMap.remove(resp.getId());
        if(future!=null){
            future.setResult(resp.getResult());
            synchronized (future){
                future.notify();
            }
        }
    }
}
