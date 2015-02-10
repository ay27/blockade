package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.widget.DrawView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import java.util.ArrayList;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/30.
 */
public class ImageKeyguardActivity extends Activity {

    @InjectView(R.id.draw_view)
    DrawView drawView;
    @InjectView(R.id.image_keyguard_check_btn)
    Button checkBtn;
    @InjectView(R.id.image_keyguard_ok_btn)
    Button okBtn;
    @InjectView(R.id.image_keyguard_clear_btn)
    Button clearBtn;
    @InjectView(R.id.image_keyguard_bitmap_btn)
    Button bitmapBtn;

    private View view;
    private WindowManager wm;
    private ArrayList<ArrayList<DrawView.LinePoint>> lines1, lines2;

    @OnClick(R.id.image_keyguard_ok_btn)
    public void okClick(View v) {
        lines1 = drawView.getLines();
        drawView.clean();
    }

    @OnClick(R.id.image_keyguard_check_btn)
    public void checkClick(View v) {
        lines2 = drawView.getLines();
        CurveProcessor processor = new CurveProcessor(lines1, lines2);
        new AlertDialog.Builder(this)
                .setTitle("result")
                .setMessage(""+processor.juxtapose())
                .show();
    }

    @OnClick(R.id.image_keyguard_clear_btn)
    public void clearClick(View v) {
        drawView.clean();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_keyguard);
        ButterKnife.inject(this);
    }


}
