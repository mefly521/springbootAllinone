package com.demo.controller;

import java.nio.IntBuffer;

/**
 * 测试buffer
 *
 * @author mifei
 * @create 2020-06-15 16:49
 **/
public class Test {
    public static void main(String[] args) {
        IntBuffer ib = IntBuffer.allocate(10);
        ib.put(11);
        ib.put(12);
        ib.put(13);
        System.out.println(ib);
        System.out.println(ib.get(0));
        ib.put(0, 22);
        System.out.println(ib);
        System.out.println(ib.get(2));
        ib.flip();
        System.out.println(ib);
        for (int i = 0; i < ib.limit(); i++) {
            System.out.println("==" + ib.get());
        }
        System.out.println("可读数据" + ib.remaining());
    }
}
