package com.example.tparr.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent serviceIntent = new Intent(getApplicationContext(), MyIntentService.class);
        serviceIntent.putExtra("item", "My book");
        startService(serviceIntent);
    }


}
