package bitman.ay27.blockade.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;

/**
 * Created by ay27 on 15/1/23.
 */
public class LockService extends Service {
    private Timer mTimer;
    public static final int FOREGROUND_ID = 0;

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            LockTask lockTask = new LockTask(this);
            mTimer.schedule(lockTask, 0L, 1000L);
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

        startTimer();

        startForeground(FOREGROUND_ID, new Notification());
    }

//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        return super.onStartCommand(intent, flags, startId);
//    }

    public void onDestroy() {
        stopForeground(true);
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
        super.onDestroy();
    }
}