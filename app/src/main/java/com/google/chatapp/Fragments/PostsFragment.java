package com.google.chatapp.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.internal.measurement.zzdp;
import com.google.android.gms.internal.measurement.zzfp;
import com.google.chatapp.Adapter.PostAdapter;
import com.google.chatapp.EditPostActivity;
import com.google.chatapp.MainActivity;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsFragment extends Fragment {


    EditText post_txt;
   CircleImageView img_user;
   List<Post> posts;
    PostAdapter postAdapter;
     FirebaseUser firebaseUser1;
    RecyclerView recyclerView;
   List<Post>mPosts;
   List<String >following;
    EditText postFragment;
     CircleImageView img_frg;
     String imageUri;

    public PostsFragment() {

    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        recyclerView = view.findViewById(R.id.posts_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postFragment = view.findViewById(R.id.post_frg_txt);
        img_frg = view.findViewById(R.id.img_frg_user);



        firebaseUser1 = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser1.getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User muser = dataSnapshot.getValue(User.class);

                assert muser != null;
                imageUri = muser.getImageUrl();
                if (imageUri != null) {
                    if (imageUri.equals("default")) {

                        img_frg.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);
                    } else {

                        Glide.with(getContext()).load(imageUri).into(img_frg);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


 img_frg.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
        // startActivity(new Intent(getActivity(),ProfileFragment.class));
     }
 });

        mPosts = new ArrayList<>();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("posts");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Post post1 = snapshot.getValue(Post.class);
                    assert post1 != null;

                    mPosts.add(post1);

                  //  Toast.makeText(getContext(), " " + post1.getDescription(), Toast.LENGTH_LONG).show();

                    postAdapter = new PostAdapter(getContext(), mPosts);
                    recyclerView.setAdapter(postAdapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), EditPostActivity.class));

            }
        });





        return view;
    }
    }


