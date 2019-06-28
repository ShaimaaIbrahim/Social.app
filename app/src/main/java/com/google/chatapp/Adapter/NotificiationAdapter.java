package com.google.chatapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.chatapp.Model.Post;
import com.google.chatapp.Model.User;
import com.google.chatapp.Model.notification;
import com.google.chatapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificiationAdapter extends RecyclerView.Adapter<NotificiationAdapter.viewHolder> {
    private Context mContext;
    private List<notification> mNotification;
    public String  discription;
    public NotificiationAdapter(Context mContext, List<notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.notification_item,viewGroup,false);
        return new NotificiationAdapter.viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        notification no=mNotification.get(i);

        viewHolder.comment.setText(no.getPublisher() +"  "+ no.getText());
       getUserInfo(viewHolder.pofile_img,viewHolder.username,no.getPostId());
       viewHolder.username.setText(no.getPublisher());

        if (no.isPost()){

            viewHolder.post_img.setVisibility(View.VISIBLE);
            getPostImage(viewHolder.post_img,no.getUserId());
        }else {
          //  viewHolder.pofile_img.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
 return mNotification.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        public ImageView post_img, pofile_img;
        public TextView username, comment;

        viewHolder(View itemView) {
            super(itemView);
            pofile_img = itemView.findViewById(R.id.image_profile);
            post_img = itemView.findViewById(R.id.post_img);
            username = itemView.findViewById(R.id.username);
            comment = itemView.findViewById(R.id.comment);
        }
    }
    private void getUserInfo(final ImageView imageView , final TextView username, final String  userId){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                Glide.with(mContext).load(user.getImageUrl()).into(imageView);
               //  username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getPostImage(final ImageView imageView , String postId){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("posts").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post=dataSnapshot.getValue(Post.class);
                assert post != null;
                Glide.with(mContext).load(post.getPostImage()).into(imageView);
               discription=post.getDescription();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private String getDesc(String postId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                assert post != null;
                discription = post.getDescription();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return  discription;
    }

    }
