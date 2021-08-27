package com.bjpowernode.crm.commons.utils;

/**
 * 2021/7/31 0031
 */
public class testMD5 {

    public static void main(String[] args) {
        System.out.println(MD5Util.getMD5(MD5Util.getMD5("ls")+"tom")); //盐值
    }
}
