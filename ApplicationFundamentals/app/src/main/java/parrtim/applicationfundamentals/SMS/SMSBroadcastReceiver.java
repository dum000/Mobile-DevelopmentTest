package parrtim.applicationfundamentals.SMS;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;

import parrtim.applicationfundamentals.MainActivity;
import parrtim.applicationfundamentals.R;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;

    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;

    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(context);
        String packageName = context.getPackageName();
        if (defaultSmsPackage != null && defaultSmsPackage.equals(packageName))
        {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            putSmsToDatabase(context.getContentResolver(), messages);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("message", messages[0].getMessageBody());
            i.putExtra("number", messages[0].getOriginatingAddress());

            PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setContentTitle("New Message")
                    .setContentText(messages[0].getMessageBody())
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, mBuilder.build());
        }
    }

    private void putSmsToDatabase(ContentResolver contentResolver, SmsMessage[] sms)
    {
        for (SmsMessage message : sms)
        {
            ContentValues values = new ContentValues();
            values.put(Telephony.Sms.Inbox.ADDRESS, message.getOriginatingAddress());
            values.put(Telephony.Sms.Inbox.BODY, message.getDisplayMessageBody());
            values.put(Telephony.Sms.Inbox.DATE, message.getTimestampMillis());
            values.put(Telephony.Sms.Inbox.READ, MESSAGE_IS_NOT_READ);
            values.put(Telephony.Sms.Inbox.STATUS, message.getStatus());
            values.put(Telephony.Sms.Inbox.TYPE, MESSAGE_TYPE_INBOX);
            values.put(Telephony.Sms.Inbox.SEEN, MESSAGE_IS_NOT_SEEN);

            contentResolver.insert(Telephony.Sms.Inbox.CONTENT_URI, values);
        }
    }
}
