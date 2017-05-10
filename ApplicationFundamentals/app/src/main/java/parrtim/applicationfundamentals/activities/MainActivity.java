package parrtim.applicationfundamentals.activities;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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
import android.view.View;

import java.util.Objects;

import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.adapters.SuggestionAdapter;
import parrtim.applicationfundamentals.database.DictionaryOpenHelper;
import parrtim.applicationfundamentals.fragments.ConversationFragment;
import parrtim.applicationfundamentals.fragments.InboxFragment;
import parrtim.applicationfundamentals.fragments.SMSMessageParentFragment;
import parrtim.applicationfundamentals.fragments.ThreadFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    String msg = "Main Activity";
    SearchView searchView;
    DictionaryOpenHelper database;
    SuggestionAdapter suggestions;
    Cursor suggestionCursor;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(msg, "OnCreate event");
        //retrieveSharePreferences();
        setContentView(R.layout.activity_main);
        database = new DictionaryOpenHelper(getApplicationContext());
        suggestionCursor = database.GetSearches("");
        suggestions = new SuggestionAdapter(getApplicationContext(), suggestionCursor);
        handler = new Handler();


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

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE))
        {
            ConversationFragment conversationFragment = new ConversationFragment();
            fragmentManager.beginTransaction().replace(R.id.frameRight, conversationFragment).commit();
        }
        else
        {
            fragmentManager.beginTransaction().replace(R.id.frame1, new ThreadFragment()).commit();
        }

        if (Telephony.Sms.getDefaultSmsPackage(this) == null || !Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
            // App is not default

            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Theme_AppCompat_Light_NoActionBar));
            builder.setMessage("TMS is not set as your default messaging app. Do you want to set it default?")
                    .setCancelable(false)
                    .setTitle("Alert!")
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) { }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @TargetApi(19)
                        public void onClick(DialogInterface dialog, int id)
                        {
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

        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameRight, fragment)
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame1, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        setupSearchView();
        return true;
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        ComponentName componentName = getComponentName();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(componentName);
        searchView.setSearchableInfo(searchableInfo);
        searchView.setSuggestionsAdapter(suggestions);

        LoadFragment(R.id.threads);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        return LoadFragment(id);
    }

    private boolean LoadFragment(int id) {
        final Fragment fragment;

        if (id == R.id.conversations) {
            fragment = new ConversationFragment();
        } else if (id == R.id.inbox) {
            fragment = new InboxFragment();
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                final Fragment clickedFragment = new SMSMessageParentFragment();
                @Override
                public boolean onSuggestionClick(int position) {
                    if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE))
                    {
                        searchView.setQuery(suggestions.GetItem(position), false);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameRight, clickedFragment, null)
                                .addToBackStack(null)
                                .commit();
                    }
                    else {
                        searchView.setQuery(suggestions.GetItem(position), true);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame1, clickedFragment, null)
                                .addToBackStack(null)
                                .commit();
                    }
                    return true;
                }

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(final String searchText) {
                    ((InboxFragment)fragment).Filter(searchText);
                    if (!Objects.equals(searchText, "")) {
                        if (runnable != null)
                            handler.removeCallbacks(runnable);

                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                database.InsertSearch(searchText);
                                suggestionCursor = database.GetSearches(searchText);
                                suggestions.changeCursor(suggestionCursor);
                                suggestions.notifyDataSetChanged();
                            }
                        };
                        handler.postDelayed(runnable, 500);
                    }
                    return true;
                }
            });
        } // THREAD FRAGMENT
        else {
            fragment = new ThreadFragment();
            searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionClick(int position) {
                    if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE))
                    {
                        searchView.setQuery(suggestions.GetItem(position), false);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frameRight, fragment, null)
                                .addToBackStack(null)
                                .commit();
                    }
                    else {
                        searchView.setQuery(suggestions.GetItem(position), true);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame1, fragment, null)
                                .addToBackStack(null)
                                .commit();
                    }
                    return true;
                }

                @Override
                public boolean onSuggestionSelect(int position) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(final String searchText) {
                    ((ThreadFragment)fragment).Filter(searchText);
                    if (!Objects.equals(searchText, "")) {
                        if (runnable != null)
                            handler.removeCallbacks(runnable);

                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                database.InsertSearch(searchText);
                                suggestionCursor = database.GetSearches(searchText);
                                suggestions.changeCursor(suggestionCursor);
                                suggestions.notifyDataSetChanged();
                            }
                        };
                        handler.postDelayed(runnable, 500);
                    }
                    return true;
                }
            });
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLeft, fragment)
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame1, fragment)
                    .commit();
        }

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


