package com.example.nettyrpc.Handler;

import com.example.nettyrpc.Factory.MessageFactory;
import com.example.nettyrpc.net.Message;
import com.example.nettyrpc.net.RpcRequestMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
public class RpcRequestEncoder extends MessageToMessageEncoder<Message> {
    @Autowired
    MessageFactory messageFactory;

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        out.add(new RpcRequestMessage(msg));
    }
}
