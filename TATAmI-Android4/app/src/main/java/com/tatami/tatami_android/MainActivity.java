package com.tatami.tatami_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.ui.AgentsActivity;
import android.ui.backend.Backend;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static Context context;

    public static Backend backend;


    void onAgentsManagerClick(View v){
        Intent agentsManagerActivity = new Intent(
                MainActivity.this, AgentsActivity.class);
        startActivityForResult(agentsManagerActivity, 0);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        /*
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading backend");
        dialog.setCancelable(false);
        dialog.show();
*/
        new BackgroundThread(context).start();

        backend = new Backend();
/*
        dialog.hide();
*/


    }
}
