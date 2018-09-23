package com.spring.security.demo.bean;

import java.util.HashMap;

/**
 * @author tensor
 */
public class Roles {

    private HashMap<String, Authority> authority;

    public Roles() {
        authority = new HashMap<>();
    }

    public void add(Authority authority) {
        this.authority.put(authority.getID(), authority);
    }

}
