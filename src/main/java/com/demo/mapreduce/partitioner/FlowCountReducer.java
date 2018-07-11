package com.demo.mapreduce.partitioner;

/**
 * @author eli
 * @date 2017/10/16 21:18
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 已经分好组了，可以直接叠加
 * //<183323,bean1><183323,bean2><183323,bean3><183323,bean4>.......
 * keyin , value , keyout , value
 * Iterable<FlowBean> values ：就是这个组的全部数据的集合
 */
public class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {

        long upSumFlow=0,dSumFlow=0;
        for (FlowBean flowBean : values){
            upSumFlow += flowBean.getUpFlow();
            dSumFlow += flowBean.getdFlow();
        }
        context.write(key,new FlowBean(upSumFlow,dSumFlow));

    }
}