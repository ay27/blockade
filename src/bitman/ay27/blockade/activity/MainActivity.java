package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.service.KeyguardService;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {

    @InjectView(R.id.main_adb_switch)
    Switch adbSwitch;
    @InjectView(R.id.main_autoBoot_switch)
    Switch autoBootSwitch;
    @InjectView(R.id.main_keyguard_switch)
    Switch keyguardSwitch;
    @InjectView(R.id.main_set_passwd)
    View setPasswd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

//        SharedPreferences aa = PreferenceManager.getDefaultSharedPreferences(this);



//        UpgradeSystemPermission.upgradeRootPermission(getPackageCodePath());
//
//        try {
//            int flag = Settings.Global.getInt(getContentResolver(), Settings.Global.ADB_ENABLED);
//            Settings.Global.putInt(getContentResolver(), Settings.Global.ADB_ENABLED, 0);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }


    }

}
