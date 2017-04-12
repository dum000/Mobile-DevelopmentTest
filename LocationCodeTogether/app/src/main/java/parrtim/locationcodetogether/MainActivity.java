package parrtim.locationcodetogether;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    Location current;
    TextView loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        loc = (TextView) findViewById(R.id.loc);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_DENIED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION }, 0);
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
        }

        List<String> providers = locationManager.getProviders(true);

        for (String provider : providers) {
            Location tempLoc = locationManager.getLastKnownLocation(provider);

            if (tempLoc == null) {
                continue;
            }

            if (current == null || tempLoc.getAccuracy() < current.getAccuracy()) {
                current = tempLoc;
            }
        }

        if (current != null) {
            loc.setText("Lat: " + current.getLatitude() + " Long: " + current.getLongitude());
        }
    }
}
