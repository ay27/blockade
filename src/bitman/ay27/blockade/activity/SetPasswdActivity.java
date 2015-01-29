package bitman.ay27.blockade.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/29.
 */
public class SetPasswdActivity extends ActionBarActivity {

    @InjectView(R.id.set_passwd_input)
    EditText passwdInput;
    @InjectView(R.id.set_passwd_ok)
    Button okBtn;
    @InjectView(R.id.set_passwd_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_passwd);
        ButterKnife.inject(this);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("SetPasswd");
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.set_passwd_ok)
    public void okClick(View view) {
        if (passwdInput.getText().toString().length() == 4) {
            try {
                PreferenceUtils.write(KeySet.KeyguardPasswd, passwdInput.getText().toString());
//                PreferenceUtils.write(KeySet.KeyguardPasswd, "good");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(SetPasswdActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }

//            try {
//                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString(KeySet.KeyguardPasswd.name(), passwdInput.getText().toString());
//                editor.commit();
//            } catch (Exception e) {
//                e.printStackTrace();
//                Toast.makeText(SetPasswdActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
//            }

            Toast.makeText(this, "change passwd success", Toast.LENGTH_SHORT).show();

            this.finish();
        }
    }
}
