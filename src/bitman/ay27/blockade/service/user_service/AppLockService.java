package bitman.ay27.blockade.service.user_service;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import bitman.ay27.blockade.BlockadeApplication;
import bitman.ay27.blockade.activity.AppLockActivity;
import bitman.ay27.blockade.orm.DatabaseHelper;
import bitman.ay27.blockade.orm.module.AppLockItem;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
import bitman.ay27.blockade.service.AbsService;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/25.
 */
public class AppLockService extends AbsService {

    private MyBinder binder = null;
    private Timer mTimer;
    private ArrayList<AppLockItem> lockedList;
    private ArrayList<AppLockItem> tempStopLockList;

    private void startTimer() {
        if (mTimer == null) {
            mTimer = new Timer();
            LockTask lockTask = new LockTask(this);
            mTimer.schedule(lockTask, 0L, 2000L);
        }
    }

    @Override
    public MyBinder onBind(Intent intent) {
        if (binder == null) {
            binder = new MyBinder() {
                @Override
                public AbsService getService() {
                    return AppLockService.this;
                }
            };
        }

        return binder;
    }

    @Override
    public KeySet getEnableKey() {
        return KeySet.AppLockEnable;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lockedList = new ArrayList<AppLockItem>();
        tempStopLockList = BlockadeApplication.tempStopLockList;
        load_locked_app_list();
        startTimer();
    }

    private void load_locked_app_list() {
        DatabaseHelper helper = new DatabaseHelper(this);
        RuntimeExceptionDao dao = helper.getRuntimeExceptionDao(AppLockItem.class);
        try {
            lockedList = new ArrayList<AppLockItem>(dao.queryBuilder().query());
        } catch (SQLException e) {
            e.printStackTrace();
            lockedList = new ArrayList<AppLockItem>();
        }
    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        super.onDestroy();
        if (PreferenceUtils.read(getEnableKey(), false)) {
            startService(new Intent(this, KeyguardService.class));
        }
    }

    public void addTempStop(String packageName) {
        tempStopLockList.add(new AppLockItem(packageName));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private class LockTask extends TimerTask {
        public static final String TAG = "LockTask";
        private Context mContext;
        private ActivityManager mActivityManager;

        public LockTask(Context context) {
            mContext = context;
            mActivityManager = (ActivityManager) context.getSystemService("activity");
        }

        @Override
        public void run() {
            ComponentName topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
            final String packageName = topActivity.getPackageName();
            String className = topActivity.getClassName();
            Log.v(TAG, "packageName" + packageName);
            Log.v(TAG, "className" + className);

            if (ifContains(packageName)) {
                Intent intent = new Intent(AppLockService.this, AppLockActivity.class);
//                intent.setClassName("bitman.ay27.blockade", "bitman.ay27.blockade.activity.AppLockActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("PackageName", packageName);
                startActivity(intent);
            }
        }

        private boolean ifContains(String packageName) {
            boolean isLocked = false;
            for (AppLockItem item : lockedList) {
                if (item.getPackageName().equals(packageName)) {
                    isLocked = true;
                    break;
                }
            }

            if (!isLocked)
                return false;

            for (AppLockItem item : tempStopLockList) {
                if (item.getPackageName().equals(packageName)) {
                    return false;
                }
            }

            return true;
        }
    }
}
