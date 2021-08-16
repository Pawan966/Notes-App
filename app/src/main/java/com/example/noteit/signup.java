package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText msignupemail;
    private EditText msignuppassword;
    private Button msignup;
    private TextView mgotologin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();

        msignupemail=findViewById(R.id.signupemail);
        msignuppassword=findViewById(R.id.signuppassword);
        msignup=findViewById(R.id.signup);
        mgotologin=findViewById(R.id.gotologin);
        firebaseAuth=FirebaseAuth.getInstance();

        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,MainActivity.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=msignupemail.getText().toString().trim();
                String password=msignuppassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(signup.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else if(password.length()<7){
                    Toast.makeText(signup.this, "Password should be atleast 7 characters long", Toast.LENGTH_SHORT).show();
                }else{
                    // register user
                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(signup.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            }else{
                                Toast.makeText(signup.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    // send email verification
    private void sendEmailVerification(){

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(signup.this, "Verification Email is sent, Verify and Log In", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signup.this,MainActivity.class));
                }
            });
        }else{
            Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
        }
    }
}