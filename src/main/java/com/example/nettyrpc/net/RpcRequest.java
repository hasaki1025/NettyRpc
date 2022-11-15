package com.example.nettyrpc.net;

public interface RpcRequest  {

    String getInterfaceName();

    String getMethodName();

    Class[] getParamType();

    Class<?> getReturnType();

    Object[] getParamValues();

}
