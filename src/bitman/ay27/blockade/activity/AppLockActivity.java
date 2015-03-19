package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
import bitman.ay27.blockade.service.user_service.AppLockService;
import bitman.ay27.blockade.utils.TaskUtils;
import bitman.ay27.blockade.widget.RandomKeyboard;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/25.
 */
public class AppLockActivity extends Activity {

    @InjectViews({R.id.app_lock_edt_1, R.id.app_lock_edt_2, R.id.app_lock_edt_3, R.id.app_lock_edt_4})
    List<EditText> edts;
    @InjectView(R.id.app_lock_error_txv)
    TextView errorTxv;
    @InjectView(R.id.app_lock_keyboard)
    RandomKeyboard keyboard;

    private RandomKeyboard.NumberClickListener keyboardListener = new RandomKeyboard.NumberClickListener() {
        @Override
        public void onClick(View v, String value) {

            if (v.getId() == R.id.key_btn_back) {
                for (int i = edts.size() - 1; i >= 0; i--) {
                    if (!edts.get(i).getText().toString().isEmpty()) {
                        edts.get(i).setText("");
                        return;
                    }
                }
                return;
            } else if (v.getId() == R.id.key_btn_cancel) {
                for (EditText text : edts) {
                    text.setText("");
                }
                return;
            }

            for (int i = 0; i < edts.size(); i++) {
                EditText text = edts.get(i);
                if (text.getText().toString().isEmpty()) {
                    text.setText(value);
                    if (i == edts.size() - 1) {
                        checkPasswd();
                    }
                    return;
                }
            }
        }
    };

    private void checkPasswd() {
        String passwd = "";
        for (EditText text : edts) {
            passwd += text.getText().toString();
        }

        String settedPasswd = PreferenceUtils.read(KeySet.NumberPassword, "");
        if (settedPasswd.equals(""))
            settedPasswd = "1234";

        if (passwd.equals(settedPasswd)) {
            errorTxv.setBackgroundResource(R.color.green_1);
            errorTxv.setText("success");

            AppLockService.addUnlockApp(getIntent().getStringExtra("PackageName"));

            start2TargetActivity();
        } else {
            for (EditText text : edts) {
                text.setText("");
            }
        }
    }

    private void start2TargetActivity() {
        TaskUtils.executeAsyncTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                startActivity(getPackageManager().getLaunchIntentForPackage(getIntent().getStringExtra("PackageName")));
                finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_lock);
        ButterKnife.inject(this);

        keyboard.registerListener(keyboardListener);
        keyboard.randomIt();
    }

    @Override
    public void onBackPressed() {
        //============= start to launcher ==================
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(intent);
        //============= start to launcher ==================

        this.finish();
    }
}
