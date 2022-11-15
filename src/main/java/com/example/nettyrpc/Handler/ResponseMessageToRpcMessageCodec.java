package com.example.nettyrpc.Handler;

import com.example.nettyrpc.net.DefaultMessage;
import com.example.nettyrpc.net.RpcRequestMessage;
import com.example.nettyrpc.net.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
public class ResponseMessageToRpcMessageCodec extends MessageToMessageCodec<DefaultMessage, RpcResponseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponseMessage msg, List<Object> out) throws Exception {
        out.add(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        out.add(new RpcRequestMessage(msg));
    }
}
