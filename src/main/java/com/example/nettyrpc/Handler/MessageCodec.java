package com.example.nettyrpc.Handler;

import com.example.nettyrpc.Exception.IncorrectMagicNumberException;
import com.example.nettyrpc.net.Message;
import com.example.nettyrpc.Util.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToMessageCodec;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
public class MessageCodec extends MessageToMessageCodec<ByteBuf,Message> {




    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf byteBuf=ctx.alloc().buffer();
        MessageUtil.messageToByteBuf(msg,byteBuf);
        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        Message message = null;
        try {
            message = MessageUtil.byteToMessage(byteBuf);
            list.add(message);
        } catch (IncorrectMagicNumberException e) {
            throw new RuntimeException(e);
        }

    }
}
