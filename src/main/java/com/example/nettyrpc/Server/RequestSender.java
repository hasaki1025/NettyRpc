package com.example.nettyrpc.Server;

import com.example.nettyrpc.Factory.MessageFactory;
import com.example.nettyrpc.Handler.RpcResponseInboundHandler;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.example.nettyrpc.net.Message;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static java.lang.Thread.sleep;

@Component
public class RequestSender {

    @Autowired
    Client client;
    @Autowired
    MessageFactory messageFactory;
    @Autowired
    RpcResponseInboundHandler rpcResponseInboundHandler;

    @Value("${Rpc.request.maxResultTime}")
    long timeout;

    @Value("${Rpc.request.resultTimeGap}")
    long gap;




    //魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
    //返回SEQ
    public  void sendRequest(Message message)
    {
        client.getChannel().writeAndFlush(message);
    }
    //TODO 对于需要远程调用的返回结果（同步堵塞？）
    public int sendRequest(Object content, SerializableType serializableType, CommandType commandType)
    {
        Message message = messageFactory.getMessage(content, serializableType, commandType);
        sendRequest(message);
        return message.getSeq();
    }

    //发送请求之后对返回结果做一些处理
    public <T> void sendRequest(Message message, Consumer<T> resultConsumer)
    {
        client.getChannel().writeAndFlush(message);
        EventLoop eventLoop = client.getGroup().next();
        eventLoop.submit(() -> {
            int i = message.getSeq();
            int sleepTime = 0;
            T value = null;
            while (true) {
                try {
                    sleep(gap);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sleepTime += gap;
                if (rpcResponseInboundHandler.getValue(i) != null) {
                    value = (T) rpcResponseInboundHandler.getValue(i);
                    break;
                }
                if (sleepTime > timeout) {
                    break;
                }
            }
            if (value != null && resultConsumer != null) {
                resultConsumer.accept(value);
            }
        });
    }

    public<T> int sendRequest(Object content, SerializableType serializableType, CommandType commandType, Consumer<T> resultConsumer)
    {
        Message message = messageFactory.getMessage(content, serializableType, commandType);
        sendRequest(message);
        return message.getSeq();
    }



}
