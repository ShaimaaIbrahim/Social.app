package com.google.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.chatapp.MessagingActivity;
import com.google.chatapp.Model.Chats;
import com.google.chatapp.Model.User;
import com.google.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressWarnings("ALL")
public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.viewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chats> mChats;
    private String imageUrl;
    private User user;
    private String userNm;
    private FirebaseUser firebaseUser;


    public MessageAdapter(Context mContext, List<Chats> mChats, String imageUrl) {
        this.mContext = mContext;
        this.mChats = mChats;
        this.imageUrl = imageUrl;

    }



    View view;
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.viewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, viewGroup, false);

        }
        return new MessageAdapter.viewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.viewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Chats chats = mChats.get(i);
        viewHolder.Message.setText(chats.getMessage());

        if (chats.getSendImage().equals("default")) {
            viewHolder.mes_profile_image.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);
        } else {
            Glide.with(mContext).load(chats.getSendImage()).into(viewHolder.mes_profile_image);
        }
        if (i==mChats.size()-1){

            if (chats.getSeen())
                viewHolder.txt_isSSeen.setText("seen");
            else {
                viewHolder.txt_isSSeen.setText("delivered");
            }
        }else{
            viewHolder.txt_isSSeen.setVisibility(View.GONE);
        }
   }


    @Override
    public int getItemCount() {
        return mChats.size();
    }

    @SuppressWarnings("ClassWithoutNoArgConstructor")
    class viewHolder extends RecyclerView.ViewHolder {

        public TextView Message;
        public CircleImageView mes_profile_image;
        public TextView txt_isSSeen;

        viewHolder(View itemView) {
            super(itemView);

            Message = itemView.findViewById(R.id.showing_message);
            mes_profile_image = itemView.findViewById(R.id.ms_profile_image);
            txt_isSSeen=itemView.findViewById(R.id.txt_seen);


        }}
    @Override
  public int getItemViewType(final int position) {

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

   if (mChats.get(position).getReciever().equals(firebaseUser.getUid())){

     return MSG_TYPE_RIGHT;
     }
     else{
         return MSG_TYPE_LEFT;
     }
    }
}

