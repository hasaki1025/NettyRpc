package com.example.nettyrpc.Factory;

import com.example.nettyrpc.Util.MessageUtil;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.example.nettyrpc.net.DefaultMessage;
import com.example.nettyrpc.net.Message;
import com.example.nettyrpc.net.RpcRequestMessage;
import com.example.nettyrpc.net.RpcResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandler;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.nettyrpc.Util.MessageUtil.*;
@Component
public class MessageFactory {
    //魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
    public  Message getMessage(Object content, SerializableType serializableType, CommandType commandType)
    {

        if (serializableType.equals(SerializableType.JSON))
        {
            try {
                String value = new ObjectMapper().writeValueAsString(content);
                return new DefaultMessage(MAGIC, VERSION, serializableType, commandType, value.getBytes(StandardCharsets.UTF_8).length, value,MessageUtil.getNextSeq());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public RpcResponseMessage createResponse(Object content, SerializableType serializableType, CommandType commandType)
    {
        DefaultMessage message = (DefaultMessage) getMessage(content, serializableType, commandType);
        return new RpcResponseMessage(message);
    }


    public RpcRequestMessage createRequest(Object content, SerializableType serializableType, CommandType commandType)
    {
        DefaultMessage message = (DefaultMessage) getMessage(content, serializableType, commandType);
        return new RpcRequestMessage(message);
    }
}
