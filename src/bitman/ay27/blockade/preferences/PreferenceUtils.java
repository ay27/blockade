package bitman.ay27.blockade.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import bitman.ay27.blockade.BlockadeApplication;

import java.util.Set;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/27.
 */
public class PreferenceUtils {

    private static final String PREFERENCE_NAME = "preferences";
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private PreferenceUtils() {
        context = BlockadeApplication.getContext();
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static SharedPreferences getPreferences() {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences;
    }

    public static void write(KeySet key, boolean value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putBoolean(key.name(), value);
        instance.editor.commit();
    }

    public static void write(KeySet key, int value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putInt(key.name(), value);
        instance.editor.commit();
    }

    public static void write(KeySet key, long value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putLong(key.name(), value);
        instance.editor.commit();
    }

    public static void write(KeySet key, String value) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putString(key.name(), value);
        instance.editor.apply();
    }

    public static void write(KeySet key, Set<String> values) {
        PreferenceUtils instance = new PreferenceUtils();
        instance.editor.putStringSet(key.name(), values);
        instance.editor.commit();
    }

    public static boolean read(KeySet key, boolean defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getBoolean(key.name(), defValue);
    }

    public static int read(KeySet key, int defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getInt(key.name(), defValue);
    }

    public static long read(KeySet key, long defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getLong(key.name(), defValue);
    }

    public static String read(KeySet key, String defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getString(key.name(), defValue);
    }

    public static Set<String> read(KeySet key, Set<String> defValue) {
        PreferenceUtils instance = new PreferenceUtils();
        return instance.preferences.getStringSet(key.name(), defValue);
    }

}
