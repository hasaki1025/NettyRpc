package com.example.nettyrpc.Server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;

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



    public void init()
    {
        group=new NioEventLoopGroup();
        ChannelFuture connect = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        log.info("connnect successfully: Host [{}], port [{}]",host,port);
                    }
                }).connect(host, port).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        channel= (NioSocketChannel) future.channel();
                    }
                });
    }


    @Override
    public void close() throws IOException {
        channel.close();
        group.shutdownGracefully();
    }


}
