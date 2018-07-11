package com.demo.mapreduce.combinefile;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * 
 * RecordReader的核心工作逻辑：
 * 通过nextKeyValue()方法去读取数据构造将返回的key   value
 * 通过getCurrentKey 和 getCurrentValue来返回上面构造好的key和value
 *
 * 每个split的就对应一个mapper task，split的数量决定了mappertask的数量。
 *
 * 为每个split创建一个RecordReader实例,该实例调用getNextKeyValue并返回一个布尔值
 * 组合使用InputFormat和RecordReader可以将任何类型的输入数据转换为MapReduce所需的键值对
 * @author
 *
 */
class WholeFileRecordReader extends RecordReader<NullWritable, BytesWritable> {
	//InputSplit描述了数据块的切分方式 RecordReader类则是实际用来加载split分片数据,并把数据转换为适合Mapper类里面map()方法处理的<key, value>形式
	private FileSplit fileSplit;
	private Configuration conf;
	private BytesWritable value = new BytesWritable();
	//标志当前文件是否被读取
	private boolean processed = false;

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		this.fileSplit = (FileSplit) split;
		this.conf = context.getConfiguration();
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		if (!processed) {
			byte[] contents = new byte[(int) fileSplit.getLength()];
			Path file = fileSplit.getPath();
			FileSystem fs = file.getFileSystem(conf);
			FSDataInputStream in = null;
			try {
				in = fs.open(file);
				IOUtils.readFully(in, contents, 0, contents.length);
				value.set(contents, 0, contents.length);
			} finally {
				IOUtils.closeStream(in);
			}
			processed = true;
			return true;
		}
		return false;
	}

	
	
	
	@Override
	public NullWritable getCurrentKey() throws IOException,
			InterruptedException {
		return NullWritable.get();
	}

	@Override
	public BytesWritable getCurrentValue() throws IOException,
			InterruptedException {
		return value;
	}

	/**
	 * 返回当前进度
	 */
	@Override
	public float getProgress() throws IOException {
		return processed ? 1.0f : 0.0f;
	}

	@Override
	public void close() throws IOException {
		// do nothing
	}
}