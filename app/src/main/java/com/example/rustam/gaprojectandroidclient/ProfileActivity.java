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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends Activity {


    SharedPreferences pref;
    String token, grav, oldpasstxt, newpasstxt, userLogged, userEmail;
    WebView web;
    TextView tvMain, tvProgressPercent, tvRequiredAmaunt;
    Button chgpass, chgpassfr, cancel, logout, btnSubmit, action50W, btnNotHome;
    LinearLayout llLightBulb, llOven, llBoiler, llOther, llNotHome;
    CheckBox chkBoxLightBulb, chkBoxOven, chkBoxBoiler, chkBoxOther, chkBoxNotHome;
    int storedChoicePower;
    Dialog dlg;
    EditText oldpass, newpass;
    List<NameValuePair> params;
    String serverName;
    int i = 0;

    private void progressThread() {

        new Thread() {
            public void run() {
                while (!interrupted()) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                tvProgressPercent.setText(i++ % 100 + "%");
                            }
                        });
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        serverName = getString(R.string.serverName);
        super.onCreate(savedInstanceState);


        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        token = pref.getString("token", "");
        grav = pref.getString("grav", "");
        userLogged = pref.getString("userLogged", " ");
        userEmail = pref.getString("userEmail", "");

        if (!userLogged.equalsIgnoreCase("user1")) {
            Intent loginactivity = new Intent(ProfileActivity.this, LoginActivity.class);

            startActivity(loginactivity);
            finish();

        }
        setContentView(R.layout.activity_profile);
        //web = (WebView) findViewById(R.id.webView);
        chgpass = (Button) findViewById(R.id.chgbtn);
        logout = (Button) findViewById(R.id.logout);
        tvMain = (TextView) findViewById(R.id.textViewMain);

        tvProgressPercent = (TextView) findViewById(R.id.tvProgressPercent);
        tvRequiredAmaunt = (TextView) findViewById(R.id.tvRequiredAmount);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnNotHome = (Button) findViewById(R.id.btnNotHome);
        //action50W = (Button) findViewById(R.id.button50W);

        llOven = (LinearLayout) findViewById(R.id.llOven);
        chkBoxOven = (CheckBox) findViewById(R.id.chkBoxOven);

        llBoiler = (LinearLayout) findViewById(R.id.llBoiler);
        chkBoxBoiler = (CheckBox) findViewById(R.id.chkBoxBoiler);

        llOther = (LinearLayout) findViewById(R.id.llOther);
        chkBoxOther = (CheckBox) findViewById(R.id.chkBoxOther);

        llLightBulb = (LinearLayout) findViewById(R.id.llLightBulb);
        chkBoxLightBulb = (CheckBox) findViewById(R.id.chkBoxLightBulb);


        Bundle bundle = getIntent().getExtras();
        String gcmMessage = "";
        tvMain.setText("Penguins are safe!");
        if (bundle != null) {
            gcmMessage = bundle.getString("gcmmessage");
        }
        if (gcmMessage != null && !gcmMessage.isEmpty()) {

            String requiedAmount = bundle.getString("gcmrequiredamount");
            tvMain.setText(gcmMessage);
            tvRequiredAmaunt.setText(requiedAmount);
            if (!btnSubmit.isEnabled()) btnSubmit.setEnabled(true);
            if (btnSubmit.getVisibility() != View.VISIBLE) btnSubmit.setVisibility(View.VISIBLE);
//            if (!action50W.isEnabled()) action50W.setEnabled(true);
//            if (action50W.getVisibility() != View.VISIBLE)
//                action50W.setVisibility(View.VISIBLE);


        } else {
            tvMain.setText("Penguins are safe!");
            if (btnSubmit.isEnabled()) btnSubmit.setEnabled(false);
            if (btnSubmit.getVisibility() == View.VISIBLE) btnSubmit.setVisibility(View.GONE);
//            if (action50W.isEnabled()) action50W.setEnabled(false);
////            if (action50W.getVisibility() == View.VISIBLE) action50W.setVisibility(View.GONE);
        }



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("token", "");
                edit.remove("userLogged");
                edit.commit();
                Intent loginactivity = new Intent(ProfileActivity.this, LoginActivity.class);

                startActivity(loginactivity);
                finish();
            }
        });

        llLightBulb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chkBoxLightBulb.isChecked()) {
                    chkBoxLightBulb.setChecked(true);
                } else {
                    chkBoxLightBulb.setChecked(false);
                }
            }
        });

        llOven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chkBoxOven.isChecked()) {
                    chkBoxOven.setChecked(true);
                } else {
                    chkBoxOven.setChecked(false);
                }
            }
        });
        llBoiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!chkBoxBoiler.isChecked()) {
                    chkBoxBoiler.setChecked(true);
                } else {
                    chkBoxBoiler.setChecked(false);
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int totalPower = 0;

                if (chkBoxOven.isChecked()) totalPower += 500;
                if (chkBoxLightBulb.isChecked()) totalPower += 5;
                if (chkBoxBoiler.isChecked()) totalPower += 1000;

                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("action", String.valueOf(totalPower)));
                params.add(new BasicNameValuePair("userEmail", userEmail));
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


                if (btnSubmit.isEnabled()) btnSubmit.setEnabled(false);
                if (btnSubmit.getVisibility() == View.VISIBLE) btnSubmit.setVisibility(View.GONE);
