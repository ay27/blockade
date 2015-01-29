package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.permission.UpgradeSystemPermission;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
import bitman.ay27.blockade.service.DaemonService;
import bitman.ay27.blockade.utils.SettingsUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import dalvik.system.DexFile;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends Activity {

    @InjectView(R.id.main_adb_switch)
    Switch adbSwitch;
    @InjectView(R.id.main_autoBoot_switch)
    Switch autoBootSwitch;
    @InjectView(R.id.main_keyguard_switch)
    Switch keyguardSwitch;
    @InjectView(R.id.main_root_switch)
    Switch rootSwitch;
    @InjectView(R.id.main_set_passwd)
    View setPasswd;

    private CompoundButton.OnCheckedChangeListener adbSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean result = UpgradeSystemPermission.grantSystemPermission(UpgradeSystemPermission.PERMISSION_WRITE_SECURE_SETTINGS);
            if (result) {
                result = SettingsUtils.setADB(isChecked);
                if (result) {
                    Toast.makeText(MainActivity.this, "set adb success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "set adb failed", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(MainActivity.this, "require root perimission failed", Toast.LENGTH_LONG).show();
            }

        }
    };
    private CompoundButton.OnCheckedChangeListener rootSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                boolean result = UpgradeSystemPermission.upgradeRootPermission();
                if (result) {
                    Toast.makeText(MainActivity.this, "open root success", Toast.LENGTH_LONG).show();
                    adbSwitch.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, "open root failed", Toast.LENGTH_LONG).show();
                    adbSwitch.setEnabled(false);
                    rootSwitch.setChecked(false);
                }
            } else {
                Toast.makeText(MainActivity.this, "root close", Toast.LENGTH_LONG).show();
                adbSwitch.setEnabled(false);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

        adbSwitch.setChecked(PreferenceUtils.read(KeySet.ADBEnable, false));
        autoBootSwitch.setChecked(PreferenceUtils.read(KeySet.AutoBoot, false));
        keyguardSwitch.setChecked(PreferenceUtils.read(KeySet.KeyguardEnable, false));
        rootSwitch.setChecked(PreferenceUtils.read(KeySet.RootEnable, false));

        adbSwitch.setEnabled(rootSwitch.isChecked());
        adbSwitch.setChecked(rootSwitch.isChecked() && adbSwitch.isChecked());

        adbSwitch.setOnCheckedChangeListener(adbSwitchListener);
        autoBootSwitch.setOnCheckedChangeListener(generateCheckedChangeListener(KeySet.AutoBoot));
        keyguardSwitch.setOnCheckedChangeListener(generateCheckedChangeListener(KeySet.KeyguardEnable));
        rootSwitch.setOnCheckedChangeListener(rootSwitchListener);


        startService(new Intent(this, DaemonService.class));
    }

    private CompoundButton.OnCheckedChangeListener generateCheckedChangeListener(final KeySet key) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceUtils.write(key, isChecked);
            }
        };
    }

}
