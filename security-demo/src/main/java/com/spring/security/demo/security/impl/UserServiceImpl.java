package com.spring.security.demo.security.impl;

import com.spring.security.demo.annotation.Service;
import com.spring.security.demo.bean.Authority;
import com.spring.security.demo.annotation.EnableSecure;
import com.spring.security.demo.annotation.Secure;
import com.spring.security.demo.security.UserService;

/**
 * @author tensor
 */
@EnableSecure
public class UserServiceImpl implements UserService {

    @Secure(role = "USER")
    @Override
    public void print(Authority authority) {
        System.out.println(authority.toString());
    }

}
