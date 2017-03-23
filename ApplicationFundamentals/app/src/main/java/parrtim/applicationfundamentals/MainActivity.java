package parrtim.applicationfundamentals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    SMSListAdapter adapter;
    ListView listView;
    ArrayList<SMSInfo> messages;
    String msg = "Main Activity";

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(msg, "OnCreate event");
        retrieveSharePreferences();
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_SMS) == PermissionChecker.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_SMS }, 0);
        }
        else
        {
            mProgress = (ProgressBar) findViewById(R.id.progressBar);

            // Start lengthy operation in a background thread
            new Thread(new Runnable() {
                public void run() {
                    retrieveSMS();
                }
            }).start();
        }
    }

    private void retrieveSMS()
    {
        messages = getSMS();
        adapter = new SMSListAdapter(getApplicationContext(), messages);
        listView = (ListView) findViewById(R.id.messageList);
        runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), SMSReply.class);
                        SMSInfo message = (SMSInfo) parent.getItemAtPosition(position);
                        intent.putExtra("message", message.Message);
                        intent.putExtra("number", message.Number);
                        startActivity(intent);
                    }
                });
                mProgress.setVisibility(View.GONE);
            }
        });
    }

    protected void retrieveSharePreferences()
    {
        SharedPreferences preferences = getSharedPreferences("Settings", 0);
        String theme = preferences.getString("Theme", null);

        if (theme == null)
        {
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("Theme", "DarkAppTheme");
            setTheme(R.style.DarkAppTheme);
        }
        else if (theme.equals("DarkAppTheme"))
        {
            setTheme(R.style.DarkAppTheme);
        }
        else if (theme.equals("LightAppTheme"))
        {
            setTheme(R.style.LightAppTheme);
        }
        else
        {
            Log.d(msg,"Theme was unexpected value: " + theme);
        }
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        String message = intent.getStringExtra("message");
        String number = intent.getStringExtra("number");
        Log.d(msg, message);
        adapter.add(new SMSInfo(number, message));
        super.onNewIntent(intent);
    }

    public ArrayList<SMSInfo> getSMS()
    {
        ArrayList<SMSInfo> sms = new ArrayList<>();
        Cursor cur = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        mProgress.setMax(cur.getCount());

        while (cur.moveToNext())
        {
            mHandler.post(new Runnable() {
                public void run() {
                    mProgress.setProgress(mProgressStatus++);
                }
            });
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add(new SMSInfo(address, body));
        }

        cur.close();
        return sms;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), OtherSettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mProgress = (ProgressBar) findViewById(R.id.progressBar);

                    // Start lengthy operation in a background thread
                    new Thread(new Runnable() {
                        public void run() {
                            retrieveSMS();
                        }
                    }).start();
                    return;
                }
        }
    }
}


