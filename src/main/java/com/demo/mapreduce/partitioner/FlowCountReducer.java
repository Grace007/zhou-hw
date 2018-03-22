package com.demo.mapreduce.partitioner;

/**
 * @author eli
 * @date 2017/10/16 21:18
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * //<183323,bean1><183323,bean2><183323,bean3><183323,bean4>.......
 * keyin , value , keyout , value
 */
public class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {
    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
           /* int count=0;

            for (IntWritable value:values){
                count +=value.get();
            }
            context.write(key,new IntWritable(count));
*/
        long upSumFlow=0,dSumFlow=0;
        for (FlowBean flowBean : values){
            upSumFlow += flowBean.getUpFlow();
            dSumFlow += flowBean.getdFlow();
        }
        context.write(key,new FlowBean(upSumFlow,dSumFlow));

    }
}