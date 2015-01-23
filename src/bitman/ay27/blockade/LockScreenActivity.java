package bitman.ay27.blockade;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
 * Created by ay27 on 15/1/21.
 */
public class LockScreenActivity extends Activity {

    public static boolean isActivity = false;
    public static boolean isLock = false;
    public static LockScreenActivity instance = null;
    public static boolean gotoRealHome = false;
    private static HomeWatcherReceiver mHomeKeyReceiver = null;
    @InjectViews({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6})
    List<Button> btns;
    @InjectViews({R.id.edt_1, R.id.edt_2, R.id.edt_3, R.id.edt_4})
    List<EditText> edts;
    @InjectView(R.id.error_pwd_txv)
    TextView errorPasswdTxv;
    private Intent lockOtherApp = null;

    private static void registerHomeKeyReceiver(Context context) {
        Log.i("home lock", "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private static void unregisterHomeKeyReceiver(Context context) {
        Log.i("home lock", "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }

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

//    @Override
//    public boolean onGenericMotionEvent(MotionEvent event) {
//
//
//                try {
//
//
//            Object service = getSystemService("statusbar");
//            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
////            Method test = statusbarManager.getMethod("collapse");
////            test.invoke(service);
//
////            Field disable_mask = statusbarManager.getField("DISABLE_MASK");
////            Method disable = statusbarManager.getMethod("disable");
////            disable.invoke(disable, disable_mask.getInt(null));
//
//            if (Build.VERSION.SDK_INT <= 16) {
//                Method collapse = statusbarManager.getMethod("collapse");
//                collapse.invoke(service);
//            } else {
//                Method collapse2 = statusbarManager.getMethod("collapsePanels");
//                collapse2.invoke(service);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//
//        return super.onTouchEvent(event);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final int PARAMS = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD // | WindowManager.LayoutParams.FLAG_FULLSCREEN
        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                ;
        getWindow().setFlags(PARAMS,
                PARAMS);
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
        setContentView(R.layout.lock_screen);
        ButterKnife.inject(this);
        instance = this;

        isLock = true;


//        View disableStatusBarView = new View(this);
//
//        disableStatusBarView.setEnabled(false);
//
//        WindowManager.LayoutParams handleParams = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.MATCH_PARENT,
//                600,
//        // This allows the view to be displayed over the status bar
//        WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
//                // this is to keep button presses going to the background window
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//                        // this is to enable the notification to recieve touch events
//                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//                        // Draws over status bar
//                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                PixelFormat.TRANSLUCENT);
//
//        handleParams.gravity = Gravity.TOP;
//
//        WindowManager winMgr = (WindowManager)getSystemService(WINDOW_SERVICE);
//        winMgr.addView(disableStatusBarView, handleParams);

//        this.getWindow().addContentView(disableStatusBarView, handleParams);


//        View lock = View.inflate(this, R.layout.lock_screen, null);
//        final LockLayer lockLayer = LockLayer.getInstance(this);
//        lockLayer.setLockView(lock);
//        lockLayer.lock();

//        disableStatusBar();

        if (gotoRealHome) {
            gotoRealHome();
        }

//        finish();

//        View lock = View.inflate(this, R.layout.lock_screen, null);
//        final LockLayer lockLayer = LockLayer.getInstance(this);
//        lockLayer.setLockView(lock);
//        lockLayer.lock();
//
//        Button unlock = (Button) lock.findViewById(R.id.unlock_btn);
//        unlock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lockLayer.unlock();
//            }
//        });


        registerHomeKeyReceiver(this);

        startService(lockOtherApp = new Intent(this, LockService.class));
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

        intent = new Intent();
        ComponentName name = new ComponentName(pkgNamesT.get(0), actNamesT.get(0));
        intent.setComponent(name);
        startActivity(intent);

        this.finish();
    }

    @Override
    protected void onResume() {

        isActivity = true;

        super.onResume();
    }

    @Override
    protected void onPause() {

        isActivity = false;

//        if (!gotoRealHome) {
//            startActivity(new Intent(this, LockScreenActivity.class));
////            this.onDestroy();
////            this.onStart();
//        }
//        else
            super.onPause();
    }

    @Override
    protected void onDestroy() {

        unregisterHomeKeyReceiver(this);

        if (lockOtherApp != null)
            stopService(lockOtherApp);

        super.onDestroy();
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


//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        // TODO Auto-generated method stub
//        super.onWindowFocusChanged(hasFocus);
//        try {
//
//
//            Object service = getSystemService("statusbar");
//            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
////            Method test = statusbarManager.getMethod("collapse");
////            test.invoke(service);
//
////            Field disable_mask = statusbarManager.getField("DISABLE_MASK");
////            Method disable = statusbarManager.getMethod("disable");
////            disable.invoke(disable, disable_mask.getInt(null));
//
//            if (Build.VERSION.SDK_INT <= 16) {
//                Method collapse = statusbarManager.getMethod("collapse");
//                collapse.invoke(service);
//            } else {
//                Method collapse2 = statusbarManager.getMethod("collapsePanels");
//                collapse2.invoke(service);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        final AlertDialog dialog = new AlertDialog.Builder(this)
//                .setTitle("hahah")
//                .create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
//        dialog.show();
//    }

    //    @Override
//    public void onAttachedToWindow() {
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ERROR);
//
//        super.onAttachedToWindow();
//
//    }
}
