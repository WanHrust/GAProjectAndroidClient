package com.example.rustam.gaprojectandroidclient;

/**
 * Created by Rustam on 3/6/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends Activity {
    EditText email, password, firstname, lastname, city, street;
    Button login, register;
    String emailtxt, passwordtxt, firstnametxt, lastnametxt, citytxt, streettxt;
    List<NameValuePair> params;
    String serverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        serverName = getString(R.string.serverName);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        city = (EditText) findViewById(R.id.city);
        street = (EditText) findViewById(R.id.street);
        register = (Button) findViewById(R.id.registerbtn);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regactivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(regactivity);
                finish();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                emailtxt = email.getText().toString();
                passwordtxt = password.getText().toString();
                firstnametxt = firstname.getText().toString();
                lastnametxt = lastname.getText().toString();
                citytxt = city.getText().toString();
                streettxt = street.getText().toString();
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", emailtxt));
                params.add(new BasicNameValuePair("password", passwordtxt));
                params.add(new BasicNameValuePair("firstname", firstnametxt));
                params.add(new BasicNameValuePair("lastname", lastnametxt));
                params.add(new BasicNameValuePair("city", citytxt));
                params.add(new BasicNameValuePair("street", streettxt));

                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON(serverName + "register", params);
                //JSONObject json = sr.getJSON("http://192.168.56.1:8080/register",params);

                if (json != null) {
                    try {
                        String jsonstr = json.getString("response");

                        Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_LONG).show();

                        Log.d("Hello", jsonstr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}