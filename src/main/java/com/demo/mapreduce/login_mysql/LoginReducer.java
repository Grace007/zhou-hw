package com.demo.mapreduce.login_mysql;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class LoginReducer extends Reducer<Text, IntWritable, LoginWritable, LoginWritable> {
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
		// values只有一个值，因为key没有相同的
		int sum = 0;
		for(IntWritable val:values){
			sum += val.get();
		}
		String[] time = key.toString().split("-");
		context.write(new LoginWritable(time[0]+"-"+time[1],time[2], sum), null);  
	}
}
