package parrtim.applicationfundamentals;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SMSReply extends Activity {

    String receivedMessage;
    String replyMessage;
    String msg = "ReplyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreply);
        Log.d(msg, "The Reply onCreate() event");

        Intent intent = getIntent();
        receivedMessage = intent.getStringExtra("message");
        TextView viewById = (TextView) findViewById(R.id.inboxMessage);
        viewById.setText(receivedMessage);

        DictionaryOpenHelper database = new DictionaryOpenHelper(getApplicationContext());

        SQLiteDatabase writeableDatabase = database.getWritableDatabase();
        writeableDatabase.


        SQLiteDatabase readableDatabase = database.getReadableDatabase();
        Cursor results = readableDatabase.query("Draft", new String[]{ "message" }, "ID = " + receivedMessage, null, null, null, null);
        Log.d("","");
    }

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The Reply onStart() event");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The Reply onResume() event");
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The Reply onPause() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();

        Log.d(msg, "The Reply onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The Reply onDestroy() event");
    }
}
