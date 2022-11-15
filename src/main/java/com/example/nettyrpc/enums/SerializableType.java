package com.example.nettyrpc.enums;

public enum SerializableType {
    JAVA(0),JSON(1);
    private int value;
    SerializableType(int i) {
        this.value=i;
    }

    public int getValue() {
        return value;
    }

    private final  static SerializableType[] serializableTypes={JAVA,JSON};

    public static SerializableType forInt(int i)
    {
        return serializableTypes[i];
    }
}
