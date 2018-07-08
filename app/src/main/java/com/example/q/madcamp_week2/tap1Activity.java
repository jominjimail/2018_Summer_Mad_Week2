package com.example.q.madcamp_week2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class tap1Activity extends AppCompatActivity {
    ListView listView;
    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tap1_layout);

        Button tap1 = (Button)findViewById(R.id.act1_tap1_btn);
        Button tap2 = (Button)findViewById(R.id.act1_tap2_btn);
        Button tap3 = (Button)findViewById(R.id.act1_tap3_btn);



        tap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                finish();
            }
        });
        tap3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(),tap3Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_left);
                finish();
            }
        });
    }
}

