package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.widget.DrawView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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

    //    private List<Point> dot1, dot2;
//    private List<Pair<Point, Point>> line1, line2;
//    private List<Pair<Point, Integer>> circle1, circle2;
    private Bitmap bitmap1;
    private Bitmap bitmap2;

    @OnClick(R.id.image_keyguard_clear_btn)
    public void clearClick(View v) {
        drawView.clear();
    }

    @OnClick(R.id.image_keyguard_ok_btn)
    public void okClick(View v) {
//        dot1 = drawView.getDots();
//        line1 = drawView.getLines();
//        circle1 = drawView.getCircles();

        bitmap1 = drawView.getBitmap();

        drawView.clear();
    }

    @OnClick(R.id.image_keyguard_check_btn)
    public void checkClick(View v) {
//        dot2 = drawView.getDots();
//        line2 = drawView.getLines();
//        circle2 = drawView.getCircles();

        bitmap2 = drawView.getBitmap();

        double similarity = new ContrastWorker(bitmap1, bitmap2).getSimilarity();

//        double similarity = new ContrastWorker(circle1, circle2, dot1, dot2, line1, line2).getSimilarity();

        new AlertDialog.Builder(this)
                .setTitle("result")
                .setMessage("the similarity of two images is " + similarity)
                .setNegativeButton("ok", null)
                .show();
    }


    @OnClick(R.id.image_keyguard_bitmap_btn)
    public void bitmapClick(View v) {
        Bitmap bitmap = drawView.getBitmap();

        drawView.clear();

        drawView.setBackground(new BitmapDrawable(bitmap));
        int[] temp = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(temp, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        Log.i("point", " start ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_keyguard);
        ButterKnife.inject(this);
    }


    private static class ContrastWorker {
        private Bitmap b1, b2;

        public ContrastWorker(Bitmap b1, Bitmap b2) {
            this.b1 = b1;
            this.b2 = b2;
        }

        public double getSimilarity() {
            int[] t1 = new int[b1.getWidth() * b1.getHeight()];
            int[] t2 = new int[b2.getWidth() * b2.getHeight()];

            b1.getPixels(t1, 0, b1.getWidth(), 0, 0, b1.getWidth(), b1.getHeight());
            b2.getPixels(t2, 0, b2.getWidth(), 0, 0, b2.getWidth(), b2.getHeight());

            double delta = 0;
//            double sum = 0;
            double min1 = Integer.MAX_VALUE, min2 = Integer.MAX_VALUE;
            double max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE;
            for (int i = 0; i < Math.min(t1.length, t2.length); i++) {

                min1 = Math.min(min1, t1[i]);
                max1 = Math.max(max1, t1[i]);
                min2 = Math.min(min2, t2[i]);
                max2 = Math.max(max2, t2[i]);

//                if ((t1[i] | t2[i]) != 0) {
//                    sum++;
//                }
                if ((t1[i] & t2[i]) != 0) {
                    delta += Math.abs(t1[i]-t2[i]);
                }
            }

            return (1 - (delta / (Math.max(Math.abs(min1-max1), Math.abs(min2-max2)))))*100.0;

//            return delta / sum * 100.0;
        }
    }

}
