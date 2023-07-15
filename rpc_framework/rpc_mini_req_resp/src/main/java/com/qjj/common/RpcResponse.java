package com.qjj.common;

import lombok.*;

import java.io.Serializable;

/**
 * @author:qjj
 * @create: 2023-07-15 19:53
 * @Description: rpc返回统一包装类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RpcResponse implements Serializable {

    private Integer code;

    private String error;

    private Object result;

    public static RpcResponse success(Object data) {
        return RpcResponse.builder().code(200).result(data).build();
    }
    public static RpcResponse fail() {
        return RpcResponse.builder().code(500).error("服务器发生错误").build();
    }
}
