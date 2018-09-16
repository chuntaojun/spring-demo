package demo.annotation;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tensor
 */
@Data
public class ScanPackage {

    private String packageName;
    private Set<Class> classes;

    /**
     * 构造函数，传入要扫描的包名
     * @param packageName
     */
    public ScanPackage(String packageName) {
        this.packageName = packageName;
        this.classes = new HashSet<>();
        sacn();
        System.out.println("打印制定包下类扫描的结果\n" + classes.toString());
    }

    /**
     * 扫描指定包下的所有class文件
     */
    protected void sacn() {
        String filePath = packageName.replace('.', '/');
        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader()
                    .getResources(filePath);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                if ("file".equals(url.getProtocol())) {
                    fileScan(packageName, URLDecoder.decode(url.getFile(), "UTF-8"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 扫描所有class文件，并将扫描到的class文件装载进Set集合
     * @param packageName
     * @param filePath
     */
    protected void fileScan(String packageName, String filePath) {
        File dir = new File(filePath);
        if (dir.exists()) {
            File[] files = dir.listFiles(pathname -> (true && pathname.isDirectory())
                    || (pathname.getName().endsWith(".class")));
            for (File file : files) {
                if (file.isDirectory()) {
                    fileScan(packageName + "." + file.getName(), file.getPath());
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    try {
                        classes.add(Thread.currentThread().getContextClassLoader()
                                .loadClass(packageName + "." + className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
