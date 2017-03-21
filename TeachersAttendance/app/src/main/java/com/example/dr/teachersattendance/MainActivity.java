package com.example.dr.teachersattendance;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity  {
    ImageButton btnShowLocation;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    double latitude;
    double longitude;
    // GPSTracker class
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnShowLocation = (ImageButton) findViewById(R.id.punchin);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            //Extract the data…
            String username = getIntent().getStringExtra("username");
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String fdate = df.format(c.getTime());
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
            Date currentLocalTime = cal.getTime();
            DateFormat date = new SimpleDateFormat("HH:mm");

            String localTime = date.format(currentLocalTime);

            @Override
            public void onClick(View arg0) {
                // create class object

                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    String lat = String.valueOf(gps.getLatitude());
                    String longi = String.valueOf(gps.getLongitude());
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    new BackGround().execute(longi, lat, fdate, username,localTime);
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });
    }
            class BackGround extends AsyncTask<String, String, String> {

                @Override
                protected String doInBackground(String... params) {
                    String username = params[0];
                    String longitude = params[1];
                    String latitude = params[2];
                    String date = params[3];
                    String data="";
                    int tmp;
                    try {
                        URL url = new URL("http://attendance-dr22libraryapp.rhcloud.com/location.php");
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        OutputStream os = httpURLConnection.getOutputStream();
                        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                        String datav= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                                URLEncoder.encode("longitude","UTF-8")+"="+URLEncoder.encode(longitude,"UTF-8")+"&"+
                                URLEncoder.encode("latitude","UTF-8")+"="+URLEncoder.encode(latitude,"UTF-8")+"&"+
                                URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&";
                        bw.write(datav);
                        bw.flush();
                        bw.close();
                        os.close();
                        InputStream is = httpURLConnection.getInputStream();
                        while((tmp=is.read())!=-1){
                            data+= (char)tmp;
                        }
                        is.close();
                        httpURLConnection.disconnect();

                        return data;

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return "Exception: "+e.getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return "Exception: "+e.getMessage();
                    }
                }
            }
    @Override
    public void onBackPressed() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(getApplicationContext(),teachers_login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Toast.makeText(getApplicationContext(), "Logged out successfully !!!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
