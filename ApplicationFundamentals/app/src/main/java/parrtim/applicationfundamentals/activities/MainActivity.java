package parrtim.applicationfundamentals.activities;

import android.Manifest;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import parrtim.applicationfundamentals.R;
import parrtim.applicationfundamentals.adapters.SuggestionAdapter;
import parrtim.applicationfundamentals.database.DatabaseOpenHelper;
import parrtim.applicationfundamentals.fragments.ConversationFragment;
import parrtim.applicationfundamentals.fragments.InboxFragment;
import parrtim.applicationfundamentals.fragments.SMSMessageParentFragment;
import parrtim.applicationfundamentals.fragments.ThreadFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    String msg = "Main Activity";
    SearchView searchView;
    DatabaseOpenHelper database;
    SuggestionAdapter suggestions;
    Cursor suggestionCursor;
    Handler handler;
    Runnable runnable;
    FragmentManager fragmentManager;
    ArrayList<String> permissionsToRequest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(msg, "OnCreate event");
        setContentView(R.layout.activity_main);
        database = new DatabaseOpenHelper(getApplicationContext());
        suggestionCursor = database.GetSearches("");
        suggestions = new SuggestionAdapter(getApplicationContext(), suggestionCursor);
        handler = new Handler();
        fragmentManager = getSupportFragmentManager();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) == PermissionChecker.PERMISSION_DENIED) {
            permissionsToRequest.add(Manifest.permission.READ_SMS);
        }
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_DENIED) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_DENIED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_DENIED) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        String[] permissionsArray = new String[permissionsToRequest.size()];
        permissionsArray = permissionsToRequest.toArray(permissionsArray);

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsArray, 0);
        }
        else {
            CheckForDefaultApp();
        }
    }

    private void CheckForDefaultApp() {
        if (Telephony.Sms.getDefaultSmsPackage(this) == null || !Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
            Intent intent =
                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);

            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    getPackageName());

            startActivity(intent);
        }
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
                if (grantResults.length == 0 || grantResults.length > 0 && !Arrays.asList(grantResults).contains(PackageManager.PERMISSION_DENIED))
                {
                    CheckForDefaultApp();
                }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        return LoadFragment(id);
    }

    private boolean LoadFragment(int id) {
        final Fragment fragment;

        switch (id) {
            case R.id.conversations:
                fragment = new ConversationFragment();
                break;
            case R.id.inbox:
                fragment = new InboxFragment();
                searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                    final Fragment clickedFragment = new SMSMessageParentFragment();

                    @Override
                    public boolean onSuggestionClick(int position) {
                        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
                            searchView.setQuery(suggestions.GetItem(position), false);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frameRight, clickedFragment, null)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            searchView.setQuery(suggestions.GetItem(position), true);
                            fragmentManager.beginTransaction()
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
                        ((InboxFragment) fragment).Filter(searchText);
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
                break;
            case R.id.threads:
                fragment = new ThreadFragment();

                searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
                    @Override
                    public boolean onSuggestionClick(int position) {
                        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
                            searchView.setQuery(suggestions.GetItem(position), false);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frameRight, fragment, null)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            searchView.setQuery(suggestions.GetItem(position), true);
                            fragmentManager.beginTransaction()
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
                        ((ThreadFragment) fragment).Filter(searchText);
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
                break;
            default:
                throw new UnsupportedOperationException(Integer.toString(id));
        }


        if (getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE))
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.frameLeft, fragment)
                    .commit();

            ConversationFragment conversationFragment = new ConversationFragment();
            fragmentManager.beginTransaction().replace(R.id.frameRight, conversationFragment).commit();
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
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}


