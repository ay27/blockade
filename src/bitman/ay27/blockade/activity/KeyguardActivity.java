package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import bitman.ay27.blockade.R;
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
 * Created by ay27 on 15/1/23.
 */
public class KeyguardActivity extends Activity {


    @InjectViews({R.id.keyguard_edt_1, R.id.keyguard_edt_2, R.id.keyguard_edt_3, R.id.keyguard_edt_4})
    List<EditText> edts;
    @InjectView(R.id.error_pwd_txv)
    TextView errorTxv;
    @InjectView(R.id.keyguard_keyboard)
    RandomKeyboard keyboard;

    View view;
    WindowManager wm;
    private RandomKeyboard.NumberClickListener keyboardListener = new RandomKeyboard.NumberClickListener() {
        @Override
        public void onClick(View v, String value) {

            errorTxv.setVisibility(View.INVISIBLE);

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

        errorTxv.setVisibility(View.VISIBLE);

        String settedPasswd = PreferenceUtils.read(KeySet.KeyguardPasswd, "");

        if (passwd.equals(settedPasswd)) {
            errorTxv.setBackgroundResource(R.color.green_1);
            errorTxv.setText("success");
            finishMySelf();
        } else {
            errorTxv.setBackgroundResource(R.color.red_1);
            errorTxv.setText("failed");
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
                wm.removeViewImmediate(view);
                finish();
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = getLayoutInflater().inflate(R.layout.lock_screen, null);

        ButterKnife.inject(this, view);

        keyboard.registerListener(keyboardListener);
        keyboard.randomIt();
        errorTxv.setVisibility(View.INVISIBLE);

        wm = (WindowManager) getApplicationContext().getSystemService("window");
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        /**
         *以下都是WindowManager.LayoutParams的相关属性
         * 具体用途请参考SDK文档
         */

        final int PARAMS = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD // | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;

        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;   //这里是关键，你也可以试试2003
        wmParams.format = PixelFormat.OPAQUE;
        /**
         *这里的flags也很关键
         *代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
         *40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
         */
        wmParams.flags = PARAMS;
        wmParams.width = wmParams.MATCH_PARENT;
        wmParams.height = wmParams.MATCH_PARENT;
        wm.addView(view, wmParams);  //创建View
    }
}
