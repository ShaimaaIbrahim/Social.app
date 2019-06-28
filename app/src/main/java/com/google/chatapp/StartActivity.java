package com.google.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class StartActivity extends AppCompatActivity {


Button register,login;
FirebaseUser firebaseUser;

    public StartActivity() {
    }

   @Override
    protected void onStart() {

        super.onStart();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
      if (firebaseUser!=null){
      startActivity(new Intent(StartActivity.this,MainActivity.class));
        finish();
      }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


login=findViewById(R.id.login_act);
register=findViewById(R.id.register_act);


login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(StartActivity.this,LoginActivity.class));
    }
});

register.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(StartActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
});
        }
    }

