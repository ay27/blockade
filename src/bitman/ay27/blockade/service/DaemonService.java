package bitman.ay27.blockade.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/25.
 */
public class DaemonService extends AutoRestartService {
    public DaemonService() {
        super(null);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
