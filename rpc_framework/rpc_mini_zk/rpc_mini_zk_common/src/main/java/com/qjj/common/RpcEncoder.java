package com.qjj.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author:qjj
 * @create: 2023-07-17 16:21
 * @Description: Rpc编码器
 */

public class RpcEncoder extends MessageToByteEncoder<Object> {

    private Class<?> genericClass;

    public RpcEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        if (genericClass.isInstance(in)) {
            byte[] data = RpcSerialize.serialize(in);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
