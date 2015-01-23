package bitman.ay27.blockade;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * Created by ay27 on 15/1/22.
 */
public class LockLayer {
    private Activity mActivty;
    private WindowManager mWindowManager;
    private View mLockView;
    private LayoutParams mLockViewLayoutParams;
    private static LockLayer mLockLayer;
    private boolean isLocked;

    public static synchronized LockLayer getInstance(Activity act){
        if(mLockLayer == null){
            mLockLayer = new LockLayer(act);
        }
        return mLockLayer;
    }

    private LockLayer(Activity act) {
        mActivty = act;
        init();
    }

    private void init(){
        isLocked = false;
        mWindowManager = mActivty.getWindowManager();
        mLockViewLayoutParams = new LayoutParams();
        mLockViewLayoutParams.width = LayoutParams.MATCH_PARENT;
        mLockViewLayoutParams.height = LayoutParams.MATCH_PARENT;
        //实现关键
        mLockViewLayoutParams.type = LayoutParams.TYPE_SYSTEM_ERROR;
        //apktool value，这个值具体是哪个变量还请网友帮忙
//        mLockViewLayoutParams.flags = LayoutParams.FLAG_DISMISS_KEYGUARD;

        mLockViewLayoutParams.flags = 1280;
        //1280=LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_LAYOUT_IN_SCREEN
    }
    public synchronized void lock() {
        if(mLockView!=null&&!isLocked){
            mWindowManager.addView(mLockView, mLockViewLayoutParams);
        }
        isLocked = true;
    }
    public synchronized void unlock() {
        if(mWindowManager!=null&&isLocked){
            mWindowManager.removeView(mLockView);
        }
        isLocked = false;
    }
    public synchronized void setLockView(View v){
        mLockView = v;
    }
}