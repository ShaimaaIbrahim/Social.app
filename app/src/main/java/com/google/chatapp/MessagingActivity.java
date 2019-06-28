package com.google.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.chatapp.Adapter.MessageAdapter;
import com.google.chatapp.Fragments.ApiService;
import com.google.chatapp.Model.Chats;
import com.google.chatapp.Notificiation.Client;
import com.google.chatapp.Notificiation.Data;
import com.google.chatapp.Notificiation.MyResponse;
import com.google.chatapp.Notificiation.Sender;
import com.google.chatapp.Notificiation.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import com.google.chatapp.Model.User;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class MessagingActivity extends AppCompatActivity {

    Bundle bundle;
    CircleImageView profile_image1;
    TextView userName;
    EditText text_send;
    ImageButton send_btn;
    String name ;
    String UserimageUrl ;
    private String useridd;
    Intent intent;
    FirebaseUser fUser;
    DatabaseReference reference;
    MessageAdapter messageAdapter;
    RecyclerView mrecyclerView;
    List<Chats> mChats;
    ApiService apiService;
   boolean notify=false;


    ValueEventListener seenListner;

    public MessagingActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        Toolbar toolbar = findViewById(R.id.mes_toolBar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessagingActivity.this, MainActivity.class));
            }
        });


        apiService= Client.getClient("https://fcm.googleapis.com/").create(ApiService.class);

        profile_image1 = findViewById(R.id.mes_profile_image);
        userName = findViewById(R.id.mes_username);
        send_btn = findViewById(R.id.sent_btn);
        text_send = findViewById(R.id.text_send);

        mrecyclerView = findViewById(R.id.mes_recycle_view);
        mrecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
       linearLayoutManager.setStackFromEnd(true);
        mrecyclerView.setLayoutManager(linearLayoutManager);

        useridd=intent.getStringExtra("userId");
        name = intent.getStringExtra("username");
        UserimageUrl = intent.getStringExtra("imageUrl");

        userName.setText(name);
        if (UserimageUrl.equals("default")) {
            profile_image1.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);
        } else {
            Glide.with(getApplicationContext()).load(UserimageUrl).into(profile_image1);
        }



       fUser = FirebaseAuth.getInstance().getCurrentUser();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     notify=true;
                String message = text_send.getText().toString();
                if (!message.equals("")) {
                    SendMessage(fUser.getUid(),useridd, message, UserimageUrl);

                } else {
                    Toast.makeText(MessagingActivity.this, "You Canot Send this Message", Toast.LENGTH_LONG).show();
                }
                text_send.setText("");
             ReadMessage(fUser.getUid(), useridd, UserimageUrl);

            }

        });

       seenMessage(useridd);
    }




    private void SendMessage(final String reciever, final String sender, String message, final String UserimageUrl) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("reciever",reciever);
        hashMap.put("sender", sender);
        hashMap.put("message", message);
        if (UserimageUrl.equals("default") || UserimageUrl.equals(null)) {
            hashMap.put("sendImage", "default");
        }
        else{
            hashMap.put("sendImage", UserimageUrl);
        }
        hashMap.put("isSeen", false);
        databaseReference.child("chats").push().setValue(hashMap);


        final DatabaseReference chatRef=FirebaseDatabase.getInstance().getReference("chatList").
                child(fUser.getUid()).child(useridd);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(useridd);


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final String mes=message;
        reference=FirebaseDatabase.getInstance().getReference("users").child(fUser.getUid());
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  User user=dataSnapshot.getValue(User.class);
                  if (notify) {
                      sendNotification(reciever, user.getUsername(), mes);
                  }
                  notify=false;
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

    }

   private void seenMessage(final String senderId) {
     DatabaseReference  reference= FirebaseDatabase.getInstance().getReference("chats");
        seenListner = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);
                    if (chats.getReciever().equals(fUser.getUid()) && chats.getSender().equals(senderId)) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);
                       snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(final String reciever , final String usernamme, final String message){
        DatabaseReference tokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=tokens.orderByKey().equalTo(reciever);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Token token=dataSnapshot.getValue(Token.class);
                Data data=new Data(fUser.getUid(),R.mipmap.ic_launcher,
                        userName+" :"+message ,"New Message" ,useridd);

                Sender sender=new Sender(data ,token.getToken());

                apiService.sendNotificiation(sender).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if (response.code()==200){
                            if (response.body().success!=1){
                                Toast.makeText(MessagingActivity.this,"Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void ReadMessage(final String myid, final String ClickedUser, final String imageUrl) {
        mChats=new ArrayList<>();
        DatabaseReference database1 = FirebaseDatabase.getInstance().getReference("chats");

        database1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               mChats.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Chats chats = dataSnapshot1.getValue(Chats.class);
                    assert chats != null;
                     if(chats.getReciever().equals(myid) && chats.getSender().equals(ClickedUser) || chats.getReciever().equals(ClickedUser) && chats
                     .getSender().equals(myid)) {

                         mChats.add(chats);
                     }
       messageAdapter = new MessageAdapter(MessagingActivity.this, mChats, imageUrl);

       mrecyclerView.setAdapter(messageAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void CurrentUser(){

    }

    public void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("users").child(fUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
    reference.removeEventListener(seenListner);
        status("offline");
    }
        }
