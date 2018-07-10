package com.example.madchocho.misea;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class MainActivity extends Activity {
    TextView tv;
    ToggleButton tb;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //if DENIED request Permission
        if(permissionCheck== PackageManager.PERMISSION_DENIED){

            //pop up the dialog box that ask to user permission yes or no  *call back function is onRequestPermissionResult()
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


        } else {
            chkGpsService();
            tv = (TextView) findViewById(R.id.textView2);
            tv.setText("위치정보 미수신중");

            tb = (ToggleButton) findViewById(R.id.toggle1);

            // LocationManager 객체를 얻어온다
            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


            tb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (tb.isChecked()) {
                            tv.setText("수신중..");
                            // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1, // 통지사이의 최소 변경거리 (m)
                                    mLocationListener);
                            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                    100, // 통지사이의 최소 시간간격 (miliSecond)
                                    1, // 통지사이의 최소 변경거리 (m)
                                    mLocationListener);
                        } else {
                            tv.setText("위치정보 미수신중");
                            lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                        }
                    } catch (SecurityException ex) {
                    }
                }
            });
        }

    } // end of onCreate

    @Override
    //this function process the user's answer
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                //if request is cancelled the result arrays are empty

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    chkGpsService();
                    tv = (TextView) findViewById(R.id.textView2);
                    tv.setText("위치정보 미수신중");

                    tb = (ToggleButton) findViewById(R.id.toggle1);

                    // LocationManager 객체를 얻어온다
                    final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


                    tb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (tb.isChecked()) {
                                    tv.setText("수신중..");
                                    // GPS 제공자의 정보가 바뀌면 콜백하도록 리스너 등록하기~!!!
                                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, // 등록할 위치제공자
                                            100, // 통지사이의 최소 시간간격 (miliSecond)
                                            1, // 통지사이의 최소 변경거리 (m)
                                            mLocationListener);
                                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자
                                            100, // 통지사이의 최소 시간간격 (miliSecond)
                                            1, // 통지사이의 최소 변경거리 (m)
                                            mLocationListener);
                                } else {
                                    tv.setText("위치정보 미수신중");
                                    lm.removeUpdates(mLocationListener);  //  미수신할때는 반드시 자원해체를 해주어야 한다.
                                }
                            } catch (SecurityException ex) {
                            }
                        }
                    });
                    Toast.makeText(this, "GPS 접근이 승인되었습니다.", Toast.LENGTH_SHORT).show();

                } else {
                    //permission denied
                    Toast.makeText(this, "GPS 접근이 거절되었습니다. 추가 승인이 필요합니다.", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            //여기서 위치값이 갱신되면 이벤트가 발생한다.
            //값은 Location 형태로 리턴되며 좌표 출력 방법은 다음과 같다.

            Log.d("test", "onLocationChanged, location:" + location);
            double longitude = location.getLongitude(); //경도
            double latitude = location.getLatitude();   //위도
            double altitude = location.getAltitude();   //고도
            float accuracy = location.getAccuracy();    //정확도
            String provider = location.getProvider();   //위치제공자
            //Gps 위치제공자에 의한 위치변화. 오차범위가 좁다.
            //Network 위치제공자에 의한 위치변화
            //Network 위치는 Gps에 비해 정확도가 많이 떨어진다.
            tv.setText("위치정보 : " + provider + "\n위도 : " + longitude + "\n경도 : " + latitude
                    + "\n고도 : " + altitude + "\n정확도 : "  + accuracy);
        }
        public void onProviderDisabled(String provider) {
            // Disabled시
            Log.d("test", "onProviderDisabled, provider:" + provider);
        }

        public void onProviderEnabled(String provider) {
            // Enabled시
            Log.d("test", "onProviderEnabled, provider:" + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // 변경시
            Log.d("test", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);
        }
    };

    private boolean chkGpsService() {

        //GPS가 켜져 있는지 확인함.
        String gpsEnabled = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            //gps가 사용가능한 상태가 아니면
            new AlertDialog.Builder(this).setTitle("GPS 설정").setMessage("GPS가 꺼져 있습니다. \nGPS를 활성화 하시겠습니까?").setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    //GPS 설정 화면을 띄움
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();

        }else if((gpsEnabled.matches(".*gps.*") && gpsEnabled.matches(".*network.*"))) {
            Toast.makeText(getApplicationContext(), "정보를 읽어오는 중입니다.", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
