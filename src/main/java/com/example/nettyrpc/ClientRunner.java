package com.example.nettyrpc;

import com.example.nettyrpc.Server.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ClientRunner implements ApplicationRunner {
    @Autowired
    Client client;

    @Override
    public void run(ApplicationArguments args) throws Exception {
      log.info("Client init....");
       // client.init();
    }
}
