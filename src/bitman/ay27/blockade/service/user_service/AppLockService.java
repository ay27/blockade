package bitman.ay27.blockade.service.user_service;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import bitman.ay27.blockade.activity.AppLockActivity;
import bitman.ay27.blockade.orm.DatabaseHelper;
import bitman.ay27.blockade.orm.module.AppLockItem;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
import bitman.ay27.blockade.service.AbsService;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.*;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/25.
 */
public class AppLockService extends AbsService {

    private MyBinder binder = null;
    private Timer mTimer;
    private static ArrayList<String> lockedApp;
    private static ArrayList<String> temporaryUnLockApp;
    static {
        lockedApp = new ArrayList<String>();
        temporaryUnLockApp = new ArrayList<String>();
    }

    public static void addUnlockApp(String packageName) {
        temporaryUnLockApp.add(packageName);
    }

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
        load_locked_app_list();
        startTimer();
    }

    private void load_locked_app_list() {
        DatabaseHelper helper = new DatabaseHelper(this);
        RuntimeExceptionDao dao = helper.getRuntimeExceptionDao(AppLockItem.class);
        lockedApp = new ArrayList<String>();
        try {
            for (Object item : dao.queryBuilder().query()) {
                lockedApp.add(((AppLockItem) item).getPackageName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = mActivityManager.getRunningTasks(Byte.MAX_VALUE);

            String topPackageName = runningTaskInfos.get(0).topActivity.getPackageName();
            if (lockedApp.contains(topPackageName) &&
                    !temporaryUnLockApp.contains(topPackageName)) {
                lockIt(topPackageName, runningTaskInfos.get(0).topActivity.getClassName());
            }
        }

        private void lockIt(String topPackageName, String className) {
            Intent intent = new Intent(AppLockService.this, AppLockActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("PackageName", topPackageName);
            intent.putExtra("ClassName", className);
            startActivity(intent);
        }

    }
}
