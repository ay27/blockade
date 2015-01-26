package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.service.KeyguardService;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {

    @InjectView(R.id.main_togBtn)
    ToggleButton togBtn;
    private Intent screenLockIntent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

//        UpgradeSystemPermission.upgradeRootPermission(getPackageCodePath());
//
//        try {
//            int flag = Settings.Global.getInt(getContentResolver(), Settings.Global.ADB_ENABLED);
//            Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }

        ButterKnife.inject(this);

        togBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(screenLockIntent = new Intent(MainActivity.this, KeyguardService.class));
                } else if (screenLockIntent != null) {
                    stopService(screenLockIntent);
                }
            }
        });

    }

}
