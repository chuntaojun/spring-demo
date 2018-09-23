package com.spring.security.demo.annotation;

import com.spring.security.demo.Main;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * @author tensor
 */
@Data
public class MethodPermissionFilter {

    private HashMap<String, Method[]> proxyMap;

    public MethodPermissionFilter() {
        String packageName = Main.class.getPackage().getName();
        ScanPackage scanPackage = new ScanPackage(packageName);
        proxyMap = getEnableSecureClass(scanPackage.getClasses());
        System.out.println("打印被@EnableSecure所注解的类以及该类所共用的方法\n" + proxyMap.toString());
    }

    /**
     * 利用Java原生的lambda表达式，构建{@link HashMap<String, Method[]>} String => 方法类名，Method[] => 该类的所有方法
     * @param classes
     * @return
     */
    private HashMap<String, Method[]> getEnableSecureClass(Set<Class> classes) {
        return classes.stream().filter(cls -> cls.getAnnotation(EnableSecure.class) != null)
                .collect(HashMap::new, (m, v) -> m.put(v.getName(), v.getMethods()), HashMap::putAll);
    }

    /**
     * 方法权限验证，如果权限正确返回true，否则返回false
     * @param clsName
     * @param meth
     * @param r
     * @return
     */
    public boolean isAuthority(String clsName, Method meth, String r) {
        Method[] methods = proxyMap.get(clsName);
        for (Method method : methods) {
            if (meth.getName().equals(method.getName())) {
                Secure secure = method.getAnnotation(Secure.class);
                String role = secure.role();
                if (r.equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }

}
