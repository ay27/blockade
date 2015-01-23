package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import bitman.ay27.blockade.HomeWatcherReceiver;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.service.LockService;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/21.
 */
public class LockScreenActivity extends Activity {

    private static final String TAG = "ActivityDemo";
    public static boolean isActivity = false;
    public static boolean isLock = false;
    public static LockScreenActivity instance = null;
    public static boolean gotoRealHome = false;

    @InjectViews({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6})
    List<Button> btns;
    @InjectViews({R.id.edt_1, R.id.edt_2, R.id.edt_3, R.id.edt_4})
    List<EditText> edts;
    @InjectView(R.id.error_pwd_txv)
    TextView errorPasswdTxv;
    private Intent lockOtherApp = null;



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
            gotoRealHome = true;
            gotoRealHome();
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

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final int PARAMS = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD // | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
        getWindow().setFlags(PARAMS,
                PARAMS);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        setContentView(R.layout.lock_screen);
        ButterKnife.inject(this);
        instance = this;

        isLock = true;

        if (gotoRealHome) {
            gotoRealHome();
        }



        startService(lockOtherApp = new Intent(this, LockService.class));

        Log.e(TAG, "start onCreate~~~");
    }

    private void disableStatusBar() {
        try {
            Object service = getSystemService("statusbar");
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");

            Field disable_mask = statusbarManager.getField("DISABLE_EXPAND");
            Class<?>[] clss = new Class[1];
            clss[0] = int.class;
            Method disable = statusbarManager.getMethod("disable", clss);
//            int[] ints = new int[1];
//            ints[0] = disable_mask.getInt(service);
            disable.invoke(service, disable_mask.getInt(service));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoRealHome() {

        isLock = false;

        List<String> pkgNamesT = new ArrayList<String>();
        List<String> actNamesT = new ArrayList<String>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfos = this.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (int i = 0; i < resolveInfos.size(); i++) {
            String string = resolveInfos.get(i).activityInfo.packageName;
            if (!string.equals(this.getPackageName())) {//排除自己的包名
                pkgNamesT.add(string);
                string = resolveInfos.get(i).activityInfo.name;
                actNamesT.add(string);
            }
        }

        Log.e(TAG, "before start to real home");

        intent = new Intent();
        ComponentName name = new ComponentName(pkgNamesT.get(0), actNamesT.get(0));
//
//        ComponentName topActivity = ((ActivityManager) getSystemService("activity")).getRunningTasks(1).get(0).topActivity;
//        if (topActivity.getClassName().equals(name.getClassName())) {
//
//        }

        intent.setComponent(name);
        startActivity(intent);

        Log.e(TAG, "before finish this");

        this.finish();

        Log.e(TAG, "after finish");
    }

    @Override
    protected void onResume() {

        isActivity = true;

        Log.e(TAG, "start onResume~~~");

        super.onResume();
    }

    @Override
    protected void onPause() {

        isActivity = false;
        Log.e(TAG, "start onPause~~~");

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        Log.e(TAG, "start onDestroy~~~");

        if (lockOtherApp != null)
            stopService(lockOtherApp);

        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "start onStart~~~");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "start onRestart~~~");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "start onStop~~~");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
