package demo.security.impl;

import demo.annotation.EnableSecure;
import demo.annotation.Secure;
import demo.bean.Authority;
import demo.security.AdminService;

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
