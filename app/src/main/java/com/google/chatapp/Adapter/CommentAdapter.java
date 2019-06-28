package com.google.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.chatapp.MainActivity;
import com.google.chatapp.Model.Comment;
import com.google.chatapp.Model.User;
import com.google.chatapp.R;
import com.google.chatapp.CommentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.viewHolder>{

private Context mContext;
private List<Comment>mComments;


    public CommentAdapter(Context mContext, List<Comment> mComments) {
        this.mContext=mContext;
        this.mComments=mComments;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         View view= LayoutInflater.from(mContext).inflate(R.layout.comment_item,viewGroup,false);
         return new  CommentAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

    FirebaseUser   firebaseuser= FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment=mComments.get(i);
        viewHolder.comment.setText(comment.getComment());
       getUserInfo(viewHolder.profile_image,viewHolder.username,comment.getPublisher());

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid",comment.getPublisher());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
      public ImageView profile_image;
      public TextView comment,username;

            viewHolder(View itemView) {
                super(itemView);
          profile_image=itemView.findViewById(R.id.image_profile);
          username=itemView.findViewById(R.id.username_com);
          comment=itemView.findViewById(R.id.comment);
            }
        }
        private void getUserInfo(final ImageView imageView , final TextView textView , String publisherId){
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users").child(publisherId);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user=dataSnapshot.getValue(User.class);
                    assert user != null;
                    textView.setText(user.getUsername());

                    Glide.with(mContext).load(user.getImageUrl()).into(imageView);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
