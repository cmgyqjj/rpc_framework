package com.qjj.server.core;

import com.qjj.codec.RpcRequest;
import com.qjj.codec.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author:qjj
 * @create: 2023-07-07 15:50
 * @Description: rpc的Netty服务处理器
 */

public class NettyRpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private Map<String, Object> handlerMap;

    public NettyRpcServerHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
    *@Param:
     * ChannelHandlerContext ctx
     * RpcRequest rpcRequest
    *@return: void
    *@Author: qjj
    *@date:
     * 处理请求，并且返回结果的逻辑
    */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
//        为RPC添加requestId，用于标识请求
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());
//        处理请求，并且返回请求结果
        try{
            Object result=handle(rpcRequest);
            rpcResponse.setResult(result);
        }catch (Throwable throwable){
            rpcResponse.setError(throwable.toString());
        }
//        写入RPC响应对象并且关闭连接
        ctx.writeAndFlush(rpcResponse).addListener(ChannelFutureListener.CLOSE);
    }

    /**
    *@Param: RpcRequest rpcRequest
    *@return: Object
    *@Author: qjj
    *@date:
     * 具体的执行流程，先读取到请求需要的className，然后通过className从本地注册中找到service，然后再通过反射调用service的方法
     * 并且带上参数，最后返回结果
    */
    private Object handle(RpcRequest rpcRequest) throws InvocationTargetException {
//        从RPC请求对象中获取请求的类名
        String className = rpcRequest.getClassName();
//        从本地注册中心获取服务对象
        Object serviceBean = handlerMap.get(className);
//        获取服务对象的类
        Class<?> serviceClass = serviceBean.getClass();
//        获取请求的方法名，参数类型，参数
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();
//        通过FastClass来调用方法
//        FastClass不使用反射类（Constructor或Method）来调用委托类方法，而是动态生成一个新的类（继承FastClass），
//        向类中写入委托类实例直接调用方法的语句，用模板方式解决Java语法不支持问题，同时改善Java反射性能。
//        动态类为委托类方法调用语句建立索引，使用者根据方法签名（方法名+参数类型）得到索引值，再通过索引值进入相应的方法调用语句，得到调用结果。
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        Object result = serviceFastMethod.invoke(serviceBean, parameters);
        return result;
    }
}
