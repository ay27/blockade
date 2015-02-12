package bitman.ay27.blockade.widget;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/10.
 */
public class DrawView extends View {
    private static final String TAG = "DrawView";
    private static final Paint paint, dotPaint;

    static {
        paint = new Paint();
        paint.setStrokeWidth(16);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        dotPaint = new Paint();
        dotPaint.setStrokeWidth(16);
        dotPaint.setColor(Color.BLUE);
        dotPaint.setStyle(Paint.Style.STROKE);
    }

    private ArrayList<LinePoint> line;
    private ArrayList<ArrayList<LinePoint>> lines;
    private ArrayList<LinePoint> dots;
    private long initTime = -1;

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private static void drawLine(Canvas canvas, List<LinePoint> line) {
        if (line.size() <= 1) return;

        Path path = new Path();
        path.reset();
        Point first = line.get(0).point;
        path.moveTo(first.x, first.y);
        for (int i = 1; i < line.size(); i++) {
            Point pre = line.get(i - 1).point;
            Point now = line.get(i).point;
            path.lineTo(now.x, now.y);
//            path.quadTo(pre.x, pre.y, (pre.x + now.x) / 2, (pre.y + now.y) / 2);
        }
        canvas.drawPath(path, paint);
    }

    private void init() {
        initTime = -1;
        lines = new ArrayList<ArrayList<LinePoint>>();
        line = new ArrayList<LinePoint>();
        dots = new ArrayList<LinePoint>();
    }

    public ArrayList<ArrayList<LinePoint>> getLines() {
        return lines;
    }

    public void clean() {
        init();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (initTime == -1) {
            initTime = System.currentTimeMillis();
        }

        //=====  add the point to current line
        Point currentPoint = new Point((int) event.getX(), (int) event.getY());
        int time = (int) (System.currentTimeMillis() - initTime);
//        if (line.size() >= 1 && near(currentPoint, line.get(line.size() - 1).point)) {
//            line.add(bindDot(new LinePoint(currentPoint, time), line.get(line.size() - 1)));
//        } else {
            line.add(new LinePoint(currentPoint, time));
//        }
        //=====

        Log.i(TAG, "time = " + time + " point=" + currentPoint.x + "," + currentPoint.y);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                lines.add(line);
                line = new ArrayList<LinePoint>();
                break;
        }

        this.invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (ArrayList<LinePoint> oneLine : lines) {
            drawLine(canvas, oneLine);
        }
        drawLine(canvas, line);

        if (dots.size()>=1) {
            for (LinePoint point : dots) {
                canvas.drawPoint(point.point.x, point.point.y, dotPaint);
            }
        }

    }

    public void drawDenoiseLine(ArrayList<ArrayList<LinePoint>> lines1, ArrayList<ArrayList<LinePoint>> lines2) {
        lines = new ArrayList<ArrayList<LinePoint>>();
        lines.add(lines1.get(0));
        lines.add(lines2.get(0));

        invalidate();
    }

    public void drawInflectionPoint(ArrayList<LinePoint> inflectionPoint, ArrayList<LinePoint> inflectionPoint1) {
        dots = new ArrayList<LinePoint>();
        dots.addAll(inflectionPoint);
        dots.addAll(inflectionPoint1);

        invalidate();
    }


    public static class LinePoint {
        public int time;
        public Point point;

        public LinePoint(Point point, int time) {
            this.point = point;
            this.time = time;
        }

        public LinePoint(LinePoint other) {
            this.time = other.time;
            this.point = new Point(other.point);
        }

    }
}
