package com.example.q.madcamp_week2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Board extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter Adapter;
    private List<User> users = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        database = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerV);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final BoardRecyclerViewAdapter boardRecyclerViewAdapter = new BoardRecyclerViewAdapter();
        recyclerView.setAdapter(boardRecyclerViewAdapter);

        /*database value change detect in real time*/
        database.getReference().child("message8").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                uidLists.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    String uidKey = snapshot.getKey();
                    users.add(user);
                    uidLists.add(uidKey);
                }

                boardRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int i) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board,parent,false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder( RecyclerView.ViewHolder holder1, final int i) {
            ((CustomViewHolder)holder1).textView1.setText(users.get(i).name);
            ((CustomViewHolder)holder1).textView2.setText(users.get(i).phone);

            ((CustomViewHolder)holder1).deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete_content(i);
                }
            });


        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        private void delete_content(int position){
            database.getReference().child("message8").child(uidLists.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(Board.this, "삭제가 완료 되었습니다.", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView textView1;
            TextView textView2;
            ImageView deleteButton;

            public CustomViewHolder(View view) {
                super(view);
                textView1 = (TextView) view.findViewById(R.id.item_textView1);
                textView2 = (TextView) view.findViewById(R.id.item_textView2);
                deleteButton = (ImageView)view.findViewById(R.id.item_delete);
            }
        }
    }
}

