package bitman.ay27.blockade.utils;

import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;
import bitman.ay27.blockade.BlockadeApplication;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/29.
 */
public class SettingsUtils {
    private SettingsUtils() {}

    private static Context context = BlockadeApplication.getContext();

    public static boolean setADB(boolean value) {
        if (value) {
            return Settings.Global.putInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 1);
        } else {
            return Settings.Global.putInt(context.getContentResolver(), Settings.Global.ADB_ENABLED, 0);
        }
    }

}
