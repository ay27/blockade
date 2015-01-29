package bitman.ay27.blockade.service.user_service;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;
import bitman.ay27.blockade.activity.KeyguardActivity;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.service.AbsService;

/**
 * Created by ay27 on 15/1/21.
 */
public class KeyguardService extends AbsService {
    private BroadcastReceiver screenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
            keyguardLock.disableKeyguard();//解锁系统锁屏
        }
    };
    private BroadcastReceiver screenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent intent1 = new Intent(KeyguardService.this, KeyguardActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);//跳转到主界面
        }
    };

    private MyBinder binder = null;

    @Override
    public MyBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new MyBinder() {
                @Override
                public AbsService getService() {
                    return KeyguardService.this;
                }
            };
        }

        return binder;
    }

    @Override
    public KeySet getEnableKey() {
        return KeySet.KeyguardEnable;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        
        IntentFilter screenOff = new IntentFilter("android.intent.action.SCREEN_OFF");
        this.registerReceiver(screenOffReceiver, screenOff);

        IntentFilter screenOn = new IntentFilter("android.intent.action.SCREEN_ON");
        this.registerReceiver(screenOnReceiver, screenOn);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(screenOnReceiver);
        unregisterReceiver(screenOffReceiver);
    }
}
