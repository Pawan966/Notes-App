package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgetpassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecover;
    private TextView mgobacktologin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        getSupportActionBar().hide();

        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecover=findViewById(R.id.passwordrecover);
        mgobacktologin=findViewById(R.id.gobacktologin);
        firebaseAuth=FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(forgetpassword.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordrecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=mforgotpassword.getText().toString().trim();
                if(mail.isEmpty()){
                    Toast.makeText(forgetpassword.this, "Enter the email", Toast.LENGTH_SHORT).show();
                }else{
                    // send recover password mail
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful()){
                                 Toast.makeText(forgetpassword.this, "email sent, please recover your password", Toast.LENGTH_SHORT).show();
                                 finish();
                                 startActivity(new Intent(forgetpassword.this,MainActivity.class));
                             }else{
                                 Toast.makeText(forgetpassword.this, "Account does not exist", Toast.LENGTH_SHORT).show();
                             }
                        }
                    });
                }
            }
        });
    }
}