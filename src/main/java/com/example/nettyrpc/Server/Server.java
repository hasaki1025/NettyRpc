package com.example.nettyrpc.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class Server {
    @Value("${Rpc.register.port}")
    int port;


   private final NioEventLoopGroup bossGroup=new NioEventLoopGroup(1);
   private final NioEventLoopGroup childGroup=new NioEventLoopGroup();

   private final DefaultEventLoopGroup defaultEventLoopGroup=new DefaultEventLoopGroup();

   private final Set<NioSocketChannel> channels=new HashSet<>();

    void init()
    {

        new ServerBootstrap()
                .group(bossGroup,childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                    }
                }).bind(port).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        NioSocketChannel channel = (NioSocketChannel) future.channel();
                        channels.add((NioSocketChannel) channel);
                    }
                });

    }
}
