/**
 * Copyright 2019 bejson.com
 */
package com.example.face.bean;

/**
 * Auto-generated: 2019-11-09 10:10:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result {

    private Baike_info baike_info;
    private String keyword;
    private String root;
    private double score;
    public void setBaike_info(Baike_info baike_info) {
        this.baike_info = baike_info;
    }
    public Baike_info getBaike_info() {
        return baike_info;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    public String getKeyword() {
        return keyword;
    }

    public void setRoot(String root) {
        this.root = root;
    }
    public String getRoot() {
        return root;
    }

    public void setScore(double score) {
        this.score = score;
    }
    public double getScore() {
        return score;
    }

}