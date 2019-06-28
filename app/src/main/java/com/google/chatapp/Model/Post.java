package com.google.chatapp.Model;

public class Post {
    private String userId;
    private String postImage;
    private String description;
    private String publisher;



public Post(){

}

    public Post(String description,String postImage,String userId ,String publisher) {
       this.description=description;
       this.userId=userId;
        this.postImage=postImage;
        this.publisher=publisher;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPublisher() {
        return publisher;
    }

    public Post setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }
}
