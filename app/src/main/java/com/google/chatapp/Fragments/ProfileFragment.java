package com.google.chatapp.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.chatapp.Adapter.PostAdapter;
import com.google.chatapp.EditPostActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import android.net.UrlQuerySanitizer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import de.hdodenhof.circleimageview.CircleImageView;


@SuppressWarnings("ALL")
public class ProfileFragment extends Fragment {

    CircleImageView circle_image_profile;
    TextView Profile_name;
    FirebaseUser profile_user;
    DatabaseReference profile_refrence;
    DatabaseReference reference;
    private Uri imageUri;
    public StorageTask uploadTask;
    private static final int RC_PHOTO_PICKER =  2;
    private static final int RC_SIGN_IN =1;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatStorageRefrence;
    FirebaseUser firebaseUser;
    private EditText post_edit;
    private List<Post>mPosts;

    PostAdapter postAdapter;
    RecyclerView recyclerView;
    private List<String >mySaves;

    private List<Post>mPosts_saves;
    public ProfileFragment() {

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        circle_image_profile = view.findViewById(R.id.circle_image_profile);
        Profile_name = view.findViewById(R.id.profile_name);
        post_edit=view.findViewById(R.id.post_frg_txt);

        recyclerView = view.findViewById(R.id.posts_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        post_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditPostActivity.class));
            }
        });

       mPosts=new ArrayList<>();
       firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference    databaseReference1=FirebaseDatabase.getInstance().getReference("posts");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post.getUserId().equals(profile_user.getUid())) {
                        mPosts.add(post);
                    }
                    postAdapter=new PostAdapter(getContext(),mPosts);
                    recyclerView.setAdapter(postAdapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        profile_user = FirebaseAuth.getInstance().getCurrentUser();
        profile_refrence = FirebaseDatabase.getInstance().getReference("users").child(profile_user.getUid());
//...........................STORAGE_REFRENCE................................
        mFirebaseStorage=FirebaseStorage.getInstance();

        mChatStorageRefrence=mFirebaseStorage.getReference("photo_url");

//.............................................................................
        profile_refrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User Puser = dataSnapshot.getValue(User.class);
                Profile_name.setText(Puser.getUsername());
                if (Puser.getImageUrl().equals("default")) {
                    circle_image_profile.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);
                } else {

       Glide.with(getContext()).load(Puser.getImageUrl()).into(circle_image_profile);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        circle_image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        return view;

    }

//to open photo Picker...................................................................................
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
     intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }
//........................................................................................................




    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
            getActivity();
                        if (requestCode==RC_PHOTO_PICKER && resultCode==getActivity().RESULT_OK){
                       final Uri selectedImageUri=data.getData();
                              circle_image_profile.setImageURI(selectedImageUri);
                            final StorageReference photoRef=mChatStorageRefrence.child(selectedImageUri.getLastPathSegment());
                        photoRef.putFile(selectedImageUri).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Uri downLoad=taskSnapshot.getUploadSessionUri();

                               reference=FirebaseDatabase.getInstance().getReference("users").child(profile_user.getUid());
                                 HashMap <String ,Object> hashMap=new HashMap();

                                 hashMap.put("imageUrl",selectedImageUri.toString());
                             reference.updateChildren(hashMap);
                             return;


                            }
                        });
                    }
                        }
                }

























