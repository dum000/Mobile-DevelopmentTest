package com.example.tparr.myapplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class MyIntentService extends IntentService
{
    private static final String TAG = "MyIntentService";
    private ArrayList<String> backpack = new ArrayList<>();

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");
        backpack.add(intent.getStringExtra("item"));
        Log.d(TAG, backpack.get(0));
        Log.d(TAG, "process ID: " + android.os.Process.myPid() + " thread ID: " + android.os.Process.myTid());
    }
}
