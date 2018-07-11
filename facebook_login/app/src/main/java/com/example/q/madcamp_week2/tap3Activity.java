package com.example.q.madcamp_week2;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


public class tap3Activity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CallbackManager callbackManager;
    Elements contents;
    Document doc = null;
    String Top10;//결과를 저장할 문자열변수
    String tempName;


    LoginButton facebook_login;
    EditText master;
    EditText repo;

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tap3_layout);


        Button tap1 = (Button) findViewById(R.id.act3_tap1_btn);
        Button tap2 = (Button) findViewById(R.id.act3_tap2_btn);
        Button tap3 = (Button) findViewById(R.id.act3_tap3_btn);
        Button parse_btn = (Button) findViewById(R.id.parse_button);
        master = (EditText) findViewById(R.id.master_name);
        repo = (EditText) findViewById(R.id.repo_name);
        textView = (TextView) findViewById(R.id.textBox);
        final ArrayList nameArrayList = new ArrayList<String>();
        final ArrayList commitNum = new ArrayList<Integer>();


        /*


        try {
            //https://github.com/jominjimail/2018_Summer_Mad_Week2/graphs/contributors


            Document rawData = Jsoup.connect("https://www.naver.com/").get();

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "connection failed", Toast.LENGTH_SHORT).show();
        }
        */

        parse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask() {//AsyncTask객체 생성
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            nameArrayList.clear();
                            commitNum.clear();

                            doc = Jsoup.connect("https://github.com/"+master.getText().toString()+"/"+repo.getText().toString()+"/commits/master").get(); //naver페이지를 불러옴
                            contents = doc.select("a.commit-author.tooltipped.tooltipped-s.user-mention");//셀렉터로 span태그중 class값이 ah_k인 내용을 가져옴
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Top10 = "";
                        int cnt = 0;//숫자를 세기위한 변수
                        for(Element element: contents) {
                            cnt++;
                            tempName = element.text();
                            if (!nameArrayList.contains(tempName)){
                                nameArrayList.add(tempName);
                                commitNum.add(1);
                            }
                            else{//name is already in the nameArrayList
                                int indexN = nameArrayList.indexOf(tempName);
                                commitNum.set(indexN,(Integer)commitNum.get(indexN)+1);



                            }
                            /*


                            Top10 += cnt+". "+element.text() + "\n";
                            if(cnt == 100)//10위까지 파싱하므로
                                break;
                                */
                        }
                        for (int i=0;i<nameArrayList.size();i++){
                            Top10 += "Uploader "+nameArrayList.get(i) +"'s total commits are  "+ Integer.toString((Integer)commitNum.get(i))+"\n";
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        Log.i("TAG",""+Top10);
                        textView.setText(Top10);
                    }
                }.execute();
            }
        });


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


        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();

        facebook_login = findViewById(R.id.facebook_login);

        facebook_login.setReadPermissions("email");

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(TAG, "onSucces LoginResult=" + loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d(TAG, "onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d(TAG, "onError");
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
