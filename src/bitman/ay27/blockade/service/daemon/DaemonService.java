package bitman.ay27.blockade.service.daemon;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import bitman.ay27.blockade.service.KeyguardService;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/25.
 */
public class DaemonService extends Service {

    private IBinder _binder = new ServiceManager();


    @Override
    public IBinder onBind(Intent intent) {
        return _binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        startService(new Intent(this, KeyguardService.class));
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
        super.onDestroy();
        startService(new Intent(this, DaemonService.class));
    }

}
