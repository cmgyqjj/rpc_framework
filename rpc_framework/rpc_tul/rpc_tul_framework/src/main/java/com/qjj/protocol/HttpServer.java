package com.qjj.protocol;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;


/**
 * @author:qjj
 * @create: 2023-06-29 22:53
 * @Description: http服务启动器
 */

public class HttpServer {
    public void start(String hostname,Integer port){
//        读取用户的配置项，从配置文件或者naocs，netty里面读取都可以
//        这里从tomcat中读取
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(port);

        StandardEngine standardEngine = new StandardEngine();
        standardEngine.setDefaultHost(hostname);

        StandardHost standardHost = new StandardHost();
        standardHost.setName(hostname);

        String contextPath = "";
        StandardContext standardContext = new StandardContext();
        standardContext.setPath(contextPath);
        standardContext.addLifecycleListener(new Tomcat.FixContextListener());

        standardHost.addChild(standardContext);

        standardEngine.addChild(standardHost);

        service.setContainer(standardEngine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath,"dispatcher",new DispatcherServlet());
        standardContext.addServletMappingDecoded("/*","dispatcher");

        try {
            tomcat.start();
            tomcat.getServer().await();
        }catch (LifecycleException e){
            e.printStackTrace();
        }
    }
}
