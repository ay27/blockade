package bitman.ay27.blockade;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getContext() {
        return instance;
    }
}