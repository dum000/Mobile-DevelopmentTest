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
    String receivedNumber;
    String msg = "ReplyActivity";
    DictionaryOpenHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreply);

        Intent intent = getIntent();
        receivedMessage = intent.getStringExtra("message");
        receivedNumber = intent.getStringExtra("number");

        TextView messageViewById = (TextView) findViewById(R.id.inboxMessage);
        messageViewById.setText(receivedMessage);
        TextView numberViewById = (TextView) findViewById(R.id.inboxNumber);
        numberViewById.setText(receivedNumber);

        database = new DictionaryOpenHelper(getApplicationContext());

        SQLiteDatabase readableDatabase = database.getReadableDatabase();

        Cursor results = readableDatabase.query("Draft", new String[]{ "Number", "ReceivedMessage", "DraftMessage" }, "Number = " + receivedNumber + " AND " + "ReceivedMessage = \"" + receivedMessage + "\"", null, null, null, null);

        if (results.moveToNext()) {
            TextView draftMessage = (TextView) findViewById(R.id.replyMessage);
            String replyMessage = results.getString(2);
            draftMessage.setText(replyMessage);
        }

        results.close();

        Log.d(msg, "The Reply onCreate() event");
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();

        TextView draftMessage = (TextView) findViewById(R.id.replyMessage);

        SQLiteDatabase writeableDatabase = database.getWritableDatabase();
        writeableDatabase.execSQL(
                "UPDATE Draft\n" +
                "SET DraftMessage='" + draftMessage.getText().toString() + "'\n" +
                "WHERE Number=\"" + receivedNumber + "\" AND ReceivedMessage=\"" + receivedMessage + "\";" +
                "\n" +
                "INSERT INTO Draft (Number, ReceivedMessage, DraftMessage)\n" +
                "SELECT '" + receivedNumber + "', \"" + receivedMessage + "\", '" + draftMessage.getText().toString() + "  \n" +
                "WHERE (Select Changes() = 0);");

        database.close();

        Log.d(msg, "The Reply onStop() event");
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
        Log.d(msg, "The Reply onDestroy() event");
    }
}
