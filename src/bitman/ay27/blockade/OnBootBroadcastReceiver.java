package bitman.ay27.blockade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import bitman.ay27.blockade.service.DaemonService;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/25.
 */
public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "boot completed action has got", Toast.LENGTH_LONG).show();

        Intent newIntent = new Intent(context, DaemonService.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startService(newIntent);
    }
}
