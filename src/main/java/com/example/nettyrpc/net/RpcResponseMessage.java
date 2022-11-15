package com.example.nettyrpc.net;

import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RpcResponseMessage extends DefaultMessage{
    private final RpcResponse response;//TODO 需要初始化response

    public RpcResponseMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, String content, int seq) {
        super(magicNumber, version, serializableType, commandType, size, content, seq);
        if (SerializableType.JSON.equals(serializableType))
        {
            try {
                response=new ObjectMapper().readValue(content,DefaultRpcResponse.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            response=null;
        }

    }

    public RpcResponseMessage(Message message) {
        this(message.getMagicNumber(),message.getVersion(),message.getSerializableType(),message.getCommandType(), message.size(), message.content(),message.getSeq());
    }

    public RpcResponse getRpcResponse() {
        return response;
    }
}
