package com.qjj.server;

import com.qjj.annotation.NettyRpcService;
import com.qjj.server.core.NettyServer;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-07-07 14:45
 * @Description: Rpc服务类
 */

public class RpcServer extends NettyServer implements ApplicationContextAware , InitializingBean, DisposableBean {

    public RpcServer(String serverAddress, String registryAddress) {
        super(serverAddress, registryAddress);
    }

    /**
    *@Param: ApplicationContext ctx
    *@return: void
    *@Author: qjj
    *@date:
     * ApplicationContextAware接口的方法，当spring容器初始化的时候，会调用这个方法，这里是为了完成服务端注册
     * 通过注解找到类，通过类取到类名，通过类取到版本号，把类名和版本号和类对象放入到map中，这里是为了完成服务端注册
    */
    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
//        获取所有带有NettyRpcService助解的对象
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(NettyRpcService.class);
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
//                通过注解找到类
                NettyRpcService nettyRpcService = serviceBean.getClass().getAnnotation(NettyRpcService.class);
//                通过类取到类名
                String interfaceName = nettyRpcService.value().getName();
//                通过类取到版本号
                String version = nettyRpcService.version();
//                把类名和版本号和类对象放入到map中
                super.addService(interfaceName, version, serviceBean);
            }
        }
    }

    /**
    *@Param: void
    *@return: void
    *@Author: qjj
    *@date:
     * InitializingBean接口的方法，当spring容器初始化完成后，会调用这个方法，这里是为了完成服务端注册
    */
    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

    /**
    *@Param: void
    *@return: void
    *@Author: qjj
    *@date:
     * DisposableBean接口的方法，当spring容器销毁的时候，会调用这个方法，这里是为了完成服务端的销毁
    */
    @Override
    public void destroy() throws Exception {
        super.stop();
    }
}
