package parrtim.fragmentexample;

import android.app.Activity;
import android.os.Bundle;
import android.telecom.Call;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        DetailsFragment details = new DetailsFragment();
        details.setArguments(getIntent().getExtras());

        getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
    }
}
