package bitman.ay27.blockade.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/25.
 */
public class DaemonService extends Service {

    private ServiceManager manager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        manager = ServiceManager.getInstance();
        manager.startAll();
    }

    /**
     * guarantee the service will not be killed.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, START_STICKY, startId);
        return Service.START_STICKY;
    }

    /**
     * guarantee the service will not be killed.
     */
    @Override
    public void onDestroy() {
        manager.stopAll();
        manager.destroy();

        super.onDestroy();

        startService(new Intent(this, DaemonService.class));
    }

}
