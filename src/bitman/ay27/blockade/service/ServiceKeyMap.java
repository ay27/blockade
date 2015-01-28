package bitman.ay27.blockade.service;

import android.util.Pair;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.utils.ClassUtils;

import java.util.*;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/28.
 */
public class ServiceKeyMap {

    private static final String SERVICE_PACKAGE_NAME = "bitman.ay27.blockade.service.user_service";
    private static final Map<KeySet, Class> serviceMap;

    private ServiceKeyMap() {
    }

    static {
        serviceMap = new HashMap<KeySet, Class>();
        List<Class> classes = ClassUtils.getClasssFromPackage(SERVICE_PACKAGE_NAME);
        for (Class cls : classes) {
            try {
                AbsService oneService = (AbsService) cls.newInstance();
                KeySet key = oneService.getEnableKey();
                serviceMap.put(key, cls);
                oneService.stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Class get(KeySet key) {
        return serviceMap.get(key);
    }

    public static KeySet get(Class cls) {
        for (KeySet key : serviceMap.keySet()) {
            if (serviceMap.get(key).equals(cls)) {
                return key;
            }
        }
        return null;
    }

    public static Set<KeySet> keySet() {
        return serviceMap.keySet();
    }

    public static Collection<Class> values() {
        return serviceMap.values();
    }
}
