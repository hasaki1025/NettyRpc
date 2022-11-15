package com.example.nettyrpc.net;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
public class DefaultRpcResponse implements RpcResponse{
    private  Object value;
    private  Exception exceptionValue;

    public DefaultRpcResponse(Object value, Exception exceptionValue) {
        this.value = value;
        this.exceptionValue = exceptionValue;
    }

    public DefaultRpcResponse() {
    }
}