//                if (action50W.isEnabled()) action50W.setEnabled(false);
//                if (action50W.getVisibility() == View.VISIBLE) action50W.setVisibility(View.GONE);

                tvMain.setText("Thank you. Penguins feel safer now!");

            }
        });

        btnNotHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("action", "0"));
                params.add(new BasicNameValuePair("userEmail", userEmail));
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


                if (btnSubmit.isEnabled()) btnSubmit.setEnabled(false);
                if (btnSubmit.getVisibility() == View.VISIBLE) btnSubmit.setVisibility(View.GONE);
//                if (action50W.isEnabled()) action50W.setEnabled(false);
//                if (action50W.getVisibility() == View.VISIBLE) action50W.setVisibility(View.GONE);

                tvMain.setText("Thank you. Penguins feel safer now!");

            }
        });

        // web.getSettings().setUseWideViewPort(true);
        //web.getSettings().setLoadWithOverviewMode(true);
        //web.loadUrl(grav);

//        chgpass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dlg = new Dialog(ProfileActivity.this);
//                dlg.setContentView(R.layout.chgpassword_frag);
//                dlg.setTitle("Change Password");
//                chgpassfr = (Button) dlg.findViewById(R.id.chgbtn);
//
//                chgpassfr.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        oldpass = (EditText) dlg.findViewById(R.id.oldpass);
//                        newpass = (EditText) dlg.findViewById(R.id.newpass);
//                        oldpasstxt = oldpass.getText().toString();
//                        newpasstxt = newpass.getText().toString();
//                        params = new ArrayList<NameValuePair>();
//                        params.add(new BasicNameValuePair("oldpass", oldpasstxt));
//                        params.add(new BasicNameValuePair("newpass", newpasstxt));
//                        params.add(new BasicNameValuePair("id", token));
//                        ServerRequest sr = new ServerRequest();
//                        //    JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/chgpass",params);
//                        JSONObject json = sr.getJSON(serverName + "api/chgpass", params);
//                        if (json != null) {
//                            try {
//                                String jsonstr = json.getString("response");
//                                if (json.getBoolean("res")) {
//
//                                    dlg.dismiss();
//                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getApplication(), jsonstr, Toast.LENGTH_SHORT).show();
//
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                });
//                cancel = (Button) dlg.findViewById(R.id.cancelbtn);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dlg.dismiss();
//                    }
//                });
//                dlg.show();
//            }
//        });

        progressThread();
    }

}