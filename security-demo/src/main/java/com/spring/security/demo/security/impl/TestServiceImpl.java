package com.spring.security.demo.security.impl;

import com.spring.security.demo.annotation.Service;
import com.spring.security.demo.security.TestService;

@Service(value = "TestService")
public class TestServiceImpl implements TestService {

    @Override
    public void print() {
        System.out.println("hello world");
    }
}
