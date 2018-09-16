package com.hotswap.org;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author tensor
 */
public class Main {

    Class aClass;

    private void init() {
        new Thread() {
            long lastModify = 0;
            @Override
            public void run() {
                while (true) {
                    File f = new File("/media/tensor/resource/code/IdeaProjects/spring-demo/hot-swap/out/production/classes/com/hotswap/org/Test.class");
                    if (lastModify != f.lastModified()) {
                        lastModify = f.lastModified();
                        MyClassLoader myClassLoader = new MyClassLoader(this.getContextClassLoader());
                        myClassLoader.setFile(f);
                        try {
                            Class<Test> clazz = (Class<Test>) myClassLoader.findClass("com.hotswap.org.Test");
                            aClass = clazz;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) throws InterruptedException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        Main m = new Main();
        m.init();
        while (true) {
            if (m.aClass != null) {
                Method method = m.aClass.getMethod("log", null);
                Object o = m.aClass.getConstructor(new Class[]{}).newInstance(new Object[]{});
                method.invoke(o, null);
            }
            Thread.sleep(5 * 1000);
        }
    }

}
