package com.google.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.chatapp.MessagingActivity;
import com.google.chatapp.Model.Chats;
import com.google.chatapp.R;
import com.google.chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    private Context mContext;
    private List<User> mUser;
    Intent intent;
    private boolean inChat;
    private String thelastMes;

    public UserAdapter(Context mContext, List<User> mUser, boolean inChat) {
        this.mContext = mContext;
        this.mUser = mUser;
        this.inChat = inChat;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);

        return new UserAdapter.viewHolder(view);
    }

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, final int i) {


        final User userq = mUser.get(i);

        viewHolder.username.setText(userq.getUsername());

        if (userq.getImageUrl().equals("default")) {
            viewHolder.profile_image.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);
        } else {
            Glide.with(mContext).load(userq.getImageUrl()).into(viewHolder.profile_image);
        }

        if (inChat){
            lastMessage(userq.getUserId(),viewHolder.last_mes);
        }else{
            viewHolder.last_mes.setVisibility(View.GONE);
        }


        if (inChat) {
            if (userq.getStatus().equals("online")) {
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            } else {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        }


   viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        intent = new Intent(mContext, MessagingActivity.class);
        intent.putExtra("userId",userq.getUserId());
        intent.putExtra("username", userq.getUsername());
        intent.putExtra("imageUrl", userq.getImageUrl());
      mContext.startActivity(intent);

        }
});
   }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    @SuppressWarnings("ClassWithoutNoArgConstructor")
    class viewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public CircleImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_mes;

        viewHolder(View itemView){
            super(itemView);
            username=itemView.findViewById(R.id.user_item);
            img_on=itemView.findViewById(R.id.online_state);
            img_off=itemView.findViewById(R.id.offline_state);
            profile_image=itemView.findViewById(R.id.profile_image1);
            last_mes=itemView.findViewById(R.id.last_mes);


        }}
        private void lastMessage(final String userId, final TextView lastMes){
             thelastMes="default";
             final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("chats");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Chats chats=snapshot.getValue(Chats.class);
                        if (chats.getReciever().equals(firebaseUser.getUid()) && chats.getSender().equals(userId)||
                        chats.getReciever().equals(userId) && chats.getSender().equals(firebaseUser.getUid())){
                            thelastMes=chats.getMessage();
                        }
                    }
                    switch (thelastMes){
                        case "default":
                            lastMes.setText("No message");
                            break;
                            default:
                                lastMes.setText(thelastMes);
                                break;
                    }
                    thelastMes="default";
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
