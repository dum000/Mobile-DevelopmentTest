package parrtim.applicationfundamentals;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import parrtim.applicationfundamentals.SMS.SMSBroadcastReceiver;
import parrtim.applicationfundamentals.SMS.SMSUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    String msg = "Main Activity";
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(msg, "OnCreate event");
        //retrieveSharePreferences();
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame1, new ThreadFragment()).commit();

        if (Telephony.Sms.getDefaultSmsPackage(this) == null || !Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
            // App is not default
            // Show the "not currently set as the default SMS app" interface

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Theme_AppCompat_Light_NoActionBar));
            builder.setMessage("TMS is not set as your default messaging app. Do you want to set it default?")
                    .setCancelable(false)
                    .setTitle("Alert!")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @TargetApi(19)
                        public void onClick(DialogInterface dialog, int id) {

                            Intent intent =
                                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);

                            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                    getPackageName());

                            startActivity(intent);

                        }
                    });
            builder.show();
        }
    }

    protected void retrieveSharePreferences()
    {
//        SharedPreferences preferences = getSharedPreferences("Settings", 0);
//        String theme = preferences.getString("Theme", null);
//
//        if (theme == null)
//        {
//            SharedPreferences.Editor edit = preferences.edit();
//            edit.putString("Theme", "DarkAppTheme");
//            setTheme(R.style.DarkAppTheme);
//        }
//        else if (theme.equals("DarkAppTheme"))
//        {
//            setTheme(R.style.DarkAppTheme);
//        }
//        else if (theme.equals("LightAppTheme"))
//        {
//            setTheme(R.style.LightAppTheme);
//        }
//        else
//        {
//            Log.d(msg,"Theme was unexpected value: " + theme);
//        }
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle bundle = new Bundle();
        bundle.putString("address", intent.getStringExtra("number"));

        ConversationFragment fragment = new ConversationFragment();
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame1, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        final android.support.v4.app.Fragment fragment;

        if (id == R.id.conversations) {
            fragment = new ConversationFragment();
        } else if (id == R.id.inbox) {
            fragment = new InboxFragment();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ((InboxFragment)fragment).Filter(newText);
                    return true;
                }
            });
        } else  {
            fragment = new ThreadFragment();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame1, fragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}


