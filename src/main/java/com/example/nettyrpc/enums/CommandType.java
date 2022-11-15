package com.example.nettyrpc.enums;

public enum CommandType {

    request(0),response(1);
    private int value;

    CommandType(int i) {
        this.value=i;
    }

    public int getValue() {
        return value;
    }

    private final static CommandType[] commandTypes={request,response};

    public static CommandType forInt(int i)
    {
        return commandTypes[i];
    }
}
