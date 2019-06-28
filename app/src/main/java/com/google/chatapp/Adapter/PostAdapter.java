package com.google.chatapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.chatapp.CommentActivity;
import com.google.chatapp.Fragments.NotificationFragment;
import com.google.chatapp.Model.Post;
import com.google.chatapp.Model.User;
import com.google.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder> {

 private   Context mContext;
   private List<Post>mPOsts;
   private FirebaseUser firebaseUser;
   private int likeees=0;

    public PostAdapter(@NonNull Context mContext, List<Post>mPOsts) {

        this.mContext=mContext;
         this.mPOsts=mPOsts;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.post_itm,viewGroup,false);
        return new PostAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final Post post = mPOsts.get(i);

        if (post.getPostImage() == null) {
            viewHolder.post_image.setVisibility(View.GONE);
        } else {
            viewHolder.post_image.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(post.getPostImage()).into(viewHolder.post_image);
        }


        viewHolder.description.setText(post.getDescription());

        PublisherInfo(viewHolder.profile_image, viewHolder.username, viewHolder.publisher,post.getUserId());
        isLiked(post.getUserId(), viewHolder.like);
        setLikes(viewHolder.likes, post.getUserId());
        getComments(post.getUserId(),viewHolder.comments);
        isSaved(post.getUserId(),viewHolder.save);

        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewHolder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getUserId()).
                            child(firebaseUser.getUid()).setValue(true);
//////////////////////////////////////////////////.................
                    AddNotification(post.getPublisher() ,post.getUserId());

                }else{

                    FirebaseDatabase.getInstance().getReference().child("likes").child(post.getUserId()).
                            child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        viewHolder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if (viewHolder.save.getTag().equals("save")){
                 FirebaseDatabase.getInstance().getReference().child("saves").child(firebaseUser.getUid())
                         .child(post.getUserId()).setValue(true);
                 isSaved(post.getUserId(),viewHolder.save);
             }
             else {
                 FirebaseDatabase.getInstance().getReference().child("saves").child(firebaseUser.getUid())
                         .child(post.getUserId()).removeValue();
                 isSaved(post.getUserId(),viewHolder.save);
             }
            }
        });

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
               intent.putExtra("postId",post.getUserId());
               intent.putExtra("publisherId",firebaseUser.getUid());
                intent.putExtra("username",post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        viewHolder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId",post.getUserId());
                intent.putExtra("publisherId",firebaseUser.getUid());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
    return mPOsts.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

      ImageView profile_image,post_image,like,comment,save;
      TextView username,likes,publisher,description,comments;



        viewHolder(View itemView){
            super(itemView);
     profile_image=itemView.findViewById(R.id.image_profile);
       post_image=itemView.findViewById(R.id.post_img);
       like=itemView.findViewById(R.id.like);
       comment=itemView.findViewById(R.id.comment);
       save=itemView.findViewById(R.id.save);

       username=itemView.findViewById(R.id.username);
       likes=itemView.findViewById(R.id.text_likees);
       publisher=itemView.findViewById(R.id.text_publisher);
       description=itemView.findViewById(R.id.text_discription);
       comments=itemView.findViewById(R.id.text_comments);



        }}
        private void getComments(String postid, final TextView comments){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.setText("View All "+dataSnapshot.getChildrenCount()+"Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }
        private void PublisherInfo(final ImageView profile_image, final TextView username , final TextView publisher ,String userId){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             User user=dataSnapshot.getValue(User.class);
                assert user != null;
                Glide.with(mContext).load(user.getImageUrl()).into(profile_image);
                username.setText(user.getUsername());
                publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }
        private void isLiked(String postId , final ImageView imageView){
        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assert firebaseUser != null;
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                  imageView.setImageResource(R.drawable.ic_pick_like);
                  imageView.setTag("liked");
              }
              else {
                  imageView.setImageResource(R.drawable.ic_like);
                  imageView.setTag("like");

              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        }
        private void AddNotification(final String username,final String postId ){
        final    String  Userpublisher;
            DatabaseReference reference1=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    assert user != null;
                    final  String Userpublisher=user.getUsername();
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("notification");
                    HashMap<String , Object>hashMap=new HashMap<>();
                    hashMap.put("userId",firebaseUser.getUid());
                    hashMap.put("text","liked your post");

                    hashMap.put("postId",postId);
                    hashMap.put("isPost", true);
                    hashMap.put("publisher", Userpublisher);
                    reference.push().setValue(hashMap);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }


        private void setLikes(final TextView likes , String postId){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("likes").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(dataSnapshot.getChildrenCount()+ " likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }
        private void isSaved(final String postId , final ImageView imageView ){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

            assert firebaseUser != null;
            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("saves").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()){
                    imageView.setImageResource(R.drawable.ic_save);
                    imageView.setTag("saved");
                }
                else {
                    imageView.setImageResource(R.drawable.ic_save);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }


}