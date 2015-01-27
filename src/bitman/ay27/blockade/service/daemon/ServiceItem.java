package bitman.ay27.blockade.service.daemon;

import android.app.Service;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/26.
 */
class ServiceItem {
    private static int seed = 0x00000000;
    private final int id;
    private Service service;
    private boolean restartable;

    public ServiceItem(Service service, boolean restartable) {
        this.service = service;
        this.restartable = restartable;

        this.id = seed++;
    }

    public int getId() {
        return id;
    }

    public boolean isRestartable() {
        return restartable;
    }

    public void setRestartable(boolean restartable) {
        this.restartable = restartable;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}