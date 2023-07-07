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

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }

    @Override
    public void destroy() throws Exception {
        super.stop();
    }
}
