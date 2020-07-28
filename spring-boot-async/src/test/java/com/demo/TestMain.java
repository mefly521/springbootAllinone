package com.demo;

import java.math.BigInteger;

/**
 * 二进制比较及运算
 * @author mifei
 * @create 2020-01-06 13:13
 **/
public class TestMain {

    public static void main(String[] args) {

        BigInteger b1 = new BigInteger("00000", 2);    //转换为BigInteger类型
        int i1 = b1.intValue();
        System.out.println(b1.intValue());
        BigInteger b2 = new BigInteger("11101", 2);    //转换为BigInteger类型
        int i2 = b2.intValue();
        String res = Integer.toBinaryString(i1|i2);
        System.out.println(res);
//        String str = "1";
//
//        // 转换为二进制字符串
//        String binaryStr = BinaryStringConverteUtil.toBinaryString(str);
//
//        // 获取1的数量
//        int cnt = BinaryStringConverteUtil.countBitOne(binaryStr);
//
//        // 反向转换
//        String newStr = BinaryStringConverteUtil.toString(binaryStr);
//
//        System.out.println("源字符串:" + str);
//        System.out.println("二进制字符串:" + binaryStr);
//        System.out.println("1位数量:" + BinaryStringConverteUtil.countBitOne(binaryStr));
//        System.out.println("新字符串:" + newStr);
    }
}
