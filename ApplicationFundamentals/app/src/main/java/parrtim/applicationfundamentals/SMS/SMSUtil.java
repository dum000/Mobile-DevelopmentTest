package parrtim.applicationfundamentals.SMS;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static android.database.Cursor.FIELD_TYPE_BLOB;
import static android.database.Cursor.FIELD_TYPE_FLOAT;
import static android.database.Cursor.FIELD_TYPE_INTEGER;
import static android.database.Cursor.FIELD_TYPE_NULL;
import static android.database.Cursor.FIELD_TYPE_STRING;

public class SMSUtil {

    static SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");;

    public static ArrayList<InboxInfo> getSMSInbox(Context context) {
        ArrayList<InboxInfo> messages = new ArrayList<>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        String[] columnNames = cur.getColumnNames();

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");
        int dateIndex = cur.getColumnIndex("date");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            try {
                Date date = simpleDate.parse(simpleDate.format(cur.getLong(dateIndex)));
                messages.add(new InboxInfo(address, body, date, true));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("Error", e.toString());
            }
        }

        cur.close();
        return messages;
    }

    public static ArrayList<ConversationInfo> getSMSConversations(Context context)
    {
        ArrayList<ConversationInfo> messages = new ArrayList<>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Conversations.CONTENT_URI, null, null, null, null);

        int snippet_index = cur.getColumnIndex("snippet");
        int msg_count_index = cur.getColumnIndex("msg_count");

        while (cur.moveToNext())
        {
            String snippet = cur.getString(snippet_index);
            String msg_count = cur.getString(msg_count_index);
            messages.add(new ConversationInfo(snippet, msg_count));
        }

        cur.close();
        return messages;
    }

    public static ArrayList<InboxInfo> getSMSSent(Context context) {
        ArrayList<InboxInfo> messages = new ArrayList<>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Sent.CONTENT_URI, null, null, null, null);

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");
        int dateIndex = cur.getColumnIndex("date");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            try {
                Date date = simpleDate.parse(simpleDate.format(cur.getLong(dateIndex)));
                messages.add(new InboxInfo(address, body, date, false));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        cur.close();
        return messages;
    }

    @SuppressWarnings("Since15")
    public static ArrayList<InboxInfo> getConversation(Context context) {
        ArrayList<InboxInfo> incomingMessages = new ArrayList<>(getSMSInbox(context).subList(0,25));
        ArrayList<InboxInfo> outgoingMessages = new ArrayList<>(getSMSSent(context).subList(0,25));

        incomingMessages.addAll(outgoingMessages);

        Collections.sort(incomingMessages, new Comparator<InboxInfo>() {
            @Override
            public int compare(InboxInfo inboxInfo, InboxInfo t1) {
                return inboxInfo.Date.compareTo(t1.Date);
            }
        });

        return incomingMessages;
    }

    public static ArrayList<InboxInfo> getSMSThreads(Context context)
    {
        ArrayList<InboxInfo> messages = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor inboxCursor = resolver.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                new String[] { Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.DATE },
                null,
                null,
                null);

        if (inboxCursor != null) {

            int addressIndex = inboxCursor.getColumnIndex("address");
            int bodyIndex = inboxCursor.getColumnIndex("body");
            int dateIndex = inboxCursor.getColumnIndex("date");

            while (inboxCursor.moveToNext())
            {
                String address = inboxCursor.getString(addressIndex);
                String body = inboxCursor.getString(bodyIndex);

                try {
                    Date date = simpleDate.parse(simpleDate.format(inboxCursor.getLong(dateIndex)));
                    messages.add(new InboxInfo(address, body, date, true));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("Error", e.toString());
                }
            }
            inboxCursor.close();
        }

        Cursor sentCursor = resolver.query(
                Telephony.Sms.Sent.CONTENT_URI,
                new String[] { Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.DATE },
                null,
                null,
                null);

        if (sentCursor != null) {

            int addressIndex = sentCursor.getColumnIndex("address");
            int bodyIndex = sentCursor.getColumnIndex("body");
            int dateIndex = sentCursor.getColumnIndex("date");

            while (sentCursor.moveToNext())
            {
                String address = sentCursor.getString(addressIndex);
                String body = sentCursor.getString(bodyIndex);

                try {
                    Date date = simpleDate.parse(simpleDate.format(sentCursor.getLong(dateIndex)));
                    messages.add(new InboxInfo(address, body, date, true));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("Error", e.toString());
                }
            }
            sentCursor.close();
        }

        return messages;
    }
}