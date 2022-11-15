package com.example.nettyrpc;

import com.example.nettyrpc.Factory.MessageFactory;
import com.example.nettyrpc.Handler.*;
import com.example.nettyrpc.Server.Client;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.example.nettyrpc.net.*;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
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

    @Autowired
    RpcResponseInboundHandler rpcResponseInboundHandler;

    @Autowired
    Client client;





    //魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
    //发起请求测试
    @Test
    void testInBoundRequestChannel() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.INFO),
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                messageCodec,
                requestCodec,
                responseCodec,
                rpcRequestInboundHandler,
                rpcResponseInboundHandler
        );

        DefaultRpcRequest request = new DefaultRpcRequest("com.example.nettyrpc.TestInterface",
                "sayHello",
                null, void.class, null);
        channel.writeOutbound(
                new RpcRequestMessage(
                        messageFactory.createRequest(request, SerializableType.JSON, CommandType.request)));


    }

    //接受响应测试
    @Test
    void testInBoundResponse() {

        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.INFO),
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                messageCodec,
                requestCodec,
                responseCodec,
                rpcRequestInboundHandler,
                rpcResponseInboundHandler
        );
        DefaultRpcResponse response = new DefaultRpcResponse(1,null);
        channel.writeInbound(new RpcResponseMessage(
                messageFactory.createResponse(response, SerializableType.JSON, CommandType.response)));
        System.out.println(rpcResponseInboundHandler.getValue(1, Integer.class));
    }

    //接受请求测试

    @Test
    void testResquestInbound() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.INFO),
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                messageCodec,
                requestCodec,
                responseCodec,
                rpcRequestInboundHandler,
                rpcResponseInboundHandler
        );

        DefaultRpcRequest request = new DefaultRpcRequest("com.example.nettyrpc.TestInterface",
                "sayHello",
                null, void.class, null);
        channel.writeInbound(new RpcRequestMessage(
                messageFactory.createRequest(request, SerializableType.JSON, CommandType.request)));

    }

    //发起响应测试

    @Test
    void testOutBoundResponse() {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.INFO),
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                messageCodec,
                requestCodec,
                responseCodec,
                rpcRequestInboundHandler,
                rpcResponseInboundHandler
        );

        DefaultRpcResponse response = new DefaultRpcResponse(1,null);
        channel.writeOutbound(new RpcResponseMessage(
                messageFactory.createResponse(response, SerializableType.JSON, CommandType.response)));

    }


    public final List<Integer> list=new ArrayList<>();
    @Test
    void testTimeTask() {





    }
}
