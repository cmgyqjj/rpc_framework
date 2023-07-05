package com.qjj.common;

import java.io.Serializable;

/**
 * @author:qjj
 * @create: 2023-06-30 10:21
 * @Description: 请求实体类
 */

public class Invocation implements Serializable {
    private String interfaceName;
    private String methodName;
    private Class[] parametersType;
    private Object[] parameters;
//    private String version;

    public Invocation(String interfaceName, String methodName, Class[] parametersType, Object[] parameters) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parametersType = parametersType;
        this.parameters = parameters;
//        this.version = version;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParametersType() {
        return parametersType;
    }

    public void setParametersType(Class[] parametersType) {
        this.parametersType = parametersType;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

//    public String getVersion() {
//        return version;
//    }
//
//    public void setVersion(String version) {
//        this.version = version;
//    }
}
