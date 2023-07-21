package com.qjj.registry;

import java.io.IOException;

/**
 * @author:qjj
 * @create: 2023-07-21 22:57
 * @Description: rpc注册中心工厂类的接口
 */

public interface RpcRegistry {
    /**
    *@Param:
    *@return:
    *@Author: qjj
     * 注册服务
    */

    void register(RpcRegistryRequest request) throws Exception;
    /**
     *@Param:
     *@return:
     *@Author: qjj
     * 取消服务注册
     */
    void unRegister(RpcRegistryRequest request) throws Exception;
    /**
     *@Param:
     *@return:
     *@Author: qjj
     * 服务发现
     */
    RpcRegistryRequest discovery(String serviceName, int invokerHashCode) throws Exception;
    /**
     *@Param:
     *@return:
     *@Author: qjj
     * 销毁
     */
    void destroy() throws IOException;

}
