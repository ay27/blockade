package bitman.ay27.blockade.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
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
import butterknife.OnClick;

public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.main_adb_switch)
    Switch adbSwitch;
    @InjectView(R.id.main_autoBoot_switch)
    Switch autoBootSwitch;
    @InjectView(R.id.main_keyguard_switch)
    Switch keyguardSwitch;
    @InjectView(R.id.main_root_switch)
    Switch rootSwitch;
    @InjectView(R.id.main_app_lock_switch)
    Switch appLockSwitch;
    @InjectView(R.id.main_set_passwd)
    View setPasswd;
    @InjectView(R.id.main_app_lock)
    View appLock;
    @InjectView(R.id.main_toolbar)
    Toolbar toolbar;

    private CompoundButton.OnCheckedChangeListener adbSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PreferenceUtils.write(KeySet.ADBEnable, isChecked);
            boolean result = UpgradeSystemPermission.grantSystemPermission(UpgradeSystemPermission.PERMISSION_WRITE_SECURE_SETTINGS);
            if (result) {
                result = SettingsUtils.setADB(isChecked);
                if (result) {
                    Toast.makeText(MainActivity.this, "set adb success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "set adb failed", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "require root perimission failed", Toast.LENGTH_SHORT).show();
            }

        }
    };
    private CompoundButton.OnCheckedChangeListener rootSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PreferenceUtils.write(KeySet.RootEnable, isChecked);
            if (isChecked) {
                boolean result = UpgradeSystemPermission.upgradeRootPermission();
                if (result) {
                    Toast.makeText(MainActivity.this, "open root success", Toast.LENGTH_SHORT).show();
                    adbSwitch.setEnabled(true);
                } else {
                    Toast.makeText(MainActivity.this, "open root failed", Toast.LENGTH_SHORT).show();
                    adbSwitch.setEnabled(false);
                    rootSwitch.setChecked(false);
                }
            } else {
                Toast.makeText(MainActivity.this, "root close", Toast.LENGTH_SHORT).show();
                adbSwitch.setEnabled(false);
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener keyguardSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PreferenceUtils.write(KeySet.KeyguardEnable, isChecked);
            if (PreferenceUtils.read(KeySet.NumberPassword, "").isEmpty()) {
                Toast.makeText(MainActivity.this, "please set your passwd", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, SetPasswdActivity.class));
            }
        }
    };
    private CompoundButton.OnCheckedChangeListener appLockSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PreferenceUtils.write(KeySet.AppLockEnable, isChecked);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Blockade");
        setSupportActionBar(toolbar);

        adbSwitch.setChecked(PreferenceUtils.read(KeySet.ADBEnable, false));
        autoBootSwitch.setChecked(PreferenceUtils.read(KeySet.AutoBoot, false));
        keyguardSwitch.setChecked(PreferenceUtils.read(KeySet.KeyguardEnable, false));
        rootSwitch.setChecked(PreferenceUtils.read(KeySet.RootEnable, false));
        appLockSwitch.setChecked(PreferenceUtils.read(KeySet.AppLockEnable, false));

        adbSwitch.setEnabled(rootSwitch.isChecked());
        adbSwitch.setChecked(rootSwitch.isChecked() && adbSwitch.isChecked());

        adbSwitch.setOnCheckedChangeListener(adbSwitchListener);
        autoBootSwitch.setOnCheckedChangeListener(generateCheckedChangeListener(KeySet.AutoBoot));
        keyguardSwitch.setOnCheckedChangeListener(keyguardSwitchListener);
        rootSwitch.setOnCheckedChangeListener(rootSwitchListener);
        appLockSwitch.setOnCheckedChangeListener(appLockSwitchListener);

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

    @OnClick(R.id.main_set_passwd)
    public void click(View view) {
        startActivity(new Intent(this, SetPasswdActivity.class));
    }

    @OnClick(R.id.main_app_lock)
    public void app_lock_click(View view) {
        startActivity(new Intent(this, AppLockSettingActivity.class));
    }
}
