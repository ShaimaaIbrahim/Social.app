package com.google.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.design.widget.TabLayout;
import com.bumptech.glide.Glide;
import com.google.chatapp.Fragments.ChatsFragment;
import com.google.chatapp.Fragments.NotificationFragment;
import com.google.chatapp.Fragments.PostsFragment;
import com.google.chatapp.Fragments.ProfileFragment;
import com.google.chatapp.Fragments.UsersFragment;
import com.google.chatapp.Model.Chats;
import com.google.chatapp.Model.User;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private CircleImageView profile_image2;
   private   TextView Usernamee;
     FirebaseUser firebaseUserr;
    DatabaseReference mRef;
  private   Intent intent;
  private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


      Toolbar toolbar = findViewById(R.id.tooly_bar);
        setSupportActionBar(toolbar);
      getSupportActionBar().setTitle("");


        Usernamee =findViewById(R.id.usereeeeer);
        profile_image2=findViewById(R.id.profile_image2);

        assert  firebaseUserr!=null;
        firebaseUserr= FirebaseAuth.getInstance().getCurrentUser();

      mRef=FirebaseDatabase.getInstance().getReference("users").child(firebaseUserr.getUid());

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                assert user != null;

               Usernamee.setText(user.getUsername());

                if (user.getImageUrl().equals("default")) {

                profile_image2.setImageResource(R.drawable.common_google_signin_btn_icon_dark_focused);

                } else {

                      Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_image2);

                    }
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       tabLayout=findViewById(R.id.tab_layout);
     final    ViewPager viewPager=findViewById(R.id.view_pager);



        mRef=FirebaseDatabase.getInstance().getReference("chats");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewPagerAdapter   viewPagerAdap=new viewPagerAdapter(getSupportFragmentManager());
                int unread=0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats chat=snapshot.getValue(Chats.class);
                    assert chat != null;
                    if (chat.getSender().equals(firebaseUserr.getUid()) && !chat.getSeen()){
                        unread++; } }

                if (unread==0){
                    viewPagerAdap.addFragments(new PostsFragment(), " ");
                    viewPagerAdap.addFragments(new UsersFragment(), " ");
                    viewPagerAdap.addFragments(new ChatsFragment()," ");
                    viewPagerAdap.addFragments(new NotificationFragment(), " ");
                    viewPagerAdap.addFragments(new ProfileFragment(), " ");
                }
                else{
                    viewPagerAdap.addFragments(new PostsFragment(), " ");
                    viewPagerAdap.addFragments(new UsersFragment(), " ");
                    viewPagerAdap.addFragments(new ChatsFragment(),"("+unread+")");
                    viewPagerAdap.addFragments(new NotificationFragment(), " ");
                    viewPagerAdap.addFragments(new ProfileFragment(), " ");
                }

                viewPager.setAdapter(viewPagerAdap);
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_message);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_notification);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic_person);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu,menu);
         return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.log_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class viewPagerAdapter extends FragmentPagerAdapter{

        private ArrayList<Fragment>fragments;
        private ArrayList<String> titles;





        public viewPagerAdapter(FragmentManager supportFragmentManager ) {
            super(supportFragmentManager);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }


        @Override
        public Fragment getItem(int i) {
            return  fragments.get(i);

        }

        @Override
        public int getCount() {
          return fragments.size();
        }

        public void  addFragments(Fragment fragment,String title) {
         fragments.add(fragment);
         titles.add(title);

        }

     @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);

        }

    }

        public void status(String status){
            mRef=FirebaseDatabase.getInstance().getReference("users").child(firebaseUserr.getUid());

            HashMap <String,Object>hashMap=new HashMap<>();
            hashMap.put("status",status);
           mRef.updateChildren(hashMap);
        }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
