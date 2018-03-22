package com.demo.mapreduce.partitioner;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

/**
 * @author eli
 * @date 2017/10/16 16:37
 */
public class ProvincePartitioner extends Partitioner<Text,FlowBean> {
    static HashMap<String, Integer> provinceMap = new HashMap<String, Integer>();

    static {

        provinceMap.put("135", 0);
        provinceMap.put("136", 1);
        provinceMap.put("137", 2);
        provinceMap.put("138", 3);
        provinceMap.put("139", 4);

    }

    @Override
    public int getPartition(Text key, FlowBean value, int numPartitions) {
        String string = key.toString().substring(0,3);
        Integer code = provinceMap.get(string);
        return code != null ? code:4;
    }
}
