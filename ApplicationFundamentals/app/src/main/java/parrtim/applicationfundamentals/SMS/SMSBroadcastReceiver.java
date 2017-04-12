package parrtim.applicationfundamentals.SMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import parrtim.applicationfundamentals.MainActivity;

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
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //context.startActivity(i);
    }
}
