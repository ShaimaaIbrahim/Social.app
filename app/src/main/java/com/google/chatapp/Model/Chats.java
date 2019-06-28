package com.google.chatapp.Model;

import android.print.PageRange;
import android.support.v7.widget.RecyclerView;

public class Chats {

    private String sender;
    private String reciever;
    private String message;
    private String sendImage;
    private boolean isSeen;


    public Chats(String sendImage,String reciever,String sender,String message, boolean isSeen) {
        this.message=message;
        this.sender=sender;
        this.sendImage=sendImage;
        this.reciever=reciever;
        this.isSeen=isSeen;
    }



    public Chats() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendImage() {
        return sendImage;
    }

    public void setSendImage(String sendImage) {
        this.sendImage = sendImage;
    }

    public String getReciever() {
        return reciever;
    }

    public Boolean getSeen() {
        return isSeen;
    }

    public void setSeen(Boolean seen) {
        isSeen = seen;
    }
}
