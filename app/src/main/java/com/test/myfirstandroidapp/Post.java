package com.test.myfirstandroidapp;

public class Post {
    private int id;
    private String postedBy;
    private String title;

    public Post(){

    }

    public Post(int id, String postedBy, String title) {
        this.id = id;
        this.postedBy = postedBy;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
