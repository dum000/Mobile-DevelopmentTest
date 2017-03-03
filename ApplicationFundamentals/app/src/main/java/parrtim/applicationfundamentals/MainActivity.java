package parrtim.applicationfundamentals;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> messages;
    String msg = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Message", "OnCreate event");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        if (savedInstanceState != null) {
            messages = savedInstanceState.getStringArrayList("messages");
            adapter.clear();
            Log.d("Message", "Bundle was restored.");
        }
        else {
            messages = new ArrayList<>();
        }

        adapter.addAll(getSMS());

        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.messageList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SMSReply.class);
                String message = (String) parent.getItemAtPosition(position);
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "onStart() event");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("messages", messages);
        Log.d("Message", "Saving Messages");
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        String message = intent.getStringExtra("message");
        Log.d("Message", message);
        adapter.add(message);
        messages.add(message);
        super.onNewIntent(intent);
    }

    public List<String> getSMS(){
        List<String> sms = new ArrayList<>();
        Cursor cur = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        while (cur.moveToNext())
        {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add("Number: " + address + " .Message: " + body);
        }
        return sms;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(msg, "onRestart Event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "onDestroy() event");
    }
}
