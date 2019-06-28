package com.google.chatapp.Notificiation;

import com.google.chatapp.MessagingActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class MyFirebaseMessage extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sended=remoteMessage.getData().get("sended");

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        assert sended != null;
        if (firebaseUser!=null && sended.equals(firebaseUser.getUid())){
            sendNotificiation(remoteMessage);
        }
    }

    private void sendNotificiation(RemoteMessage remoteMessage) {
        String user=remoteMessage.getData().get("user");
        String icon=remoteMessage.getData().get("icon");
        String title=remoteMessage.getData().get("title");
        String body=remoteMessage.getData().get("body");

        RemoteMessage.Notification  notification=remoteMessage.getNotification();
        int j=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this, MessagingActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("userId",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder= new NotificationCompat.Builder(this).
                setSmallIcon(Integer.parseInt(icon)).
                setContentTitle(title).
                setContentText(body).
                setAutoCancel(true).
                setSound(defaultSound).setContentIntent(pendingIntent);

        NotificationManager noti =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i=0;
        if (j>0){
            j=i; }

       noti.notify(i, builder.build());

    }
}
