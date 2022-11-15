package com.example.nettyrpc.Handler;

import com.example.nettyrpc.Factory.MessageFactory;
import com.example.nettyrpc.NettyRpcApplication;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.example.nettyrpc.net.Message;
import com.example.nettyrpc.net.RpcRequest;
import com.example.nettyrpc.net.RpcRequestMessage;
import com.example.nettyrpc.net.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Component
@ChannelHandler.Sharable
public class RpcRequestInboundHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {


    @Autowired
    AnnotationConfigApplicationContext context;
    @Autowired
    MessageFactory messageFactory;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage requestMessage) throws Exception {
        if (CommandType.request.equals(requestMessage.getCommandType()))
        {
            RpcRequest request = requestMessage.getRpcRequest();
            Class<?> clazz = Class.forName(request.getInterfaceName());
            Object bean = context.getBean(clazz);
            String methodName = request.getMethodName();
            Method method = clazz.getMethod(methodName, request.getParamType());
            Object result = method.invoke(bean, request.getParamValues());
            RpcResponseMessage message = messageFactory.createResponse(result, SerializableType.JSON, CommandType.response);
            ctx.writeAndFlush(message);
            //TODO 需要设置一个set集合，该集合中存放了正在等待结果的连接的请求序号
        }

    }
}
