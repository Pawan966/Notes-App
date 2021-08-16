package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notedetail extends AppCompatActivity {

    private TextView mnotedetailtitle;
    private TextView mnotedetailcontent;
    FloatingActionButton mgotoeditnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetail);
        mnotedetailtitle=findViewById(R.id.notedetailtitle);
        mnotedetailcontent=findViewById(R.id.notedetailcontent);
        mgotoeditnote=findViewById(R.id.gotoeditnote);
        Toolbar toolbar=findViewById(R.id.notedetailtoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data=getIntent();

        mgotoeditnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),editnote.class);
                intent.putExtra("title",data.getStringExtra("title"));
                intent.putExtra("content",data.getStringExtra("content"));
                intent.putExtra("noteid",data.getStringExtra("noteid"));
                finish();
                v.getContext().startActivity(intent);
            }
        });

        mnotedetailtitle.setText(data.getStringExtra("title"));
        mnotedetailcontent.setText(data.getStringExtra("content"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}