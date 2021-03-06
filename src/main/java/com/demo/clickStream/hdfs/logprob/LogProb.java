package com.demo.clickStream.hdfs.logprob;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author
 * 
 */
public class LogProb {

	public static void main(String[] args) throws Exception {
		Runnable runnable = new Runnable() {
			public void run() {
				// task to run goes here
				System.out.println("Hello !!");
			}
		};
		
		Runnable runnable2 = new Runnable() {
			public void run() {
				// task to run goes here
				System.out.println("Hello222 !!");
			}
		};
		
		
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable2,8,1,TimeUnit.SECONDS);
		// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
		service.scheduleAtFixedRate(runnable, 5, 2, TimeUnit.SECONDS);
	}

}
