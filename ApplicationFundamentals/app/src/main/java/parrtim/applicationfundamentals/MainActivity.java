package parrtim.applicationfundamentals;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    SMSListAdapter adapter;
    ListView listView;
    ArrayList<SMSInfo> messages;
    String msg = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(msg, "OnCreate event");
        retrieveSharePreferences();
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_SMS) == PermissionChecker.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.READ_SMS }, 0);
        }
        else
        {
            retrieveSMSInbox();
        }
    }

    private void retrieveSMSInbox()
    {
        messages = new ArrayList<>();
        SMSUtil.getSMSInbox(getApplicationContext());
        adapter = new SMSListAdapter(getApplicationContext(), messages);
//        listView = (ListView) findViewById(R.id.messageList);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(getApplicationContext(), SMSReply.class);
//                SMSInfo message = (SMSInfo) parent.getItemAtPosition(position);
//                intent.putExtra("message", message.Message);
//                intent.putExtra("number", message.Number);
//                startActivity(intent);
//            }
//        });
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
                    retrieveSMSInbox();
                }
        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.conversations) {

        } else if (id == R.id.inbox) {

        } else if (id == R.id.sent) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


