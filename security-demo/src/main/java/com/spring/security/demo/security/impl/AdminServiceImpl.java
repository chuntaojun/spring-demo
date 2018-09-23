package com.spring.security.demo.security.impl;

import com.spring.security.demo.bean.Authority;
import com.spring.security.demo.security.AdminService;
import com.spring.security.demo.annotation.EnableSecure;
import com.spring.security.demo.annotation.Secure;

/**
 * @author tensor
 */
@EnableSecure
public class AdminServiceImpl implements AdminService {

    @Secure(role = "ADMIN")
    @Override
    public void print(Authority authority) {
        System.out.println(authority.toString());
    }

}
