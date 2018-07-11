package com.demo.mapreduce.serializable;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author eli
 * @date 2017/10/16 16:39
 *
 */
public class FlowBeanHadoop implements WritableComparable<FlowBeanHadoop> {
    private long upFlow;
    private long dFlow;
    private long sumFlow;

    //反序列化时，需要反射调用空参构造函数，所以要显示定义一个
    public FlowBeanHadoop(){}

    public FlowBeanHadoop(long upFlow, long dFlow) {
        this.upFlow = upFlow;
        this.dFlow = dFlow;
        this.sumFlow = upFlow + dFlow;
    }


    public long getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(long upFlow) {
        this.upFlow = upFlow;
    }

    public long getdFlow() {
        return dFlow;
    }

    public void setdFlow(long dFlow) {
        this.dFlow = dFlow;
    }

    public long getSumFlow() {
        return sumFlow;
    }

    public void setSumFlow(long sumFlow) {
        this.sumFlow = sumFlow;
    }
    /**
     * 序列化方法
     */
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(dFlow);
        dataOutput.writeLong(sumFlow);
    }
    /**
     * 反序列化方法
     * 注意：反序列化的顺序跟序列化的顺序完全一致
     */
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        upFlow=dataInput.readLong();
        dFlow=dataInput.readLong();
        sumFlow=dataInput.readLong();
    }
    @Override
    public String toString() {

        return upFlow + "\t" + dFlow + "\t" + sumFlow;
    }
    @Override
    public int compareTo(FlowBeanHadoop o) {
        //自定义倒序比较规则
        return sumFlow > o.getSumFlow() ? -1:1;
    }

}
