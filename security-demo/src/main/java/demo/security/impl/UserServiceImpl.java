package demo.security.impl;

import demo.annotation.EnableSecure;
import demo.annotation.Secure;
import demo.bean.Authority;
import demo.security.UserService;

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
