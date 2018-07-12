package com.example.q.madcamp_week2;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class tap3Activity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView tv;
    // 웹사이트 주소를 저장할 변수
    String urlAddress ;
    Handler handler = new Handler(); // 화면에 그려주기 위한 객체
    EditText master;
    EditText repo;


    LoginButton facebook_login;


    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tap3_layout);



        /*


        Button tap1 = (Button) findViewById(R.id.act3_tap1_btn);
        Button tap2 = (Button) findViewById(R.id.act3_tap2_btn);
        Button tap3 = (Button) findViewById(R.id.act3_tap3_btn);
        */
        //Button parse_btn = (Button) findViewById(R.id.parse_button);
        master = (EditText) findViewById(R.id.master_text);
        repo = (EditText) findViewById(R.id.repo_text);
        textView = (TextView) findViewById(R.id.textView1);



        final ArrayList nameArrayList = new ArrayList<String>();
        final ArrayList commitNum = new ArrayList<Integer>();

        /*


        tap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), tap1Activity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();


            }
        });

        tap2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(myIntent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                finish();
            }
        });
        */
        master = (EditText) findViewById(R.id.master_text);
        repo = (EditText)findViewById(R.id.repo_text);


        tv = (TextView)findViewById(R.id.textView1);
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button b = (Button)findViewById(R.id.button1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadHtml(); // 웹에서 html 읽어오기
            }
        });
    } // end of onCreate

    void loadHtml() { // 웹에서 html 읽어오기
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();

                try {
                    urlAddress = "https://api.github.com/repos/" + master.getText().toString() + "/" + repo.getText().toString() + "/contributors";
                    URL url = new URL(urlAddress);
                    HttpURLConnection conn =
                            (HttpURLConnection) url.openConnection();// 접속
                    if (conn != null) {
                        conn.setConnectTimeout(2000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode()
                                == HttpURLConnection.HTTP_OK) {
                            //    데이터 읽기
                            BufferedReader br
                                    = new BufferedReader(new InputStreamReader
                                    (conn.getInputStream(), "euc-kr"));//"utf-8"
                            while (true) {
                                String line = br.readLine();
                                if (line == null) break;
                                sb.append(line + "\n");
                            }
                            br.close(); // 스트림 해제
                        }
                        conn.disconnect(); // 연결 끊기
                    }
                    // 값을 출력하기
                    Log.d("test", sb.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String temp = sb.toString();
                            String[] users = temp.split("[,\\{}]");
                            String parsed_data = "";

                            //tv.setText(users[0]);
                            ArrayList<String> userArray = new ArrayList<>();

                            for (String temp1 : users) {

                                userArray.add(temp1);


                            }
                            for (int i = 0; i < userArray.size(); i++) {
                                if (userArray.get(i).contains("login")) {
                                    parsed_data += "유저 ";

                                    parsed_data += userArray.get(i).substring(9, userArray.get(i).length() - 1);

                                    parsed_data += " 는 지금까지 ";


                                } else if (userArray.get(i).contains("contributions")) {
                                    parsed_data += userArray.get(i).substring(16);
                                    parsed_data += "개의 commit 을 올렸습니다.";
                                    parsed_data += "\n\n";
                                }

                            }
                            tv.setText(parsed_data);


                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작
    }





}
