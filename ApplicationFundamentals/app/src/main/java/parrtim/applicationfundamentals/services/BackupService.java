package parrtim.applicationfundamentals.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import parrtim.applicationfundamentals.models.InboxInfo;
import parrtim.applicationfundamentals.helper.SMSUtil;

public class BackupService extends IntentService {

    Handler mHandler;
    final String filename = "sms_Backup.txt";

    public BackupService() {
        super("BackupService");
        mHandler = new Handler();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BackupService(String name) {
        super(name);
        mHandler = new Handler();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<InboxInfo> smsConversations = SMSUtil.getSMSConversations(this);
        try
        {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(smsConversations);
            fos.close();
            oos.close();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Backed Up Messages to " + filename, Toast.LENGTH_LONG).show();
                    Log.d("BackupService", "Backed Up Messages to " + filename);
                }
            });
        } catch (IOException e)
        {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Failed to Backup Messages", Toast.LENGTH_LONG).show();
                    Log.d("BackupService", "Backed Up Messages to " + filename);
                }
            });
            Log.d("BackupService", "Failed to Backup Messages");
        }
    }
}
