package com.google.chatapp.Fragments;

import com.google.chatapp.Notificiation.MyResponse;
import com.google.chatapp.Notificiation.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Headers(
            {"content-Type:appliciation/json",
            "Authorization:key=AAAAnbIxIHE:APA91bGIG_jkLB2-R4zL-QlIzpCcmsx6rIt1TSreXzHN0_uLGJgaFeQQMCKw0rM1GIOqeJFb3JNKroalP-knh7e1X4JafzNafGz8YyvWYRGFy3-Rv09uWiIH-um0nq0dn1SDOUSMW4Th"}
    )


    @POST("fcm/send")
    Call<MyResponse>sendNotificiation(@Body Sender body);
}

