package com.demo.mapreduce.login_mysql;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWritable implements Writable, DBWritable {  
    String tbl_month_time;
    String tbl_day_time;
    int tbl_number;

    public LoginWritable() {  
    }  

    public LoginWritable(String month, String day, int number) {  
        this.tbl_day_time = day; 
        this.tbl_month_time = month;
        this.tbl_number = number;  
    }  

    @Override  
    public void write(PreparedStatement statement) throws SQLException {  
        statement.setString(1, this.tbl_month_time);
        statement.setString(2, this.tbl_day_time);
        statement.setInt(3, this.tbl_number);  
    }  

    @Override  
    public void readFields(ResultSet resultSet) throws SQLException {  
        this.tbl_month_time = resultSet.getString(1);
        this.tbl_day_time = resultSet.getString(2); 
        this.tbl_number = resultSet.getInt(3);  
    }  

    @Override  
    public void write(DataOutput out) throws IOException {  
        out.writeUTF(this.tbl_month_time);
        out.writeUTF(this.tbl_day_time);
        out.writeInt(this.tbl_number);  
    }  

    @Override  
    public void readFields(DataInput in) throws IOException {  
        this.tbl_month_time = in.readUTF();
        this.tbl_day_time = in.readUTF();
        this.tbl_number = in.readInt();  
    }  

    public String toString() {  
        return new String(this.tbl_month_time + " " + this.tbl_day_time + " " + this.tbl_number);  
    }  
}  