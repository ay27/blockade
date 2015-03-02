package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import bitman.ay27.blockade.BlockadeApplication;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.orm.module.AppLockItem;
import bitman.ay27.blockade.preferences.KeySet;
import bitman.ay27.blockade.preferences.PreferenceUtils;
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

//            errorTxv.setVisibility(View.INVISIBLE);

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

            BlockadeApplication.tempStopLockList.add(new AppLockItem(getIntent().getStringExtra("PackageName")));

//            appLockService.addTempStop(getIntent().getStringExtra("PackageName"));

            finishMySelf();
        } else {
//            errorTxv.setBackgroundResource(R.color.red_1);
//            errorTxv.setText("failed");
            for (EditText text : edts) {
                text.setText("");
            }
        }
    }

    private void finishMySelf() {
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
                finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        View view = LayoutInflater.from(this).inflate(R.layout.app_lock, null);
//        setContentView(view);
        setContentView(R.layout.app_lock);
        ButterKnife.inject(this);

        keyboard.registerListener(keyboardListener);
        keyboard.randomIt();

//        bindService(new Intent(this,AppLockService.class), conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        finishMySelf();
        super.onBackPressed();
    }
}
