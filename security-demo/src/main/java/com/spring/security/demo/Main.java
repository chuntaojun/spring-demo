package com.spring.security.demo;

import com.spring.security.demo.annotation.Autowired;
import com.spring.security.demo.annotation.Service;
import com.spring.security.demo.annotation.ServiceFilter;
import com.spring.security.demo.bean.Authority;
import com.spring.security.demo.cglib.CglibProxy;
import com.spring.security.demo.proxy.JDKFactory;
import com.spring.security.demo.proxy.proxyInterface;
import com.spring.security.demo.security.TestService;
import com.spring.security.demo.security.UserService;
import com.spring.security.demo.security.impl.UserServiceImpl;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * @author tensor
 */
@Service(value = "Main")
public class Main {

    @Autowired
    private TestService testService;

    public TestService getTestService() {
        return testService;
    }

    public void setTestService(TestService testService) {
        this.testService = testService;
    }

    public static void main(String[] args) {
        ServiceFilter serviceFilter = new ServiceFilter();
        Main main = (Main) serviceFilter.getServiceMap().get(Main.class);
        main.testService.print();
    }

}
