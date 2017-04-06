package parrtim.applicationfundamentals;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tparr on 4/4/2017.
 */

public class SMSUtil {

    public static ArrayList<SMSInfo> getSMSInbox(Context context)
    {
        ArrayList<SMSInfo> messages = new ArrayList<SMSInfo>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            messages.add(new SMSInfo(address, body));
        }

        cur.close();
        return messages;
    }

    public static ArrayList<SMSInfo> getSMSConversations(Context context)
    {
        ArrayList<SMSInfo> messages = new ArrayList<SMSInfo>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            messages.add(new SMSInfo(address, body));
        }

        cur.close();
        return messages;
    }

    public static ArrayList<SMSInfo> getSMSSent(Context context)
    {
        ArrayList<SMSInfo> messages = new ArrayList<SMSInfo>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            messages.add(new SMSInfo(address, body));
        }

        cur.close();
        return messages;
    }
}
