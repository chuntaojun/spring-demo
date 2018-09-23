package com.spring.security.demo.annotation;

import com.spring.security.demo.Main;
import com.spring.security.demo.cglib.CglibProxy;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

/**
 * @author liaochuntao
 */
@Data
public class ServiceFilter {

    private HashMap<Class, Object> serviceMap;
    private HashMap<String, Object> autowiredMap;
    private CglibProxy cglibProxy;

    public ServiceFilter() {
        cglibProxy = new CglibProxy();
        String packageName = Main.class.getPackage().getName();
        ScanPackage scanPackage = new ScanPackage(packageName);
        serviceMap = getServiceClasses(scanPackage.getClasses());
        setServiceFields();
    }

    public HashMap getServiceClasses(Set<Class> classes) {
        return classes.stream().filter(c -> c.isAnnotationPresent(Service.class))
                .collect(HashMap::new, (m, v) -> {
                    try {
                        if (v.getInterfaces().length > 0) {
                            m.put(v.getInterfaces()[0], cglibProxy.getProxy(v.newInstance()));
                        } else {
                            m.put(v, v.newInstance());
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }, HashMap::putAll);
    }

    public void setServiceFields() {
        serviceMap.keySet().forEach(cls -> {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Autowired.class)) {
                    try {
                        field.set(serviceMap.get(cls), serviceMap.get(field.getType()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
