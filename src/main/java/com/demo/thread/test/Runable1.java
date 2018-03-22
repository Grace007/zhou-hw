package com.demo.thread.test;

/**
 * @author eli
 * @date 2017/8/14 10:19
 */
public class Runable1 implements Runnable{



    public static void main(String[] args) {
        Runable1 runnable1 = new Runable1();
        Thread thread = new Thread(runnable1,"thread1");
        thread.start();
        thread.run();
    }

    @Override
    public void run() {
        for (int i=0;i<20;i++)
        {

            System.out.println("i = " + i);
            System.out.println(Thread.currentThread().getName());
        }

    }
}
