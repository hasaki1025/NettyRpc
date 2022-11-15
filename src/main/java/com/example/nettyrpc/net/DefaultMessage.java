package com.example.nettyrpc.net;

import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import lombok.Data;

@Data
public class DefaultMessage implements Message {



    private final String magicNumber;

    private final int version;

    private final SerializableType serializableType;


    private final CommandType commandType;

    private final int size;

    private final String content;
    private final int seq;


    @Override
    public int size() {
        return size;
    }

    @Override
    public String content() {
        return content;
    }


}
