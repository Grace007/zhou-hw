package com.demo.mapreduce.login_mysql;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;

/**
 * 需求,获取log中每日的登录人数,将结果写入mysql表中
 */
public class LoginMain extends Configured implements Tool{
	@Override
	public int run(String[] arg0) throws Exception {
		// 读取配置文件
		Configuration conf = new Configuration();
		conf.set("fs.defultFS", "hdfs://192.168.109.200:9000");
//		conf.set("hadoop.job.user", "zzti");
//	
//	   	String HADOOP_HOME="G:/hadoop/hadoop-2.5.2";
//	    conf.addResource(HADOOP_HOME+"/etc/hadoop/core-site.xml");
//	    conf.addResource(HADOOP_HOME+"/etc/hadoop/hdfs-site.xml");
//	    conf.addResource(HADOOP_HOME+"/etc/hadoop/mapred-site.xml");
//	    conf.addResource(HADOOP_HOME+"/etc/hadoop/yarn-site.xml");

		//设置mysql连接位置
		DBConfiguration.configureDB(conf, "com.mysql.jdbc.Driver",  
	            "jdbc:mysql://192.168.109.1:3306/test_mr?characterEncoding=UTF-8", "root", "password");  
		// 新建一个任务
		Job job = new Job(conf, "DBOutputormatDemo");
		// 设置主类
		job.setJarByClass(LoginMain.class);
		// 输入路径
		FileInputFormat.addInputPath(job, new Path(arg0[0]));
		// Mapper
		job.setMapperClass(LoginMapper.class);
		// Reducer
		job.setReducerClass(LoginReducer.class);
		// mapper输出格式
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		// 输出格式
	    job.setOutputFormatClass(LoginOutputFormat.class);  
	
	    LoginOutputFormat.setOutput(job, "day_number", "month", "day", "login");
	
		return job.waitForCompletion(true)?0:1;
	}

	
}
