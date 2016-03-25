package com.example.rustam.gaprojectandroidclient;

/**
 * Created by Rustam on 3/6/2016.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProfileActivity extends Activity {


    SharedPreferences pref;
    String token, grav, oldpasstxt, newpasstxt, usermail;
    WebView web;
    Button chgpass, chgpassfr, cancel, logout, action5W, action50W;
    Dialog dlg;
    EditText oldpass, newpass;
    List<NameValuePair> params;
    String serverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serverName = getString(R.string.serverName);
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        grav = pref.getString("grav", "");
        usermail = pref.getString("usermail", " ");

        if (!usermail.equalsIgnoreCase("user1")) {
            Intent loginactivity = new Intent(ProfileActivity.this, LoginActivity.class);

            startActivity(loginactivity);
            finish();

        }
        setContentView(R.layout.activity_profile);
        //web = (WebView) findViewById(R.id.webView);
        chgpass = (Button) findViewById(R.id.chgbtn);
        logout = (Button) findViewById(R.id.logout);






        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.remove("usermail");
                edit.commit();
                Intent loginactivity = new Intent(ProfileActivity.this, LoginActivity.class);

                startActivity(loginactivity);
                finish();
            }
        });
        action5W = (Button) findViewById(R.id.button5W);
        action5W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("action", "5"));
                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON(serverName + "api/action", params);

                if (json != null) {
                    try {
                        String jsonstr = json.getString("response");
                        if (json.getBoolean("res")) {
                            Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        action50W = (Button) findViewById(R.id.button50W);
        action50W.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("action", "50"));
                ServerRequest sr = new ServerRequest();
                JSONObject json = sr.getJSON(serverName + "api/action", params);

                if (json != null) {
                    try {
                        String jsonstr = json.getString("response");
                        if (json.getBoolean("res")) {
                            Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        // web.getSettings().setUseWideViewPort(true);
        //web.getSettings().setLoadWithOverviewMode(true);
        //web.loadUrl(grav);

        chgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg = new Dialog(ProfileActivity.this);
                dlg.setContentView(R.layout.chgpassword_frag);
                dlg.setTitle("Change Password");
                chgpassfr = (Button) dlg.findViewById(R.id.chgbtn);

                chgpassfr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        oldpass = (EditText) dlg.findViewById(R.id.oldpass);
                        newpass = (EditText) dlg.findViewById(R.id.newpass);
                        oldpasstxt = oldpass.getText().toString();
                        newpasstxt = newpass.getText().toString();
                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("oldpass", oldpasstxt));
                        params.add(new BasicNameValuePair("newpass", newpasstxt));
                        params.add(new BasicNameValuePair("id", token));
                        ServerRequest sr = new ServerRequest();
                        //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
                        JSONObject json = sr.getJSON(serverName + "api/chgpass", params);
                        if (json != null) {
                            try {
                                String jsonstr = json.getString("response");
                                if (json.getBoolean("res")) {

                                    dlg.dismiss();
                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                cancel = (Button) dlg.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dlg.dismiss();
                    }
                });
                dlg.show();
            }
        });


    }


}