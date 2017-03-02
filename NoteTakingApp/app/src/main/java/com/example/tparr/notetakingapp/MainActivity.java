package com.example.tparr.notetakingapp;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart()
    {}
}
