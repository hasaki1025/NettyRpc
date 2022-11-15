package com.example.nettyrpc.net;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
public class DefaultRpcRequest implements RpcRequest{
    private final String interfaceName;
    private final String methodName;
    private final Class[] paramType;
    private final Class<?> returnType;
    private final Object[] paramValues;

}
