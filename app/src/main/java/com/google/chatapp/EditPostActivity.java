package com.google.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.chatapp.Model.Post;
import com.google.chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditPostActivity extends AppCompatActivity {

   private EditText post_edit_txt;
   private ImageView post_edit_img;
   private TextView textView;
   private FirebaseUser firebaseUser;
  private   String userNamee;
  private String userImage;
  FirebaseUser firebaseUser1;
    private DatabaseReference databaseReference;
  private String picked_img;
  private static final int RC_PHOTO_PICKER=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        Toolbar toolbar = findViewById(R.id.edit_post_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Write Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView post_img = findViewById(R.id.edit_img_post);

        post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            //    Toast.makeText(getApplicationContext(),""+picked_img,Toast.LENGTH_LONG).show();

            }
        });

toolbar.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        post_edit_txt = findViewById(R.id.edit_post_txt);
        if (!post_edit_txt.getText().toString().equals("")){
        MakePost(post_edit_txt.getText().toString());
        startActivity(new Intent(EditPostActivity.this,MainActivity.class));
        Toast.makeText(EditPostActivity.this,"Posted Succefully",Toast.LENGTH_LONG).show();
        finish();
    }else{
            Toast.makeText(EditPostActivity.this,"Posted is Empty",Toast.LENGTH_LONG).show();

        }

    }
});

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 User user=dataSnapshot.getValue(User.class);
                assert user != null;
                userNamee=user.getUsername();
                userImage=user.getImageUrl();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

private void MakePost(String edit_post){
        FirebaseUser firebaseUser2=FirebaseAuth.getInstance().getCurrentUser();
       // String post_edit=post_edit_txt.getText().toString();
    assert firebaseUser != null;
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("posts");

    HashMap<String ,Object>hashMap=new HashMap<>();
    assert firebaseUser2 != null;
    hashMap.put("userId",firebaseUser2.getUid());
    hashMap.put("postImage",picked_img);
    hashMap.put("description",edit_post);
    hashMap.put("publisher",userNamee);

    databaseReference.push().setValue(hashMap);

}

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            picked_img = selectedImageUri.toString();


        }
    }}

