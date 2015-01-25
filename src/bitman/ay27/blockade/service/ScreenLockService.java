package bitman.ay27.blockade.service;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import bitman.ay27.blockade.activity.FloatView;

/**
 * Created by ay27 on 15/1/21.
 */
public class ScreenLockService extends AutoRestartService {
    private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
            keyguardLock.disableKeyguard();//解锁系统锁屏

            Intent intent1 = new Intent(ScreenLockService.this, FloatView.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent1);//跳转到主界面
        }
    };

    public ScreenLockService() {
        super(ScreenLockService.class);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter screenOn = new IntentFilter("android.intent.action.SCREEN_ON");
        this.registerReceiver(screenOnReceiver, screenOn);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenOnReceiver);
        super.onDestroy();
    }
}
