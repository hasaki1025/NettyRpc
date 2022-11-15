package com.example.nettyrpc.Handler;

import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.net.DefaultMessage;
import com.example.nettyrpc.net.RpcRequestMessage;
import com.example.nettyrpc.net.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
@ChannelHandler.Sharable
@Component
@Slf4j
public class ResponseMessageToRpcMessageCodec extends MessageToMessageDecoder<DefaultMessage> {


    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultMessage msg, List<Object> out) throws Exception {
        if (CommandType.response.equals(msg.getCommandType()))
        {
            log.debug("Message To  RpcResponse :message {}",msg);
            out.add(new RpcResponseMessage(msg));
        }
        else {
            out.add(msg);
        }

    }
}
