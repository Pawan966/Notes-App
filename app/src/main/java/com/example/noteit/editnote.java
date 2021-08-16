package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class editnote extends AppCompatActivity {

    Intent data;
    private EditText meditnotetitle,meditnotecontent;
    private FloatingActionButton msaveeditnote;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnote);

        meditnotetitle=findViewById(R.id.editnotetitle);
        meditnotecontent=findViewById(R.id.editnotecontent);
        msaveeditnote=findViewById(R.id.saveeditnote);

        data=getIntent();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        Toolbar toolbar=findViewById(R.id.editnotetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        msaveeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtitle=meditnotetitle.getText().toString();
                String newcontent=meditnotecontent.getText().toString();

                if(newtitle.isEmpty() || newcontent.isEmpty()){
                    Toast.makeText(editnote.this, "Both fields required", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(data.getStringExtra("noteid"));
                    Map<String,Object> note=new HashMap<>();
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(editnote.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(editnote.this,notes.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editnote.this, "Failed to update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        String notetitle=data.getStringExtra("title");
        String notecontent=data.getStringExtra("content");
        meditnotetitle.setText(notetitle);
        meditnotecontent.setText(notecontent);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}