package bitman.ay27.blockade.service;

import android.app.Service;
import android.content.Intent;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/25.
 */
public abstract class AutoRestartService extends Service {

    private Class subCls = null;

    public AutoRestartService(Class subCls) {
        super();
        this.subCls = subCls;
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
        if (subCls!=null)
            startService(new Intent(getApplicationContext(), subCls));
    }
}
