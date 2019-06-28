package com.google.chatapp.Model;

public class notification {
    private String postId;
    private String userId;
    private String text;
    private boolean isPost;
    private String publisher;

    public notification(boolean isPost , String userId ,String text, String publisher ) {
        this.isPost = isPost;
        this.userId=userId;
        this.text=text;
        this.publisher=publisher;
    }

    public notification() {
    }

    public String getUserId() {
        return userId;
    }

    public notification setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getText() {
        return text;
    }

    public notification setText(String text) {
        this.text = text;
        return this;
    }

    public boolean isPost() {
        return isPost;
    }

    public notification setPost(boolean post) {
        isPost = post;
        return this;
    }

    public String getPostId() {
        return postId;
    }

    public notification setPostId(String postId) {
        this.postId = postId;
        return this;
    }

    public String getPublisher() {
        return publisher;
    }

    public notification setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }
}
