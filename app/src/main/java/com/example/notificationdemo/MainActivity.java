package com.example.notificationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by boby on 2016/12/3.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void manager(View v) {
        startActivity(new Intent(this, ManagerActivity.class));
    }

    public void http(View v) {
        startActivity(new Intent(this, HttpActivity.class));
    }
}
