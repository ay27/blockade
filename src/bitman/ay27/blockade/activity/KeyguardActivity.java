package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import bitman.ay27.blockade.R;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/23.
 */
public class KeyguardActivity extends Activity {


    @InjectViews({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6})
    List<Button> btns;
    @InjectViews({R.id.edt_1, R.id.edt_2, R.id.edt_3, R.id.edt_4})
    List<EditText> edts;
    @InjectView(R.id.error_pwd_txv)
    TextView errorPasswdTxv;

    View view;

    WindowManager wm;

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6})
    public void numberBtnClick(Button numBtn) {
        for (int i = 0; i < edts.size(); i++) {
            EditText editText = edts.get(i);
            if (editText.getText() == null || editText.getText().toString() == null || editText.getText().toString().isEmpty()) {
                editText.setText(numBtn.getText());

                if (i == edts.size() - 1) {
                    checkPasswd();
                    return;
                }

                break;
            }
        }
    }

    private void checkPasswd() {
        String passwd = "";
        for (EditText editText : edts) {
            passwd += editText.getText().toString();
        }

        if (passwd.equals("1234")) {
            errorPasswdTxv.setText("success");
            errorPasswdTxv.setVisibility(View.VISIBLE);

            wm.removeViewImmediate(view);
            this.finish();

        } else {
            errorPasswdTxv.setText("error, try again");
            errorPasswdTxv.setVisibility(View.VISIBLE);
            for (EditText editText : edts) {
                editText.setText("");
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = getLayoutInflater().inflate(R.layout.lock_screen, null);

        ButterKnife.inject(this, view);

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
