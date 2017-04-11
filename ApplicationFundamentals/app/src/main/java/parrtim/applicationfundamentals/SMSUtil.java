package parrtim.applicationfundamentals;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import java.util.ArrayList;

public class SMSUtil {

    public static ArrayList<InboxInfo> getSMSInbox(Context context)
    {
        ArrayList<InboxInfo> messages = new ArrayList<InboxInfo>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            messages.add(new InboxInfo(address, body));
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

    public static ArrayList<InboxInfo> getSMSSent(Context context)
    {
        ArrayList<InboxInfo> messages = new ArrayList<>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            messages.add(new InboxInfo(address, body));
        }

        cur.close();
        return messages;
    }

    public static ArrayList<ConversationInfo> getSMSThreads(Context context)
    {
        ArrayList<ConversationInfo> messages = new ArrayList<>();
        Cursor cur = context.getContentResolver().query(Telephony.Threads.CONTENT_URI, null, null, null, null);

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
}
