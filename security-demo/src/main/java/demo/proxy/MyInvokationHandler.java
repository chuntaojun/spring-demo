package demo.proxy;

import demo.annotation.MethodPermissionFilter;

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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (methodPermissionFilter.isAuthority(target.getClass().getName(), method, role)) {
            return method.invoke(target, args);
        }
        throw new RuntimeException("You have no permission use this function");
    }
}
