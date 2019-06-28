package com.google.chatapp.Notificiation;

public class Data  {

    private String user;
    private int icon;
    private String body;
    private String title;
    private String sended;

    public Data(String user, int icon ,String body ,String title ,String sended) {
        this.user=user;
        this.icon=icon;
        this.sended=sended;
        this.title=title;
        this.body = body;
    }

    public Data() {
    }

    public String getBody() {
        return body;
    }

    public Data setBody(String body) {
        this.body = body;
        return this;
    }

    public int getIcon() {
        return icon;
    }

    public Data setIcon(int icon) {
        this.icon = icon;
        return this;
    }

    public String getSended() {
        return sended;
    }

    public Data setSended(String sended) {
        this.sended = sended;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Data setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getUser() {
        return user;
    }

    public Data setUser(String user) {
        this.user = user;
        return this;
    }
}

