package com.spring.security.demo.cglib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author tensor
 */
public class CglibMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (obj.getClass().equals(method.getDeclaringClass())) {
            return method.invoke(obj, args);
        }
        String methodName = method.getName();
        //打印日志
        System.out.println("[before] The method " + methodName + " begins with " + (args!=null ? Arrays.asList(args) : "[]"));
        Object result = proxy.invokeSuper(obj, args);
        System.out.println(String.format("after method:%s execute", method.getName()));
        System.out.println("[after] The method ends with " + result);
        return result;
    }
}
