package com.example.nettyrpc;

import org.springframework.stereotype.Component;

@Component
public class TestInterfaceImpl implements TestInterface {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
