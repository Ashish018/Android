package com.ashish.booklibrary.Models;

public class BooksModelClass {

    String text;
    String url;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BooksModelClass( int id,String text, String url) {
        this.text = text;
        this.url = url;
        this.id = id;
    }

    public BooksModelClass(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
