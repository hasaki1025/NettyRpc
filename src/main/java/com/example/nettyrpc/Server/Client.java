package com.example.nettyrpc.Server;

import com.example.nettyrpc.Exception.ResponseTimeOutException;
import com.example.nettyrpc.Factory.MessageFactory;
import com.example.nettyrpc.Handler.MessageCodec;
import com.example.nettyrpc.Handler.RpcResponseInboundHandler;
import com.example.nettyrpc.enums.CommandType;
import com.example.nettyrpc.enums.SerializableType;
import com.example.nettyrpc.net.Message;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
@Slf4j
@Getter
@ToString
public class Client implements Closeable {

    @Value("${Rpc.register.Host}")
    String host;

    @Value("${Rpc.register.port}")
    int port;

    private NioSocketChannel channel;

    private NioEventLoopGroup group;
    private DefaultEventLoopGroup defaultEventLoopGroup;
    private final Map<Integer, ScheduledFuture<?>> futureMap=new ConcurrentHashMap<>();


    private final Map<Integer, Long> timeOutMap=new ConcurrentHashMap<>();
    @Autowired
    MessageFactory messageFactory;
    @Autowired
    RpcResponseInboundHandler rpcResponseInboundHandler;


    @Value("${Rpc.request.maxResultTime}")
    long timeout;

    @Value("${Rpc.request.resultTimeGap}")
    long gap;

    @Autowired
    MessageCodec messageCodec;



    public void init()
    {
        group=new NioEventLoopGroup();
        defaultEventLoopGroup=new DefaultEventLoopGroup();
        ChannelFuture connect = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        log.info("connnect successfully: Host [{}], port [{}]",host,port);
                    }
                }).connect(host, port)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        channel= (NioSocketChannel) future.channel();
                    }
                });

    }


    @Override
    public void close() throws IOException {
        channel.close().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                group.shutdownGracefully();
                defaultEventLoopGroup.shutdownGracefully();
            }
        });
    }





    //魔数（4）-版本号（2）-序列化算法（1）-指令类型（1）-请求序号(4)-正文长度(4)-消息本体
    //不关心返回结果
    public  void sendRequest(Message message)
    {
        channel.writeAndFlush(message);
    }


    public <T> T sendRequest(Message message,Class<T> clazz) throws ExecutionException, InterruptedException {
        sendRequest(message);
        return getResult(message.getSeq());
    }
    // 对于返回结果同步堵塞
    public<T> T sendRequest(Object content, SerializableType serializableType, CommandType commandType,Class<T> clazz) throws ExecutionException, InterruptedException {
        Message message = messageFactory.getMessage(content, serializableType, commandType);
        return sendRequest(message, clazz);
    }

    //发送请求之后对返回结果做一些处理
    public <T> void sendRequest(Message message, Consumer<T> resultConsumer)
    {
        channel.writeAndFlush(message);
        int seq=message.getSeq();
        resultConsumerExec(resultConsumer, seq);
    }

    public<T> void sendRequest(Object content, SerializableType serializableType, CommandType commandType, Consumer<T> resultConsumer)
    {
        Message message = messageFactory.getMessage(content, serializableType, commandType);
        sendRequest(message,resultConsumer);
    }

    private <T> void resultConsumerExec(Consumer<T> resultConsumer, int seq) {
        ScheduledFuture<?> future = defaultEventLoopGroup.scheduleAtFixedRate(() -> {

            ScheduledFuture<?> future1 = futureMap.get(seq);
            Long nowTime = timeOutMap.get(seq);
            if (!messageCodec.stillWaiting(seq))
            {
                Object value = rpcResponseInboundHandler.getValue(seq);
                if (!(value instanceof Exception))
                {
                    T v=(T)value;
                    resultConsumer.accept(v);
                }
                futureMap.remove(seq);
                timeOutMap.remove(seq);
                future1.cancel(true);
            }
            if (nowTime >=timeout)
            {
                futureMap.remove(seq);
                timeOutMap.remove(seq);
                future1.cancel(true);
                throw new ResponseTimeOutException();
            }

            if (!future1.isCancelled())
            {
                timeOutMap.put(seq,timeOutMap.get(seq)+gap);
            }
        }, gap, gap, TimeUnit.MILLISECONDS);
        futureMap.put(seq,future);
        timeOutMap.put(seq,0L);
    }

    private <T> T getResult(int seq) throws ExecutionException, InterruptedException {

        DefaultPromise<T> promise = new DefaultPromise<>(group.next());
        ScheduledFuture<?> future = defaultEventLoopGroup.scheduleAtFixedRate(() -> {

            ScheduledFuture<?> future1 = futureMap.get(seq);
            Long nowTime = timeOutMap.get(seq);
            if (!messageCodec.stillWaiting(seq))
            {
                Object value = rpcResponseInboundHandler.getValue(seq);
                if (!(value instanceof Exception))
                {
                    T v=(T)value;
                    promise.setSuccess(v);
                }
                else {
                    promise.setFailure((Throwable) value);
                }
                futureMap.remove(seq);
                timeOutMap.remove(seq);
                future1.cancel(true);
            }
            if (nowTime >=timeout)
            {
                futureMap.remove(seq);
                timeOutMap.remove(seq);
                future1.cancel(true);
                promise.setFailure(new ResponseTimeOutException());
            }

            if (!future1.isCancelled())
            {
                timeOutMap.put(seq,timeOutMap.get(seq)+gap);
            }
        }, gap, gap, TimeUnit.MILLISECONDS);
        futureMap.put(seq,future);
        timeOutMap.put(seq,0L);
        return promise.get();
    }
}
