package bitman.ay27.blockade;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import bitman.ay27.blockade.orm.module.AppLockItem;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
import bitman.ay27.blockade.service.DaemonService;

import java.util.ArrayList;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/26.
 */
public class BlockadeApplication extends Application {

    private static BlockadeApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float x = metrics.density;
    }

    public static Context getContext() {
        return instance;
    }
}
