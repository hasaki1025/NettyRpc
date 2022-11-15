package com.example.nettyrpc.net;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

/**
 *
 * 魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
 *  +-------------------------------------------------+
 *          |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 01 00 02 06 01 00 01 00 00 00 00 01 00 00 00 86 |................|
 * |00000010| 7b 22 69 6e 74 65 72 66 61 63 65 4e 61 6d 65 22 |{"interfaceName"|
 * |00000020| 3a 22 63 6f 6d 2e 65 78 61 6d 70 6c 65 2e 6e 65 |:"com.example.ne|
 * |00000030| 74 74 79 72 70 63 2e 54 65 73 74 49 6e 74 65 72 |ttyrpc.TestInter|
 * |00000040| 66 61 63 65 22 2c 22 6d 65 74 68 6f 64 4e 61 6d |face","methodNam|
 * |00000050| 65 22 3a 22 73 61 79 48 65 6c 6c 6f 22 2c 22 70 |e":"sayHello","p|
 * |00000060| 61 72 61 6d 54 79 70 65 22 3a 6e 75 6c 6c 2c 22 |aramType":null,"|
 * |00000070| 72 65 74 75 72 6e 54 79 70 65 22 3a 22 76 6f 69 |returnType":"voi|
 * |00000080| 64 22 2c 22 70 61 72 61 6d 56 61 6c 75 65 73 22 |d","paramValues"|
 * |00000090| 3a 6e 75 6c 6c 7d                               |:null}          |
 * +--------+-------------------------------------------------+----------------+
 */

@Data
public class DefaultRpcRequest implements RpcRequest{
    private  String interfaceName;
    private  String methodName;
    private  Class[] paramType;
    private  Class<?> returnType;
    private  Object[] paramValues;

    public DefaultRpcRequest(String interfaceName, String methodName, Class[] paramType, Class<?> returnType, Object[] paramValues) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.paramType = paramType;
        this.returnType = returnType;
        this.paramValues = paramValues;
    }

    public DefaultRpcRequest() {
    }
}

