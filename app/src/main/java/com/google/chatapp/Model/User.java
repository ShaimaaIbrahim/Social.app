package com.google.chatapp.Model;

public class User  {


    private String userId;
    private String username;
    private String imageUrl;
    private String status;
    private String search;

    public   User(String username,String userId,String imageUrl,String status,String search) {
        this.userId = userId;
        this.username = username;
        this.imageUrl = imageUrl;
        this.status=status;
        this.search=search;
    }



    public User() {


    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
