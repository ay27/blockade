package bitman.ay27.blockade.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import bitman.ay27.blockade.BlockadeApplication;

import java.util.Set;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/27.
 */
public class PreferenceUtils {

    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private PreferenceUtils() {
        context = BlockadeApplication.getContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public static void write(String key, boolean value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putBoolean(key, value);
        instance.editor.commit();
    }

    public static void write(String key, int value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putInt(key, value);
        instance.editor.commit();
    }

    public static void write(String key, long value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putLong(key, value);
        instance.editor.commit();
    }

    public static void write(String key, String value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putString(key, value);
        instance.editor.commit();
    }

    public static void write(String key, Set<String> values) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putStringSet(key, values);
        instance.editor.commit();
    }

    public static boolean read(String key, boolean defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getBoolean(key, defValue);
    }

    public static int read(String key, int defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getInt(key, defValue);
    }

    public static long read(String key, long defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getLong(key, defValue);
    }

    public static String read(String key, String defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getString(key, defValue);
    }

    public static Set<String> read(String key, Set<String> defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getStringSet(key, defValue);
    }

}
