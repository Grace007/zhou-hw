package com.demo.mapreduce.login_mysql;

import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class LoginOutputFormat<K extends DBWritable, V> extends
		OutputFormat<K, V> {

	public void checkOutputSpecs(JobContext context) throws IOException,
			InterruptedException {
	}

	/**
	 * 多个输出分类或是单个输出分类
	 * @param context
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public OutputCommitter getOutputCommitter(TaskAttemptContext context)
			throws IOException, InterruptedException {
		return new FileOutputCommitter(FileOutputFormat.getOutputPath(context),
				context);
	}

	/** * A RecordWriter that writes the reduce output to a SQL table */
	public class DBRecordWriter extends RecordWriter<K, V> {
		private Connection connection;
		private PreparedStatement statement;

		public DBRecordWriter() throws SQLException {
		}

		public DBRecordWriter(Connection connection, PreparedStatement statement) throws SQLException {
			this.connection = connection;
			this.statement = statement;
			this.connection.setAutoCommit(false);
		}

		public Connection getConnection() {
			return connection;
		}

		public PreparedStatement getStatement() {
			return statement;
		}

		public void close(TaskAttemptContext context) throws IOException {
			try {
				statement.executeBatch();
				connection.commit();
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
				}
				throw new IOException(e.getMessage());
			} finally {
				try {
					statement.close();
					connection.close();
				} catch (SQLException ex) {
					throw new IOException(ex.getMessage());
				}
			}
		}

		/** {@inheritDoc} */
		public void write(K key, V value) throws IOException {
			try {
				key.write(statement);
				statement.addBatch();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 构造sql语句
	 * @param table
	 * @param fieldNames
	 * @return
	 */
	public String constructQuery(String table, String[] fieldNames) {
		if (fieldNames == null) {
			throw new IllegalArgumentException("Field names may not be null");
		}
		StringBuilder query = new StringBuilder();
		//replace into是insert into 的增强版
		//1. 首先判断数据是否存在； 2. 如果不存在，则插入；3.如果存在，则更新。
		query.append("replace into ").append(table);  
        System.err.println("fieldNames.length:" + fieldNames.length);  
        if (fieldNames.length > 0) {  
            query.append(" (" + fieldNames[0] + ",");
            query.append(fieldNames[1] + ","); 
            query.append(fieldNames[2] + ")");  
            query.append(" values ");  
            query.append(" (?,?,?) ");  
            System.err.println(query.toString());  
            return query.toString();  
		} else {
			return null;
		}
	}

	/** {@inheritDoc} */
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext context) throws IOException {
		DBConfiguration dbConf = new DBConfiguration(context.getConfiguration());
		String tableName = dbConf.getOutputTableName();
		String[] fieldNames = dbConf.getOutputFieldNames();
		if (fieldNames == null) {
			fieldNames = new String[dbConf.getOutputFieldCount()];
		}
		try {
			Connection connection = dbConf.getConnection();
			PreparedStatement statement = null;
			statement = connection.prepareStatement(constructQuery(tableName, fieldNames));
			return new DBRecordWriter(connection, statement);
		} catch (Exception ex) {
			throw new IOException(ex.getMessage());
		}
	}

	public static void setOutput(Job job, String tableName, String... fieldNames) throws IOException {
		if (fieldNames.length > 0 && fieldNames[0] != null) {
			DBConfiguration dbConf = setOutput(job, tableName);
			dbConf.setOutputFieldNames(fieldNames);
		} else {
			if (fieldNames.length > 0) {
				setOutput(job, tableName, fieldNames.length);
			} else {
				throw new IllegalArgumentException("Field names must be greater than 0");
			}
		}
	}

	public static void setOutput(Job job, String tableName, int fieldCount) throws IOException {
		DBConfiguration dbConf = setOutput(job, tableName);
		dbConf.setOutputFieldCount(fieldCount);
	}

	private static DBConfiguration setOutput(Job job, String tableName) throws IOException {
		job.setOutputFormatClass(LoginOutputFormat.class);
		job.setReduceSpeculativeExecution(false);
		DBConfiguration dbConf = new DBConfiguration(job.getConfiguration());
		dbConf.setOutputTableName(tableName);
		return dbConf;
	}
}