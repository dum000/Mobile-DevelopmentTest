package parrtim.applicationfundamentals;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by tparr on 2/23/2017.
 */
public class HeadlessSmsSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
