package com.demo.thread.test;

/**
 * @author eli
 * @date 2017/8/14 10:16
 */
public class Thread1 extends Thread {
    @Override
    public void run() {
        for (int i=0;i<20;i++)
        {
            System.out.println("i =真 " + i);
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread1();
        thread.start();
    }
}
