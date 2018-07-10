package com.example.madchocho.misea;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Instgram extends AppCompatActivity {
    //private static final int GALLERY_CODE = 12;
    private FirebaseStorage storage;
    private Button button;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private EditText editText ,editText2;
    public String name , email;
    private ChildEventListener mChild;
    private DatabaseReference mDatabase;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    List<Object> Array = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instgram);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button button1 = (Button)findViewById(R.id.upload);
        editText=(EditText)findViewById(R.id.name);
        editText2=(EditText)findViewById(R.id.email);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = editText.getText().toString();
                email=editText2.getText().toString();
                User user = new User(name,email);

                databaseReference.child("message6").push().setValue(user);
            }
        });



    }//onCreate

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }




}
