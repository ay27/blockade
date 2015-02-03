package bitman.ay27.blockade.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import bitman.ay27.blockade.R;
import bitman.ay27.blockade.widget.DrawView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import java.util.List;

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
//    @InjectView(R.id.image_keyguard_bitmap_btn)
//    Button bitmapBtn;

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

        double similarity = getSimilarity(bitmap1, bitmap2);

//        double similarity = new ContrastWorker(circle1, circle2, dot1, dot2, line1, line2).getSimilarity();

        new AlertDialog.Builder(this)
                .setTitle("result")
                .setMessage("the similarity of two images is " + similarity)
                .setNegativeButton("ok", null)
                .show();
    }

    private double getSimilarity(Bitmap bitmap1, Bitmap bitmap2) {
        int[] t1 = new int[bitmap1.getWidth() * bitmap1.getHeight()];
        int[] t2 = new int[bitmap2.getWidth() * bitmap2.getHeight()];

        bitmap1.getPixels(t1, 0, bitmap1.getWidth(), 0, 0, bitmap1.getWidth(), bitmap1.getHeight());
        bitmap2.getPixels(t2, 0, bitmap2.getWidth(), 0, 0, bitmap2.getWidth(), bitmap2.getHeight());


        double delta = 0;
        double sum = 0;
        for (int i = 0; i < Math.min(t1.length, t2.length); i++) {
            if ((t1[i] | t2[i]) != 0) {
                sum++;
            }
            if ((t1[i] & t2[i]) != 0) {
                delta++;
            }
        }

        return delta / sum * 100.0;
    }

//    @OnClick(R.id.image_keyguard_bitmap_btn)
//    public void bitmapClick(View v) {
//        Bitmap bitmap = drawView.getBitmap();
//        drawView.setBackground(new BitmapDrawable(bitmap));
//        int[] temp = new int[bitmap.getWidth() * bitmap.getHeight()];
//        bitmap.getPixels(temp, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
//        Log.i("point", " start ");
//        int[] tt = new int[temp.length];
//        for (int i = 0; i < temp.length; i++) {
//            tt[i] = temp[i];
////            if (temp[i] != 0)
////                Log.i("point", ""+i+" "+temp[i]);
//        }
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_keyguard);
        ButterKnife.inject(this);
    }


    private static class ContrastWorker {
        private static final int MULTIPLE = 50;
        private static final int ACCEPT_RANGE = 50;
        private static final int[] Weights;

        static {
            Weights = new int[ACCEPT_RANGE + 2];
            int delta = 100 / ACCEPT_RANGE;
            Weights[0] = 100;
            for (int i = 1; i < ACCEPT_RANGE + 2; i++) {
                Weights[i] = 100 - delta * (i - 1);
            }
        }

        private List<Point> dot1, dot2;
        private List<Pair<Point, Point>> line1, line2;
        private List<Pair<Point, Integer>> circle1, circle2;

        public ContrastWorker(List<Pair<Point, Integer>> circle1, List<Pair<Point, Integer>> circle2, List<Point> dot1, List<Point> dot2, List<Pair<Point, Point>> line1, List<Pair<Point, Point>> line2) {
            this.circle1 = circle1;
            this.circle2 = circle2;
            this.dot1 = dot1;
            this.dot2 = dot2;
            this.line1 = line1;
            this.line2 = line2;
        }

        private static int calcWeight(Point p1, Point p2) {
            int dis = (int) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
            if (dis > ACCEPT_RANGE) {
                return Weights[ACCEPT_RANGE + 1];
            }
            return Weights[dis];
        }

        public double getSimilarity() {
            double result = 0;

            for (int i = 0; i < Math.min(dot1.size(), dot2.size()); i++) {
                Point p1 = dot1.get(i), p2 = dot2.get(i);
                result += calcWeight(fuzzy(p1), fuzzy(p2));
            }

            for (int i = 0; i < Math.min(line1.size(), line2.size()); i++) {
                Point start1 = line1.get(i).first, start2 = line2.get(i).first;
                Point end1 = line1.get(i).second, end2 = line2.get(i).second;

                result += calcWeight(fuzzy(start1), fuzzy(start2)) * calcWeight(fuzzy(end1), fuzzy(end2)) / 100;
            }

            for (int i = 0; i < Math.min(circle1.size(), circle2.size()); i++) {
                Point c1 = circle1.get(i).first, c2 = circle2.get(i).first;
                int r1 = circle1.get(i).second, r2 = circle2.get(i).second;

                int rWeight = Math.abs(r1 - r2) * 2 > ACCEPT_RANGE ? 0 : Weights[Math.abs(r1 - r2) * 2];
                result += calcWeight(fuzzy(c1), fuzzy(c2)) * rWeight / 100;
            }

            double sum = dot1.size() + line1.size() + circle1.size();
            sum += Math.abs(dot1.size() - dot2.size()) + Math.abs(line1.size() - line2.size()) + Math.abs(circle1.size() - circle2.size());

            return result / sum;
        }

        private Point fuzzy(Point p) {
            return new Point(p.x / MULTIPLE, p.y / MULTIPLE);
        }
    }

}
