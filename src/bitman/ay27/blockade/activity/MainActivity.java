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
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
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
    @InjectView(R.id.main_root_switch)
    Switch rootSwitch;
    @InjectView(R.id.main_set_passwd)
    View setPasswd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);

        adbSwitch.setChecked(PreferenceUtils.read(KeySet.ADBEnable, false));
        adbSwitch.setOnCheckedChangeListener(generateCheckedChangeListener(KeySet.ADBEnable));

        autoBootSwitch.setChecked(PreferenceUtils.read(KeySet.AutoBoot, false));
        autoBootSwitch.setOnCheckedChangeListener(generateCheckedChangeListener(KeySet.AutoBoot));

        keyguardSwitch.setChecked(PreferenceUtils.read(KeySet.KeyguardEnable, false));
        keyguardSwitch.setOnCheckedChangeListener(generateCheckedChangeListener(KeySet.KeyguardEnable));

        rootSwitch.setChecked(PreferenceUtils.read(KeySet.RootEnable, false));
        rootSwitch.setOnCheckedChangeListener(generateCheckedChangeListener(KeySet.RootEnable));

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
