package bitman.ay27.blockade.service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import bitman.ay27.blockade.activity.LockScreenActivity;

import java.util.TimerTask;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/23.
 */
public class LockTask extends TimerTask {
    public static final String TAG = "LockTask";
    private Context mContext;
    String testPackageName = "com.android.settings";
    String testClassName = "com.android.settings.Settings";

    private ActivityManager mActivityManager;

    public LockTask(Context context) {
        mContext = context;
        mActivityManager = (ActivityManager) context.getSystemService("activity");
    }

    @Override
    public void run() {
        ComponentName topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
        String packageName = topActivity.getPackageName();
        String className = topActivity.getClassName();
        Log.v(TAG, "packageName" + packageName);
        Log.v(TAG, "className" + className);

        if (testPackageName.equals(packageName)
                && testClassName.equals(className) && LockScreenActivity.isLock) {
            Intent intent = new Intent();
            intent.setClassName("bitman.ay27.blockade", "bitman.ay27.blockade.activity.LockScreenActivity");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
}