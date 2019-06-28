package com.google.chatapp.Fragments;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.chatapp.Adapter.NotificiationAdapter;
import com.google.chatapp.Model.notification;
import com.google.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class NotificationFragment extends Fragment {

    private NotificiationAdapter notificiationAdapter;
    private RecyclerView recyclerView;
    private List<notification> mnotification;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_notification,container,false);

        recyclerView = view.findViewById(R.id.recycler_view_not);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mnotification=new ArrayList<notification>();


        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;

      DatabaseReference reference= FirebaseDatabase.getInstance().getReference("notification");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mnotification.clear();
                    notification no= snapshot.getValue(notification.class);

                    assert no != null;
                   if (!firebaseUser.getUid().equals(no.getUserId()) && firebaseUser.getUid().equals(no.getPostId())){
                    mnotification.add(no);
              }
            }
               Collections.reverse(mnotification);
                notificiationAdapter=new NotificiationAdapter(getContext(),mnotification);
                recyclerView.setAdapter(notificiationAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        return view;
    }


}
