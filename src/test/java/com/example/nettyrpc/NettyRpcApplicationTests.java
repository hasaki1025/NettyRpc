package com.example.nettyrpc;

import com.example.nettyrpc.Factory.MessageFactory;
import com.example.nettyrpc.Handler.MessageCodec;
import com.example.nettyrpc.Handler.RequestMessageToRpcMessageCodec;
import com.example.nettyrpc.Handler.ResponseMessageToRpcMessageCodec;
import com.example.nettyrpc.Handler.RpcRequestInboundHandler;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.example.nettyrpc.net.DefaultRpcRequest;
import com.example.nettyrpc.net.Message;

import com.example.nettyrpc.net.RpcRequestMessage;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootTest
class NettyRpcApplicationTests {


    @Autowired
    MessageCodec messageCodec;
    @Autowired
    RpcRequestInboundHandler rpcRequestInboundHandler;
    @Autowired
    RequestMessageToRpcMessageCodec requestCodec;
    @Autowired
    ResponseMessageToRpcMessageCodec responseCodec;
    @Autowired
    MessageFactory messageFactory;



    //魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
    @Test
    void testChannel() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.INFO),
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                messageCodec,
                requestCodec,
                responseCodec,
                rpcRequestInboundHandler
        );

        DefaultRpcRequest request = new DefaultRpcRequest("com.example.nettyrpc.TestInterface",
                "sayHello",
                null, void.class, null);
        channel.writeOutbound(
                new RpcRequestMessage(
                        messageFactory.createRequest(request, SerializableType.JSON, CommandType.request)));


    }
}
