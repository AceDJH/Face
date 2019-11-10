package com.example.face.adapter;

public class Thing {
    private String keyword;//图片中的物体或场景名称
    private String score;//置信度，0-1

    public Thing(String keyword, String score) {
        this.keyword = keyword;
        this.score = score;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
