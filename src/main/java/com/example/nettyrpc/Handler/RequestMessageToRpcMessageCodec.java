package com.example.nettyrpc.Handler;

import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.net.DefaultMessage;
import com.example.nettyrpc.net.RpcRequestMessage;
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
public class RequestMessageToRpcMessageCodec extends MessageToMessageDecoder<DefaultMessage> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DefaultMessage defaultMessage, List<Object> list) throws Exception {
        if (CommandType.request.equals(defaultMessage.getCommandType()))
        {
            log.debug("DefaultMessage To RpcRequestMessage :message {}",defaultMessage);
            list.add(new RpcRequestMessage(defaultMessage));
        }
        else
        {
            list.add(defaultMessage);
        }
    }
}
