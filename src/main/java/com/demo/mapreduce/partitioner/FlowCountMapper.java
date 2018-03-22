package com.demo.mapreduce.partitioner;

/**
 * @author eli
 * @date 2017/10/16 21:17
 */

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * keyin , value , keyout , value
 * 所以输出的是FlowBean
 */
public class FlowCountMapper extends Mapper<LongWritable,Text,Text,FlowBean> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] lineArray = line.split("\t");
        String phone = lineArray[1];
        long upFlow = Long.parseLong(lineArray[lineArray.length - 3]);
        long dFlow = Long.parseLong(lineArray[lineArray.length - 2]);

        context.write(new Text(phone), new FlowBean(upFlow, dFlow));
    }
}