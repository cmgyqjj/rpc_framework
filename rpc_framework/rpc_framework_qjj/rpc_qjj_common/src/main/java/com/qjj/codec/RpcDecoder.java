package com.qjj.codec;

import com.qjj.utils.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * RPC Decoder
 *
 * @author luxiaoxun
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    public final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }
        in.markReaderIndex();
        int dataLength = in.readInt();
        if (dataLength < 0) {
            ctx.close();
        }
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
        }
        byte[] data = new byte[dataLength];
        in.readBytes(data);

        Object obj = SerializationUtil.deserialize(data, genericClass);
        out.add(obj);
    }

    public static class RpcEncoder extends MessageToByteEncoder<Object> {

        private Class<?> genericClass;

        public RpcEncoder(Class<?> genericClass) {
            this.genericClass = genericClass;
        }

        @Override
        public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
            if (genericClass.isInstance(in)) {
                byte[] data = SerializationUtil.serialize(in);
                out.writeInt(data.length);
                out.writeBytes(data);
            }
        }
    }

}
