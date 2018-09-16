package demo.proxy;

import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author tensor
 */
@Data
public class JDKFactory {

    private InvocationHandler handler;
    private proxyInterface proxy;

    /**
     * JDK代理实现，统一拦截被代理的方法
     * @param proxy
     * @param role
     */
    public JDKFactory(proxyInterface proxy, String role) {
        this.proxy = proxy;
        this.handler = new MyInvokationHandler<>(proxy, role);
        this.proxy = (proxyInterface) Proxy.newProxyInstance(proxyInterface.class.getClassLoader(),
                new Class[]{proxyInterface.class}, handler);
    }
}
