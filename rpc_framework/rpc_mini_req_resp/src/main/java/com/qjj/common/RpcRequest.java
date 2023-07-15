package com.qjj.common;

import lombok.*;

/**
 * @author:qjj
 * @create: 2023-07-15 19:53
 * @Description: rpc请求统一包装类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest {

    private String requestId;

    private String className;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

}
