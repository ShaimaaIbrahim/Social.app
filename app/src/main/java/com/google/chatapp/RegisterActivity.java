package com.google.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.chatapp.R;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import android.support.v4.widget.*;
import android.support.v7.widget.Toolbar;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText Username, Email, Password;
    Button btn_register;

    FirebaseAuth mAuth;
    DatabaseReference refreence;
   String txt_username;
   String txt_email;
   String txt_password;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Toolbar toolbar = findViewById(R.id.toolBar);
         setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Username = (MaterialEditText) findViewById(R.id.username);
        Email = (MaterialEditText) findViewById(R.id.email);
        Password = (MaterialEditText) findViewById(R.id.password);
        btn_register = (Button) findViewById(R.id.btn_register);

        mAuth=FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               txt_username = Username.getText().toString();
                txt_email = Email.getText().toString();
               txt_password = Password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(RegisterActivity.this, "All fields must be not Empty", Toast.LENGTH_LONG).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
                } else {

                    Register(txt_username, txt_email, txt_password);
                }
            }
        });

    }


    private void Register(final String username, String Email, String password) {

        mAuth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@Nullable Task<AuthResult> task) {

                assert task != null;
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert firebaseUser != null;
                    final   String  userid = firebaseUser.getUid();

                    refreence = FirebaseDatabase.getInstance().getReference("users").child(userid);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("userId", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageUrl","default");
                    hashMap.put("status","offline");
                    hashMap.put("search",username.toLowerCase());

                    refreence.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@Nullable Task<Void> task) {
                            assert task != null;
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                            }
                        }
                    });

                } else {
                   Toast.makeText(RegisterActivity.this, "You can not register", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
