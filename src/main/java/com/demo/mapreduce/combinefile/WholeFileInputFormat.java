package com.demo.mapreduce.combinefile;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

public class WholeFileInputFormat extends FileInputFormat<NullWritable, BytesWritable>{

	/**
	 * 将输入文件切分为map处理所需的split,不切片,完整的文件
	 * @param context
	 * @param file
	 * @return
	 */
	@Override
	protected boolean isSplitable(JobContext context, Path file) {
		return false;
	}

	/**
	 * 创建RecordReader类, 它将从一个split生成键值对序列
	 * @param split
	 * @param context
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Override
	public RecordReader<NullWritable, BytesWritable> createRecordReader(
			InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException {
		WholeFileRecordReader reader = new WholeFileRecordReader();
		reader.initialize(split, context);
		return reader;
	}

}
