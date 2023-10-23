package com.qjj.V1.client.req;

import com.qjj.V1.client.resp.Response;
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

}
