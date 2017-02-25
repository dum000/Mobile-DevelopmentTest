package parrtim.applicationfundamentals;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    ArrayAdapter<String> adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView = (ListView) findViewById(R.id.messageList);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("Message", intent.getStringExtra("message"));
        adapter.add(intent.getStringExtra("message"));
        super.onNewIntent(intent);
    }
}
