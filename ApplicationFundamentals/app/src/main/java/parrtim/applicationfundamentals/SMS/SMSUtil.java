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

    static SimpleDateFormat simpleDate;

    public static ArrayList<InboxInfo> getSMSInbox(Context context) {
        ArrayList<InboxInfo> messages = new ArrayList<>();
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        String[] columnNames = cur.getColumnNames();

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");
        int dateIndex = cur.getColumnIndex("date");

        simpleDate = new SimpleDateFormat("MM/dd/yyyy");

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            try {
                Date date = simpleDate.parse(simpleDate.format(cur.getLong(dateIndex)));
                messages.add(new InboxInfo(address, body, date, false));
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
        Cursor cur = context.getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);

        int addressIndex = cur.getColumnIndex("address");
        int bodyIndex = cur.getColumnIndex("body");
        int dateIndex = cur.getColumnIndex("date");

        simpleDate = new SimpleDateFormat();

        while (cur.moveToNext())
        {
            String address = cur.getString(addressIndex);
            String body = cur.getString(bodyIndex);
            try {
                Date date = simpleDate.parse(cur.getString(dateIndex));
                messages.add(new InboxInfo(address, body, date, true));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        cur.close();
        return messages;
    }

    @SuppressWarnings("Since15")
    public static ArrayList<InboxInfo> getConversation(Context context) {
        ArrayList<InboxInfo> incomingMessages = getSMSInbox(context);
        ArrayList<InboxInfo> outgoingMessages = getSMSSent(context);

        incomingMessages.addAll(outgoingMessages);

        Collections.sort(incomingMessages, new Comparator<InboxInfo>() {
            @Override
            public int compare(InboxInfo inboxInfo, InboxInfo t1) {
                return inboxInfo.Date.compareTo(t1.Date);
            }
        });

        return incomingMessages;
    }

    public static ArrayList<ThreadInfo> getSMSThreads(Context context)
    {
        ArrayList<ThreadInfo> messages = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cur = resolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        String[] columns = cur.getColumnNames();
        Log.d("SMSUtil", "Columns");
        int column_count = cur.getColumnCount();
        int multiplier = 0;
        int count = cur.getCount();
        Object[] values = new Object[column_count * count];
        while (cur.moveToNext()) {
            for (int i = 0 ; i < column_count; i++) {
                switch (cur.getType(i))
                {
                    case FIELD_TYPE_NULL: values[i + (multiplier * column_count)] = null; break;
                    case FIELD_TYPE_FLOAT: values[i + (multiplier * column_count)] = cur.getFloat(i); break;
                    case FIELD_TYPE_INTEGER: values[i + (multiplier * column_count)] = cur.getInt(i); break;
                    case FIELD_TYPE_STRING: values[i + (multiplier * column_count)] = cur.getString(i); break;
                    case FIELD_TYPE_BLOB: values[i + (multiplier * column_count)] = cur.getBlob(i); break;
                    default: Log.d("VALUE", cur.getType(i) + ""); break;
                }
            }
            multiplier++;
        }
        int snippet_index = cur.getColumnIndex("body");
        int msg_count_index = cur.getColumnIndex("msg_count");

        while (cur.moveToNext())
        {
            String snippet = cur.getString(snippet_index);
            String msg_count = cur.getString(msg_count_index);
            messages.add(new ThreadInfo("", snippet, msg_count));
        }

        cur.close();
        return messages;
    }
}
/*0 = "body"
1 = "person"
2 = "text_only"
3 = "sub"
4 = "subject"
5 = "retr_st"
6 = "type"
7 = "date"
8 = "ct_cls"
9 = "sub_cs"
10 = "_id"
11 = "read"
12 = "ct_l"
13 = "tr_id"
14 = "st"
15 = "msg_box"
16 = "thread_id"
17 = "reply_path_present"
18 = "m_cls"
19 = "read_status"
20 = "ct_t"
21 = "status"
22 = "retr_txt_cs"
23 = "d_rpt"
24 = "error_code"
25 = "m_id"
26 = "date_sent"
27 = "m_type"
28 = "v"
29 = "exp"
30 = "pri"
31 = "service_center"
32 = "address"
33 = "rr"
34 = "rpt_a"
35 = "resp_txt"
36 = "locked"
37 = "resp_st"
38 = "m_size"
*/
