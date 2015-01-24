package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import bitman.ay27.blockade.HomeWatcherReceiver;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.UpgradeSystemPermission;
import bitman.ay27.blockade.service.ScreenLockService;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {

    private Intent lockOtherApp = null;

    @InjectView(R.id.main_togBtn)
    ToggleButton togBtn;
    private Intent screenLockIntent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        UpgradeSystemPermission.upgradeRootPermission(getPackageCodePath());

        try {
            int flag = Settings.Global.getInt(getContentResolver(), Settings.Global.ADB_ENABLED);
            Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        ButterKnife.inject(this);

        togBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(screenLockIntent = new Intent(MainActivity.this, ScreenLockService.class));
                }
                else if (screenLockIntent != null) {
                    stopService(screenLockIntent);
                }
            }
        });

//        registerHomeKeyReceiver(this);
    }

    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    private static void registerHomeKeyReceiver(Context context) {
        Log.i("home lock", "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private static void unregisterHomeKeyReceiver(Context context) {
        Log.i("home lock", "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }
}
