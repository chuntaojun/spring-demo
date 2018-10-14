package com.executor.org;


/**
 * @author tensor
 */
public class Executor {

    /**
     * 主函数，用于演示
     * @param args
     */
    public static void main(String[] args) {
        ExpireTimeMap expireTimeMap = new ExpireTimeMap();
        expireTimeMap.put("1", "1");
        expireTimeMap.put("2", "2", "2000s");
        System.out.println(expireTimeMap.toString());
    }
}