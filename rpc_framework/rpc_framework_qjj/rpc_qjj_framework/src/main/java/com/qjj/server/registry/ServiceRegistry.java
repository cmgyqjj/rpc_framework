package com.qjj.server.registry;

import com.qjj.protocol.RpcServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-07-11 17:07
 * @Description: 服务注册类
 */

@Slf4j
public class ServiceRegistry {


    public void registerService(String host, int port, Map<String, Object> serviceMap) {
        List<RpcServiceInfo> serviceInfoList = new ArrayList<>();
        for (String key : serviceMap.keySet()) {
//            因为在刚刚的NettyServer中使用的是#作为分隔符，所以这里也要使用#作为分隔符
            String[] serviceInfo = key.split("#");
            if (serviceInfo.length > 0) {
                RpcServiceInfo rpcServiceInfo = new RpcServiceInfo();
                rpcServiceInfo.setServiceName(serviceInfo[0]);
                if (serviceInfo.length == 2) {
                    rpcServiceInfo.setVersion(serviceInfo[1]);
                } else {
                    rpcServiceInfo.setVersion("");
                }
                log.info("Register new service: {} ", key);
//                处理好之后放到List里面
                serviceInfoList.add(rpcServiceInfo);
            }else {
                log.warn("Can not get service name and version: {} ", key);
            }
    }

    public void unregisterService() {

    }
}
