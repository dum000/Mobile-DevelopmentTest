package parrtim.applicationfundamentals;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by tparr on 2/23/2017.
 */
public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for (SmsMessage message : messages) {
            Log.d("TAG", message.getMessageBody());
        }

        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("message", messages[0].getMessageBody());            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);
    }
}
