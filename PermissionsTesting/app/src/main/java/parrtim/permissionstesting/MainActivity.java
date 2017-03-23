package parrtim.permissionstesting;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Main Activity", Integer.toString(ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_SMS")));
    }
}
