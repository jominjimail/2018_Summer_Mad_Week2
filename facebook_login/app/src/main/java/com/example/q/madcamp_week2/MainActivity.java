package com.example.q.madcamp_week2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {
    public static ArrayList<Model_images> al_images = new ArrayList<>();
    boolean boolean_folder;

    private FirebaseStorage storage;
    private Button button;
    private FirebaseDatabase database;
    private EditText editText ,editText2;
    public String name , phone;
    private ChildEventListener mChild;
    private DatabaseReference mDatabase;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<User> users = new ArrayList<>();
    private List<String> uidLists = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_gallery);

        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        Button tap1 = (Button) findViewById(R.id.act2_tap1_btn);
        Button tap2 = (Button) findViewById(R.id.act2_tap2_btn);
        Button tap3 = (Button) findViewById(R.id.act2_tap3_btn);

        Button button1 = (Button)findViewById(R.id.upload);
        Button button2= (Button)findViewById(R.id.board);
        Button button3 = (Button)findViewById(R.id.sync);
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
        final PersonAdapter m_adapter = new PersonAdapter(this, R.layout.view_friend_list, m_orders);
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
                    mDatabase.child("message8").push().setValue(user);
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

        /*내장 핸드폰 연락처 DB 업데이트 동기화*/
        button3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                database.getReference().child("message8").removeValue();
                for(int i=0;i<m_orders.size();i++){
                    //System.out.println(m_orders.get(i).Number);
                    name=m_orders.get(i).Name;
                    phone=m_orders.get(i).Number;
                    User user = new User(name,phone);
                    mDatabase.child("message8").push().setValue(user);
                }

            }
        });



        tap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), tap1Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_right);
                finish();


            }
        });

        tap3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), tap3Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                finish();


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
