package com.google.chatapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import com.google.chatapp.Adapter.UserAdapter;
import com.google.chatapp.Model.Chatlist;
import com.google.chatapp.Model.Chats;
import com.google.chatapp.Model.User;
import com.google.chatapp.Notificiation.Token;
import com.google.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;


public class ChatsFragment extends Fragment {

  private RecyclerView recyclerView;
  private UserAdapter userAdapter;
  private List<User>mUsers;
  private FirebaseUser firebaseUser;
  private DatabaseReference reference;
  private List<Chatlist>mUsersList;

    public ChatsFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mUsersList = new ArrayList<>();


      reference=FirebaseDatabase.getInstance().getReference("chatList").child(firebaseUser.getUid());
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist=snapshot.getValue(Chatlist.class);
                    mUsersList.add(chatlist);

                }
                chatlist();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

      UpdateToken(FirebaseInstanceId.getInstance().getToken());
        return view;

    }

//TODO..............................................................
private void UpdateToken(String token){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Tokens");
    Token token1=new Token(token);
    reference.child(firebaseUser.getUid()).setValue(token1);
}
//////////////////////////////////////////////////////////////////////
    private void chatlist(){
        mUsers=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  mUsers.clear();
                  for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                      User user=snapshot.getValue(User.class);
                      for (Chatlist chatlist : mUsersList){
                          assert user != null;
                          if (user.getUserId().equals(chatlist.getId())){
                                mUsers.add(user);
                          }
                      }
                      userAdapter =new UserAdapter(getContext(),mUsers,true);
                      recyclerView.setAdapter(userAdapter);

                  }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    }


