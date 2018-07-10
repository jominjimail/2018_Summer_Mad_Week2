package com.example.madchocho.misea;

import android.Manifest;
import android.content.Context;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class Instgram extends AppCompatActivity {
    //private static final int GALLERY_CODE = 12;
    private FirebaseStorage storage;
    private Button button;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private EditText editText ,editText2;
    public String name , phone;
    private ChildEventListener mChild;
    private DatabaseReference mDatabase;
    private ListView listView;
    private ArrayAdapter<String> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instgram);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button button1 = (Button)findViewById(R.id.upload);
        Button button2= (Button)findViewById(R.id.board);
        listView = (ListView)findViewById(R.id.listview);
        final ArrayList<Person> m_orders= new ArrayList<>();

        /*권한*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String [] {READ_CONTACTS},0);
        }

        Map<String, String> phone_address = ContactUtil.getAddressBook(this);

        Iterator ite = phone_address.keySet().iterator();

        while(ite.hasNext())
        {
            phone = ite.next().toString();
            name = phone_address.get(phone).toString();
            m_orders.add(new Person(name, phone));
        }

        /*listview처리*/
        PersonAdapter m_adapter = new PersonAdapter(this, R.layout.view_friend_list, m_orders);
        listView.setAdapter(m_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowID)
            {
                doSelectFriend((Person)parent.getItemAtPosition(position));
            }});


        /* 백업하기 버튼 DB push */
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i=0;i<m_orders.size();i++){
                    //System.out.println(m_orders.get(i).Number);
                    name=m_orders.get(i).Name;
                    phone=m_orders.get(i).Number;
                    User user = new User(name,phone);
                    databaseReference.child("message8").push().setValue(user);
                }


            }
        });

        /*실시간 DB board 보여줌*/
        button2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Board.class);
                //intent.putExtra("text",String.valueOf(editText.getText()));
                startActivity(intent);
            }
        });



    }//onCreate

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    public void doSelectFriend(Person p)
    {
        Log.e("####", p.getName() + ", " + p.getNumber());
    }

    private class PersonAdapter extends ArrayAdapter<Person>
    {
        private ArrayList<Person> items;

        public PersonAdapter(Context context, int textViewResourceId, ArrayList<Person> items)
        {
            super(context, textViewResourceId, items);
            this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View v = convertView;
            if (v == null)
            {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.view_friend_list, null);
            }
            Person p = items.get(position);
            if (p != null)
            {
                TextView tt = (TextView) v.findViewById(R.id.name);
                TextView bt = (TextView) v.findViewById(R.id.msg);
                if (tt != null)
                {
                    tt.setText(p.getName());
                }
                if(bt != null)
                {
                    bt.setText("전화번호: "+ p.getNumber());
                }
            }
            return v;
        }
    }

    class Person
    {
        private String Name;
        private String Number;

        public Person(String _Name, String _Number)
        {
            this.Name = _Name;
            this.Number = _Number;
        }

        public String getName()
        {
            return Name;
        }

        public String getNumber()
        {
            return Number;
        }
    }




}
