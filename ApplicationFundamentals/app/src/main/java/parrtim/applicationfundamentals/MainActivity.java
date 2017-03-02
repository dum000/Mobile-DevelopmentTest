package parrtim.applicationfundamentals;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class MainActivity extends Activity {

    ArrayAdapter<String> adapter;
    ListView listView;
    ArrayList<String> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Message", "The Main OnCreate event");

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
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("messages", messages);
        Log.d("Message", "Saving Messages");
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String message = intent.getStringExtra("message");
        Log.d("Message", message);
        adapter.add(message);
        messages.add(message);
        super.onNewIntent(intent);
    }

    public List<String> getSMS(){
        List<String> sms = new ArrayList<>();
        Cursor cur = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        while (cur.moveToNext()) {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add("Number: " + address + " .Message: " + body);

        }
        return sms;

    }
}
