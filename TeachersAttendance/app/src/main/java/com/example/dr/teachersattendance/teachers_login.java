package com.example.dr.teachersattendance;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DR on 1/3/2017.
 */
public class teachers_login extends AppCompatActivity {
    EditText username,password;
    String Username,Password;
    String USERNAME = null, PASSWORD = null,NAME=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teachers_login);
        username = (EditText) findViewById(R.id.main_username);
        password = (EditText) findViewById(R.id.main_password);
    }
    public void main_login(View v){

        if (  ( !username.getText().toString().equals("")) && ( !password.getText().toString().equals("")) )
        {

            Username = username.getText().toString();
            Password = password.getText().toString();
            BackGround b = new BackGround();
            b.execute(Username, Password);
        }
        else if ( ( !username.getText().toString().equals("")) )
        {
            Toast.makeText(getApplicationContext(),
                    "Password field empty", Toast.LENGTH_SHORT).show();
        }
        else if ( ( !password.getText().toString().equals("")) )
        {
            Toast.makeText(getApplicationContext(),
                    "Username field empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    "Username and Password fields are empty", Toast.LENGTH_SHORT).show();
        }
    }
    class BackGround extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String password = params[1];
            String data = "";
            int tmp;
            try {
                URL url = new URL("http://attendance-dr22libraryapp.rhcloud.com/login.php");
                String urlParams = "username=" + name + "&password=" + password;
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());

                os.flush();
                os.close();
                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }
                is.close();
                httpURLConnection.disconnect();
                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s){
            String err = null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                NAME = user_data.getString("name");
                USERNAME = user_data.getString("username");
                PASSWORD = user_data.getString("password");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("name", NAME);
            i.putExtra("username", USERNAME);
            i.putExtra("password", PASSWORD);
            i.putExtra("err", err);
            startActivity(i);
        }
    }
}
