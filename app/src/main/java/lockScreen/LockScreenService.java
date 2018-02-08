package lockScreen;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class LockScreenService extends Service {
    public LockScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mScreenOnFilter = new IntentFilter();
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mScreenOnFilter.addAction(Intent.ACTION_SCREEN_ON);
        LockScreenService.this.registerReceiver(mScreenActionReceiver, mScreenOnFilter);
    }

    private BroadcastReceiver mScreenActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                Intent LockIntent = new Intent(LockScreenService.this,LockScreenActivity.class);
                LockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(LockIntent);
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                Log.e(TAG, "screen off");
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mScreenActionReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
