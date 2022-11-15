package com.example.nettyrpc.Handler;

import com.example.nettyrpc.net.DefaultMessage;
import com.example.nettyrpc.net.RpcRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
public class RequestMessageToRpcMessageCodec extends MessageToMessageCodec<DefaultMessage, RpcRequestMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcRequestMessage rpcRequestMessage, List<Object> list) throws Exception {
        list.add(rpcRequestMessage);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DefaultMessage defaultMessage, List<Object> list) throws Exception {
        list.add(new RpcRequestMessage(defaultMessage));
    }
}
