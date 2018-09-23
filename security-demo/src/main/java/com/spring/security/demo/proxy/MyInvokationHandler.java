package com.spring.security.demo.proxy;

import com.spring.security.demo.annotation.MethodPermissionFilter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvokationHandler<E> implements InvocationHandler {

    private E target;
    private String role;

    private MethodPermissionFilter methodPermissionFilter;

    public MyInvokationHandler(E target, String role) {
        this.target = target;
        this.role = role;
        this.methodPermissionFilter = new MethodPermissionFilter();
    }

    /**
     * 方法被代理后最终通过 invoke 函数实现函数的调用
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methodPermissionFilter.isAuthority(target.getClass().getName(), method, role)) {
            return method.invoke(target, args);
        }
        throw new RuntimeException("You have no permission use this function");
    }
}
