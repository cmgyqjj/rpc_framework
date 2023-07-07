package com.qjj.server.core;

/**
 * @author:qjj
 * @create: 2023-07-07 14:51
 * @Description: rpc服务抽象类
 */

public abstract class Server {
    /**
     * start server
     *
     * @param
     * @throws Exception
     */
    public abstract void start() throws Exception;

    /**
     * stop server
     *
     * @throws Exception
     */
    public abstract void stop() throws Exception;

}
