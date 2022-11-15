package com.example.nettyrpc.Handler;

import com.example.nettyrpc.net.DefaultMessage;
import com.example.nettyrpc.net.RpcRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
@Slf4j
public class RequestMessageToRpcMessageCodec extends MessageToMessageCodec<DefaultMessage, RpcRequestMessage> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcRequestMessage rpcRequestMessage, List<Object> list) throws Exception {
        log.debug("RpcRequestMessage To DefaultMessage :message {}",rpcRequestMessage);
        list.add(rpcRequestMessage);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DefaultMessage defaultMessage, List<Object> list) throws Exception {
        log.debug("DefaultMessage To RpcRequestMessage :message {}",defaultMessage);
        list.add(new RpcRequestMessage(defaultMessage));
    }
}
