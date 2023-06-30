package com.qjj.protocol;

import com.qjj.common.Invocation;
import com.qjj.register.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author:qjj
 * @create: 2023-06-29 23:08
 * @Description: Http请求处理器
 */

public class HttpServerHandler {
    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();
            String interfaceName = invocation.getInterfaceName();
//            根据接口名，找到实现类
//            TODO 这个可以读取配置或者实现一个默认的版本号
            Class classImpl = LocalRegister.get(interfaceName, "1.0");
            Method method = classImpl.getMethod(invocation.getMethodName(), invocation.getParametersType());
            String result = (String) method.invoke(classImpl.newInstance(), invocation.getParameters());
            IOUtils.write(result,resp.getOutputStream(),"UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
