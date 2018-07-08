package com.example.madchocho.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_FROM_PHOTO = 2;
    //private ImageView imgview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        //imgview = (ImageView) findViewById(R.id.imageView1);


        Button button1= (Button) findViewById(R.id.contact);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent1 = new Intent(Main4Activity.this,
                        TestAddressBook.class);
                startActivity(myIntent1);
            }
        });


        Button button3= (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent3 = new Intent(Main4Activity.this,
                        Gps.class);
                startActivity(myIntent3);
            }


/*
                myIntent2.setType("image/*");
                myIntent2.setAction(myIntent2.ACTION_GET_CONTENT);

                try{
                    myIntent2.putExtra("return-data",true);
                    startActivityForResult(myIntent2.createChooser(myIntent2,"Complete action using"),PICK_FROM_PHOTO);
                }catch (ActivityNotFoundException e){
                    // do nothing
                }
*/

        });
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(),
                "Button Clicked!", Toast.LENGTH_SHORT).show();
    }

}
