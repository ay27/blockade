package bitman.ay27.blockade.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import bitman.ay27.blockade.LockScreenActivity;

/**
 * Created by ay27 on 15/1/21.
 */
public class ScreenLockService extends Service {
    private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
            keyguardLock.disableKeyguard();//解锁系统锁屏

            Intent intent1 = new Intent(ScreenLockService.this, LockScreenActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent1);//跳转到主界面
        }
    };
    private BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
            keyguardLock.disableKeyguard();//解锁系统锁屏
            Intent intent1 = new Intent(ScreenLockService.this, LockScreenActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            LockScreenActivity.gotoRealHome = false;

            startActivity(intent1);//跳转到主界面
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        IntentFilter screenOn = new IntentFilter("android.intent.action.SCREEN_ON");
        this.registerReceiver(screenOnReceiver, screenOn);

//        IntentFilter screenOff = new IntentFilter("android.intent.action.SCREEN_OFF");
//        this.registerReceiver(screenOffReceiver, screenOff);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(screenOnReceiver);
        unregisterReceiver(screenOffReceiver);
    }
}
