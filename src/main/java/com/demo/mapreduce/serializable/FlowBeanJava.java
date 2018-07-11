package com.demo.mapreduce.serializable;

import java.io.Serializable;

/**
 * @author eli
 * @date 2018/3/23 11:44
 */
public class FlowBeanJava implements Serializable {
    private long upFlow;
    private long dFlow;
    private long sumFlow;

    //反序列化时，需要反射调用空参构造函数，所以要显示定义一个
    public FlowBeanJava(){}

    public FlowBeanJava(long upFlow, long dFlow) {
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
    @Override
    public String toString() {

        return upFlow + "\t" + dFlow + "\t" + sumFlow;
    }

}
