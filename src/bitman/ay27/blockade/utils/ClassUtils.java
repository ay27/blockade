package bitman.ay27.blockade.utils;

import android.content.Context;
import bitman.ay27.blockade.BlockadeApplication;
import dalvik.system.DexFile;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/27.
 */
public class ClassUtils {


    public static List<Class> getClassesFromPackage(String packageName) {
        Context context = BlockadeApplication.getContext();
        ArrayList<Class> result = new ArrayList<Class>();

        try {
            String path = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), 0).sourceDir;
            DexFile dexfile = new DexFile(path);
            Enumeration<String> entries = dexfile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement();
                if (name.startsWith(packageName) && !name.contains("$"))
                    result.add(Class.forName(name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
