package com.google.chatapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.chatapp.Adapter.UserAdapter;
import com.google.chatapp.MessagingActivity;
import com.google.chatapp.Model.User;
import com.google.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


@SuppressWarnings("ALL")
public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> users;
    private String FFuser;
  private   DatabaseReference databaseReference;
   private EditText search_users;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       users=new ArrayList<>();


      final FirebaseUser   firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
      final DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
      databaseReference1.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               User fuser=dataSnapshot.getValue(User.class);
               FFuser=fuser.getUsername();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

          databaseReference = FirebaseDatabase.getInstance().getReference("users");


          databaseReference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //  if (search_users.getText().toString().equals("")) {
                      users.clear();
                      for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {

                          User user = Snapshot.getValue(User.class);


                          assert user != null;
                          if (!FFuser.equals(user.getUsername())) {
                              users.add(user);
                          }
                      }
                      userAdapter = new UserAdapter(getContext(), users, true);
                      recyclerView.setAdapter(userAdapter);

                  }
          //   }
              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
   search_users=view.findViewById(R.id.search_users);
   search_users.addTextChangedListener(new TextWatcher() {
       @Override
       public void beforeTextChanged(CharSequence s, int start, int count, int after) {

       }

       @Override
       public void onTextChanged(CharSequence s, int start, int before, int count) {
         SearchUsers(s.toString().toLowerCase());
       }


       @Override
       public void afterTextChanged(Editable s) {

       }
   });
return view;

    }

    private void SearchUsers(String toString) {
       final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
       Query query =  FirebaseDatabase.getInstance().getReference("users").orderByChild("search")
               .startAt(toString).
                       endAt(toString+"\uf8ff");

       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               users.clear();
               for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                   User mUser = dataSnapshot1.getValue(User.class);
                   if (mUser.getUsername().toLowerCase().equals(mUser.getSearch()) || mUser.getUsername().equals(mUser.getSearch().toUpperCase())) {
                       users.add(mUser);
                   }
               }
               userAdapter=new UserAdapter(getContext(),users,false);
               recyclerView.setAdapter(userAdapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }
}

