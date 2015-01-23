package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import bitman.ay27.blockade.R;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/23.
 */
public class FloatView extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        Button bb=new Button(getApplicationContext());
//        bb.setText("hahaha");

        View view = getLayoutInflater().inflate(R.layout.lock_screen, null);
        WindowManager wm=(WindowManager)getApplicationContext().getSystemService("window");
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

        /**
         *以下都是WindowManager.LayoutParams的相关属性
         * 具体用途请参考SDK文档
         */
        wmParams.type=2002;   //这里是关键，你也可以试试2003
        wmParams.format=1;
        /**
         *这里的flags也很关键
         *代码实际是wmParams.flags |= FLAG_NOT_FOCUSABLE;
         *40的由来是wmParams的默认属性（32）+ FLAG_NOT_FOCUSABLE（8）
         */
        wmParams.flags=40;
        wmParams.width=wmParams.MATCH_PARENT;
        wmParams.height=wmParams.MATCH_PARENT;
        wm.addView(view, wmParams);  //创建View
    }
}
