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
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.widget.*;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.chatapp.Adapter.CommentAdapter;
import com.google.chatapp.Model.Comment;
import com.google.chatapp.Model.User;
import com.google.chatapp.Model.notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

  private CommentAdapter commentAdapter;
  private RecyclerView recyclerView;
  private List<Comment>mComments;

  private   EditText addComment;
    private ImageView img_profile;

  private   String postId;
  private String username;
   private String publisherId;
   private TextView post_txt;

    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar = findViewById(R.id.tool_barr);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view_com);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mComments=new ArrayList<>();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView post_txt = findViewById(R.id.post_txt);
        img_profile = findViewById(R.id.image_profile);
        addComment = findViewById(R.id.add_comment);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");
        publisherId = intent.getStringExtra("publisherId");
        username = intent.getStringExtra("username");

        post_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addComment.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "you Can't add empty comment", Toast.LENGTH_LONG).show();
                } else {
              DatabaseReference      reference= FirebaseDatabase.getInstance().getReference("comments");
                    HashMap<String ,Object >hashMap=new HashMap<>();

                    hashMap.put("comment",addComment.getText().toString());
                    hashMap.put("publisher",firebaseUser.getUid());
                    reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                addComment.setText("");
                                Toast.makeText(getApplicationContext(), "succeeed....", Toast.LENGTH_LONG).show();
                                 AddNotification();
                               
                            }
                        }
                    });

                }
            }
        });
        getImage();
   //  ReadComment();

    }

    private void getImage(){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);
                assert user != null;
                Glide.with(CommentActivity.this).load(user.getImageUrl()).into(img_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void AddNotification() {
        final String Userpublisher;
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                final String Userpublisher = user.getUsername();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notification");
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("userId", firebaseUser.getUid());
                hashMap.put("text", "commented your post...........");
                hashMap.put("postId", postId);
                hashMap.put("isPost", true);
                hashMap.put("publisher", Userpublisher);
                reference.push().setValue(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
        private void ReadComment(){

            DatabaseReference reference=FirebaseDatabase.getInstance().getReference("comments");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mComments.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Comment comment=snapshot.getValue(Comment.class);
                        mComments.add(comment);
                        assert comment != null;
                        Toast.makeText(CommentActivity.this,".."+comment.getComment(),Toast.LENGTH_LONG).show();
                    }
                    commentAdapter=new CommentAdapter(CommentActivity.this,mComments);
                    recyclerView.setAdapter(commentAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }


}
