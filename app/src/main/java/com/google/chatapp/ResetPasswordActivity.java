package com.google.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText reset_password;
    Button send_btn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

      Toolbar toolbar = findViewById(R.id.bar_layout_reset);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reset_password=findViewById(R.id.send_email);
        send_btn=findViewById(R.id.txt_reset);

        firebaseAuth=FirebaseAuth.getInstance();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String email=reset_password.getText().toString();

              if (email.equals("")){
                  Toast.makeText(ResetPasswordActivity.this,"All fields are required",Toast.LENGTH_LONG).show();
              }
              else{
                  firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()){
                               Toast.makeText(ResetPasswordActivity.this,"Please Check your email",Toast.LENGTH_LONG).show();
                             startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                           }
                           else{
                               String error= Objects.requireNonNull(task.getException()).getMessage();
                               Toast.makeText(ResetPasswordActivity.this,error,Toast.LENGTH_LONG).show();

                           }
                      }
                  });
              }
            }
        });
    }
}
