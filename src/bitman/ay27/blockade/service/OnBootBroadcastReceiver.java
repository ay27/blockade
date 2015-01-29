package bitman.ay27.blockade.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/25.
 */
public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        boolean enable = PreferenceUtils.read(KeySet.AutoBoot, false);

        if (enable) {
            Intent newIntent = new Intent(context, DaemonService.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(newIntent);
        }
    }
}
