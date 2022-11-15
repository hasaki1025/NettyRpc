package com.example.nettyrpc.Handler;

import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.net.RpcResponse;
import com.example.nettyrpc.net.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@ChannelHandler.Sharable
@Slf4j
public class RpcResponseInboundHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {
    private final Map<Integer, Object> returnValue = new HashMap<>();



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        if(CommandType.response.equals(msg.getCommandType()))
        {
            log.debug("get Response {}",msg);
            RpcResponse response = msg.getRpcResponse();
            if (response.getExceptionValue() == null) {
                returnValue.put(msg.getSeq(), response.getValue());
                log.info("Method Exec Successfully...");
            } else {
                Exception value = response.getExceptionValue();
                value.printStackTrace();
            }
        }

    }

    public <T> T getValue(int seq, Class<T> clazz)
    {
        return (T) returnValue.get(seq);
    }

    public  Object getValue(int seq)
    {
        return returnValue.get(seq);
    }
}
