package bitman.ay27.blockade.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/1/30.
 */
public class DrawView_old extends View {

    private static final String TAG = "DrawView";
    private int POINT_WIDTH = 24;
    private Paint paint;
    private ArrayList<Pair<Point, Integer>> line;
    private ArrayList<List<Pair<Point, Integer>>> lines;
    private long initTime = -1;
    private static final int DEFAULT_COLOR = Integer.parseInt("22eeeeee", 16);

    public DrawView_old(Context context) {
        super(context);
        POINT_WIDTH = (int) (POINT_WIDTH * getContext().getResources().getDisplayMetrics().density);
        init();
    }

    public DrawView_old(Context context, AttributeSet attrs) {
        super(context, attrs);
        POINT_WIDTH = (int) (POINT_WIDTH * getContext().getResources().getDisplayMetrics().density);
        init();
    }

    public DrawView_old(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        POINT_WIDTH = (int) (POINT_WIDTH * getContext().getResources().getDisplayMetrics().density);
        init();
    }

    private static void setPaint(final Paint paint, final int width, Paint.Style style, final int color) {
        paint.setStrokeWidth(width);
        paint.setStyle(style);
        paint.setColor(color);
    }

    public void clear() {
        this.init();
        invalidate();
    }

    private void init() {
        line = new ArrayList<Pair<Point, Integer>>();
        lines = new ArrayList<List<Pair<Point, Integer>>>();
        initTime = -1;
        paint = new Paint();
        setPaint(paint, POINT_WIDTH, Paint.Style.FILL, DEFAULT_COLOR);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawMySelf(canvas, false);
    }

    public void drawMySelf(Canvas canvas, boolean timeFactor) {

        //----draw lines-----
        for (List<Pair<Point, Integer>> oneLine : lines) {
            draw_a_line(canvas, oneLine, timeFactor);
        }

        //-----draw current line------
        if (line.isEmpty())
            return;

        draw_a_line(canvas, line, timeFactor);
    }

    private void draw_a_line(Canvas canvas, List<Pair<Point, Integer>> oneLine, boolean timeFactor) {
        if (!timeFactor) {
            paint.setColor(DEFAULT_COLOR);
            canvas.drawCircle(oneLine.get(0).first.x, oneLine.get(0).first.y, POINT_WIDTH / 2, paint);
            for (int i = 1; i < oneLine.size(); i++) {
                Point point = oneLine.get(i).first;
                if (near(point, oneLine.get(i-1).first)) {
                    canvas.drawCircle(point.x, point.y, POINT_WIDTH/2, paint);
                }
                else {
                    canvas.drawLine(point.x, point.y, oneLine.get(i - 1).first.x, oneLine.get(i - 1).first.y, paint);
                }
            }
            return;
        }

        paint.setColor(oneLine.get(0).second);
        canvas.drawCircle(oneLine.get(0).first.x, oneLine.get(0).first.y, POINT_WIDTH / 2, paint);
        for (int i = 1; i < oneLine.size(); i++) {
            Point point = oneLine.get(i).first;
            int time = oneLine.get(i).second;
            paint.setColor(time);
            if (near(point, oneLine.get(i-1).first)) {
                canvas.drawCircle(point.x, point.y, POINT_WIDTH/2, paint);
            }
            else {
                canvas.drawLine(point.x, point.y, oneLine.get(i - 1).first.x, oneLine.get(i - 1).first.y, paint);
            }
        }
    }

    private boolean near(Point p1, Point p2) {
        return (p1.x-p2.x)*(p1.x-p2.x)+(p1.y-p2.y)*(p1.y-p2.y)<=(POINT_WIDTH/2)*(POINT_WIDTH/2);
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawMySelf(canvas, true);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (initTime == -1) {
            initTime = System.currentTimeMillis();
        }

        Point currentPoint = new Point((int) event.getX(), (int) event.getY());
        int delta = (int) (System.currentTimeMillis() - initTime) + (int)4278190080L;
        line.add(new Pair<Point, Integer>(currentPoint, delta));
        Log.i(TAG, "delta = "+delta);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                line.add(new Pair<Point, Integer>(currentPoint, (int) (System.currentTimeMillis() - initTime)));
                break;
            case MotionEvent.ACTION_UP:
                lines.add(line);
                line = new ArrayList<Pair<Point, Integer>>();
                break;
        }

        this.invalidate();
        return true;
    }
}