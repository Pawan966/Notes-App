package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    private EditText mcreatenotetitle,mcreatenotecontent;
    private FloatingActionButton msavebutton;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mcreatenotetitle=findViewById(R.id.createnotetitle);
        mcreatenotecontent=findViewById(R.id.createnotecontent);
        msavebutton=findViewById(R.id.savenote);

        Toolbar toolbar=findViewById(R.id.createnotetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        msavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mcreatenotetitle.getText().toString();
                String content=mcreatenotecontent.getText().toString();

                if(title.isEmpty() || content.isEmpty()){
                    Toast.makeText(CreateNote.this, "Both fields are required", Toast.LENGTH_SHORT).show();
                }else{
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateNote.this, "Note added Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(CreateNote.this,notes.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(CreateNote.this, "Failed to create note", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}