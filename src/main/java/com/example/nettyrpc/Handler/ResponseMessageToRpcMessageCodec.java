package com.example.nettyrpc.Handler;

import com.example.nettyrpc.net.DefaultMessage;
import com.example.nettyrpc.net.RpcRequestMessage;
import com.example.nettyrpc.net.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
@Slf4j
public class ResponseMessageToRpcMessageCodec extends MessageToMessageCodec<DefaultMessage, RpcResponseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, RpcResponseMessage msg, List<Object> out) throws Exception {
        log.debug("RpcResponse To Message :message {}",msg);
        out.add(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        log.debug("Message To  RpcResponse :message {}",msg);
        out.add(new RpcRequestMessage(msg));
    }
}
