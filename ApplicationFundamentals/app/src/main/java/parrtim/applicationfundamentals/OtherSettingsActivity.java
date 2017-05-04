package parrtim.applicationfundamentals;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class OtherSettingsActivity extends AppCompatActivity {

    String msg = "Reply Activity";
    boolean lightTheme = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieveSharePreferences();
        setContentView(R.layout.activity_other_settings);
        Switch toggle = (Switch) findViewById(R.id.switch3);
        toggle.setChecked(lightTheme);
    }

    public void onClickSwitch(View view)
    {
//        Switch toggle = (Switch)view;
//        SharedPreferences settings = getSharedPreferences("Settings", 0);
//        SharedPreferences.Editor editor = settings.edit();
//        if (toggle.isChecked())
//        {
//            editor.putString("Theme","LightAppTheme");
//            editor.apply();
//        }
//        else
//        {
//            editor.putString("Theme","DarkAppTheme");
//            editor.apply();
//        }
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
//            lightTheme = false;
//        }
//        else if (theme.equals("LightAppTheme"))
//        {
//            setTheme(R.style.LightAppTheme);
//            lightTheme = true;
//        }
//        else
//        {
//            Log.d(msg,"Theme was unexpected value: " + theme);
//        }
    }

    public void BackupMessages(View view)
    {
        Intent intent = new Intent(getApplicationContext(), BackupService.class);
        startService(intent);
    }
}