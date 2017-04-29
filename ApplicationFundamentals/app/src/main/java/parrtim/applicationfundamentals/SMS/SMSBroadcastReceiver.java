package parrtim.applicationfundamentals.SMS;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import parrtim.applicationfundamentals.MainActivity;
import parrtim.applicationfundamentals.R;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for (SmsMessage message : messages) {
            Log.d("Receiver Message", message.getMessageBody());
        }

        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("message", messages[0].getMessageBody());
        i.putExtra("number", messages[0].getOriginatingAddress());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

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
