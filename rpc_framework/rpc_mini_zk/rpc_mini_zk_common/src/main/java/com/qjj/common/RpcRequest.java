package com.qjj.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author:qjj
 * @create: 2023-07-15 19:53
 * @Description: rpc请求统一包装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    private String requestId;
    // 客户端只知道接口名，在服务端中用接口名指向实现类
    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

}
