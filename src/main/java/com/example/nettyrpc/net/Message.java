package com.example.nettyrpc.net;

import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;

public interface Message {
    String getMagicNumber();
    int getVersion();
    SerializableType getSerializableType();
    CommandType getCommandType();
    int size();
    String content();

    int getSeq();
}
