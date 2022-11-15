package com.example.nettyrpc.net;

import com.example.nettyrpc.Util.MessageUtil;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RpcRequestMessage extends DefaultMessage{

    private final RpcRequest request;


    public RpcRequestMessage(String magicNumber, int version, SerializableType serializableType, CommandType commandType, int size, String content, int seq) {
        super(magicNumber, version, serializableType, commandType, size, content, seq);

        if (SerializableType.JSON.equals(serializableType))
        {
            try {
                request=new ObjectMapper().readValue(content,DefaultRpcRequest.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            request=null;
        }
    }

    public RpcRequestMessage(Message message) {
        this(message.getMagicNumber(),message.getVersion(),message.getSerializableType(),message.getCommandType(), message.size(), message.content(),message.getSeq());
    }

    public RpcRequest getRpcRequest()
    {
        return request;
    }
}
