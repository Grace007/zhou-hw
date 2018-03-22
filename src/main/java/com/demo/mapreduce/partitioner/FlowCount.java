package com.demo.mapreduce.partitioner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
1363157985066 	13726230503	00-FD-07-A4-72-B8:CMCC	120.196.100.82             24	27	2481	24681	200  1363157995052 	13826544101	5C-0E-8B-C7-F1-E0:CMCC	120.197.40.4			4	0	264	0	200
  1363157991076 13926435656	20-10-7A-28-CC-0A:CMCC	120.196.100.99			2	4	132	1512	200
  1363154400022 	13926251106	5C-0E-8B-8B-B1-50:CMCC	120.197.40.4			4	0	240	0	200
*/

/**
 * @author eli
 * @date 2017/10/16 16:43
 * 分析:
 *
 *
 */

public class FlowCount  {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            args = new String[2];
            args[0] = "hdfs://mini1:9000/flowcount/input/";
            args[1] = "hdfs://mini1:9000/flowcount/output/";
        }

        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        job.setJarByClass(FlowCount.class);
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReducer.class);
        //指定我们自定义的数据分区器
        job.setPartitionerClass(ProvincePartitioner.class);
        //同时指定相应“分区”数量的reducetask
        job.setNumReduceTasks(5);
        //mapper输出的key , value的类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);
        //reducer输出的key , value的类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        boolean re = job.waitForCompletion(true);
        System.exit(re?0:1);

    }
}
