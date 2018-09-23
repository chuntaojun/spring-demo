package com.spring.security.demo.cglib;


import net.sf.cglib.proxy.Enhancer;

/**
 * @author tensor
 */
public class CglibProxy {

    private static final Enhancer enhancer = new Enhancer();

    public <T> T getProxy(Object target) {
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibMethodInterceptor());
        return (T) enhancer.create();
    }

}
