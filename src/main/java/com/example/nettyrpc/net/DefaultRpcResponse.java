package com.example.nettyrpc.net;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
public class DefaultRpcResponse implements RpcResponse{
    private final Object value;
    private final Exception exceptionValue;

}
