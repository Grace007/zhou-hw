package com.demo.mapreduce.login_mysql;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LoginMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	/**
	 * 获取<2017-10-01,1>格式
	 * @param key
	 * @param value
	 * @param context
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	protected void map(LongWritable key, Text value,Context context) throws IOException, InterruptedException {
		String text = value.toString();
		String[] login_datas = text.split("\t");
		String login_time = login_datas[1].replace("\"","").split(" ")[0];
		context.write(new Text(login_time), new IntWritable(1));
	}
}