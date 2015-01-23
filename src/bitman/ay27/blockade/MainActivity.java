package bitman.ay27.blockade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import bitman.ay27.blockade.service.LockService;
import bitman.ay27.blockade.service.ScreenLockService;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private Intent lockOtherApp = null;

    @InjectView(R.id.main_togBtn)
    ToggleButton togBtn;
    private Intent screenLockIntent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        ButterKnife.inject(this);

        togBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(screenLockIntent = new Intent(MainActivity.this, ScreenLockService.class));
                }
                else if (screenLockIntent != null) {
                    stopService(screenLockIntent);
                }
            }
        });

//        startService(lockOtherApp = new Intent(this, LockService.class));

//        View lock = View.inflate(this, R.layout.main, null);
//        LockLayer lockLayer = LockLayer.getInstance(this);
//        lockLayer.setLockView(lock);
//        lockLayer.lock();

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
//                        .setTitle(null)
//                        .setMessage("hhaah")
//                        .setNeutralButton("ok", null)
//                        .setOnKeyListener(new DialogInterface.OnKeyListener() {
//                            @Override
//                            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                                switch (keyCode) {
//                                    case KeyEvent.KEYCODE_HOME:
//                                        Log.i("key", "home");
//                                        return true;
//
//                                    case KeyEvent.KEYCODE_BACK:
//                                        Log.i("key", "back");
//                                        return true;
//                                }
//                                return false;
//                            }
//                        })
//                        .setCancelable(false)
//                        .create();
//
//                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//                dialog.show();
////                startService(new Intent(MainActivity.this, ScreenLockService.class));
//            }
//        });

    }


//    @Override
//    protected void onDestroy() {
//
//        if (lockOtherApp != null)
//            stopService(lockOtherApp);
//
//        super.onDestroy();
//    }
}
