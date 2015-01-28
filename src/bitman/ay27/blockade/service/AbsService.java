package bitman.ay27.blockade.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import bitman.ay27.blockade.preferences.KeySet;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/27.
 */
public abstract class AbsService extends Service {
    @Override
    public abstract MyBinder onBind(Intent intent);

    public abstract KeySet getEnableKey();

    public abstract static class MyBinder extends Binder {
        public abstract AbsService getService();
    }

}
