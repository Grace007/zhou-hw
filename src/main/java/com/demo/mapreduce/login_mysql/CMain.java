package com.demo.mapreduce.login_mysql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;


/**
 * MR程序入口
 */
public class CMain {
	
	// 每天登陆人数
	public static void login_day_1() throws Exception{
		String[] args_login = {
			"hdfs://192.168.109.200:9000/test_data/Loginlog_big.log"
		};
		int login_type = ToolRunner.run(new Configuration(), new LoginMain(), args_login);
		System.out.println("-----> 每天登陆人数 分析完成");
	}


	public static void main(String[] args) throws Exception {
		login_day_1();// 每天登陆人数	
	}

}
