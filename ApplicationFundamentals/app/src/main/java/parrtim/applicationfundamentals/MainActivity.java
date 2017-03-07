package parrtim.applicationfundamentals;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    SMSListAdapter adapter;
    ListView listView;
    ArrayList<SMSInfo> messages;
    String msg = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(msg, "OnCreate event");
        setContentView(R.layout.activity_main);

        messages = getSMS();
        adapter = new SMSListAdapter(getApplicationContext(), messages);
        listView = (ListView) findViewById(R.id.messageList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SMSReply.class);
                SMSInfo message = (SMSInfo) parent.getItemAtPosition(position);
                intent.putExtra("message", message.Message);
                intent.putExtra("number", message.Number);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        String message = intent.getStringExtra("message");
        String number = intent.getStringExtra("number");
        Log.d(msg, message);
        adapter.add(new SMSInfo(number, message));
        super.onNewIntent(intent);
    }

    public ArrayList<SMSInfo> getSMS()
    {
        ArrayList<SMSInfo> sms = new ArrayList<>();
        Cursor cur = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        while (cur.moveToNext())
        {
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            sms.add(new SMSInfo(address, body));
        }

        cur.close();
        return sms;
    }
}

