package com.demo.thread.test;


/**
 * @author eli
 * @date 2017/8/14 12:04
 */
public class ThreadPool1 {
    public static volatile int numb = 0;

    public static void main(String[] args) throws Exception {

        for (int i = 0; i < 100; i++) {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    for (int i = 0; i < 1000; i++) {
                        numb++;
                    }
                }
            }).start();

        }

        Thread.sleep(2000);
        System.out.println("numb为"+numb);
    }

}
