package bitman.ay27.blockade.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;
import bitman.ay27.blockade.BlockadeApplication;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/27.
 */
public class ServiceManager {

    private static ServiceManager instance = null;
    private final SharedPreferences.OnSharedPreferenceChangeListener onPreferenceChangedListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String _key) {
            KeySet key = KeySet.valueOf(_key);
            boolean enable = PreferenceUtils.read(key, false);
            if (enable) {
                startService(key);
            } else {
                stopService(key);
            }
        }
    };

    private Context context = BlockadeApplication.getContext();
    private Map<KeySet, Class> runningServices;

    private ServiceManager() {
        runningServices = new HashMap<KeySet, Class>();
    }

    public static ServiceManager getInstance() {
        if (instance == null)
            instance = new ServiceManager();
        return instance;
    }

    public void stopService(KeySet key) {
        stopService(ServiceKeyMap.get(key));
    }

    public void stopService(Class cls) {
        if (cls == null)
            return;
        KeySet key = ServiceKeyMap.get(cls);
        if (runningServices.containsKey(key)) {
            boolean value = PreferenceUtils.read(key, false);
            if (!value) {
                context.stopService(new Intent(context, cls));
                runningServices.remove(key);
                Toast.makeText(context, cls.getSimpleName()+" close", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startService(KeySet key) {
        startService(ServiceKeyMap.get(key));
    }

    public void startService(Class cls) {
        if (cls == null)
            return;
        KeySet key = ServiceKeyMap.get(cls);
        boolean enable = PreferenceUtils.read(key, false);
        if (enable) {
            Intent intent = new Intent(context, cls);
            context.startService(intent);
            runningServices.put(key, cls);
            Toast.makeText(context, cls.getSimpleName()+" open", Toast.LENGTH_SHORT).show();
        }
    }

    public void startAll() {
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(onPreferenceChangedListener);
        for (Class cls : ServiceKeyMap.values()) {
            startService(cls);
        }
    }

    public void stopAll() {
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(onPreferenceChangedListener);
        for (KeySet key : runningServices.keySet()) {
            stopService(key);
        }
    }

    public void destroy() {
        instance = null;
    }
}
