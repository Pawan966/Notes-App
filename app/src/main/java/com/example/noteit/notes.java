package com.example.noteit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class notes extends AppCompatActivity {

    private FloatingActionButton mcreatenote;
    FirebaseAuth firebaseAuth;
    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        getSupportActionBar().setTitle("All Notes");

        mcreatenote=findViewById(R.id.createnote);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        mcreatenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(notes.this,CreateNote.class));
            }
        });

        Query query=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        noteadapter=new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(NoteViewHolder holder, int position, firebasemodel model) {

                ImageView menupopup=holder.itemView.findViewById(R.id.menupopup);

                int colorcode=getRandomColor();
                holder.mnote.setBackgroundColor(holder.itemView.getResources().getColor(colorcode,null));

                holder.notetitle.setText(model.getTitle());
                holder.notecontent.setText(model.getContent());

                String docId=noteadapter.getSnapshots().getSnapshot(position).getId();

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // open note detail activity
                        Intent intent=new Intent(v.getContext(),notedetail.class);
                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("content",model.getContent());
                        intent.putExtra("noteid",docId);
                        v.getContext().startActivity(intent);
                    }
                });

                menupopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                Intent intent=new Intent(v.getContext(),editnote.class);
                                intent.putExtra("title",model.getTitle());
                                intent.putExtra("content",model.getContent());
                                intent.putExtra("noteid",docId);
                                v.getContext().startActivity(intent);
                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(docId);
                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(notes.this, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull  Exception e) {
                                        Toast.makeText(notes.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return false;
                            }
                        });

                        popupMenu.show();
                    }
                });
            }

            @Override
            public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview=findViewById(R.id.list);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteadapter);
        mrecyclerview.setItemAnimator(null);
    }



    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetitle,notecontent;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle=itemView.findViewById(R.id.notetitle);
            notecontent=itemView.findViewById(R.id.notecontent);
            mnote=itemView.findViewById(R.id.note);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout:
               firebaseAuth.signOut();
               finish();
               startActivity(new Intent(notes.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteadapter!=null){
            noteadapter.stopListening();
        }
    }

    private int getRandomColor(){
        List<Integer> colorcode=new ArrayList<>();
        colorcode.add(R.color.gray);
        colorcode.add(R.color.green);
        colorcode.add(R.color.pink);
        colorcode.add(R.color.lightgreen);
        colorcode.add(R.color.skyblue);
        colorcode.add(R.color.color1);
        colorcode.add(R.color.color2);
        colorcode.add(R.color.color3);
        colorcode.add(R.color.color4);
        colorcode.add(R.color.color5);

        Random random=new Random();
        int number=random.nextInt(colorcode.size());
        return colorcode.get(number);
    }
    
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}