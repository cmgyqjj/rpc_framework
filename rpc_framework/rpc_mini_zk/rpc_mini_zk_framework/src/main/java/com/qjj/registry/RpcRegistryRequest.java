package com.qjj.registry;

import lombok.Data;

/**
 * @author:qjj
 * @create: 2023-07-21 23:08
 * @Description: rpc服务注册通用请求类
 */
@Data
public class RpcRegistryRequest {
    private String serviceName;

    private String serviceVersion;

    private String serviceAddr;

    private int servicePort;
}
