 package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

 public class MainActivity extends AppCompatActivity {

    private EditText mloginemail,mloginpassword;
    private Button mlogin,msignupmain;
    private TextView mforgotpassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        mloginemail=findViewById(R.id.loginemail);
        mloginpassword=findViewById(R.id.loginpassword);
        mlogin=findViewById(R.id.login);
        msignupmain=findViewById(R.id.signupmain);
        mforgotpassword=findViewById(R.id.forgotpassword);
        mprogress=findViewById(R.id.progress);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this,notes.class));
        }

        mforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,forgetpassword.class);
                startActivity(intent);
            }
        });

        msignupmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,signup.class);
                startActivity(intent);
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mloginemail.getText().toString().trim();
                String password=mloginpassword.getText().toString().trim();

                if(mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }else{

                    mprogress.setVisibility(View.VISIBLE);
                    // login the user
                    firebaseAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful()){
                                 checkemailverification();
                             }else{
                                 Toast.makeText(MainActivity.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                                 mprogress.setVisibility(View.INVISIBLE);
                             }
                        }
                    });
                }
            }
        });
    }

    private void checkemailverification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true){
            Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,notes.class));
        }else{
            mprogress.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}