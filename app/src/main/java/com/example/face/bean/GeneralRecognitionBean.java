/**
 * Copyright 2019 bejson.com
 */
package com.example.face.bean;
import java.util.List;

/**
 * Auto-generated: 2019-11-09 10:10:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class GeneralRecognitionBean {

    private long log_id;
    private List<Result> result;
    private int result_num;
    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }
    public long getLog_id() {
        return log_id;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
    public List<Result> getResult() {
        return result;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }
    public int getResult_num() {
        return result_num;
    }

    @Override
    public String toString() {
        return "GeneralRecognitionBean{" +
                "log_id=" + log_id +
                ", result=" + result +
                ", result_num=" + result_num +
                '}';
    }
}