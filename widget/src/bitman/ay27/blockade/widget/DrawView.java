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
public class DrawView extends View {

    private static final Paint linePaint;
    private static final Paint circlePaint;
    private static final Paint dotsPaint;
    private static final Paint movingPaint;
    private static final int POINT_WIDTH = 50;
    private static final int POINT_NUMBER = 50;

    static {
        linePaint = new Paint();
        circlePaint = new Paint();
        dotsPaint = new Paint();
        movingPaint = new Paint();

        setPaint(linePaint, POINT_WIDTH, Paint.Style.FILL, Color.BLUE);
        setPaint(circlePaint, POINT_WIDTH / 2, Paint.Style.STROKE, Color.GREEN);
        setPaint(dotsPaint, POINT_WIDTH, Paint.Style.FILL, Color.BLACK);
        setPaint(movingPaint, POINT_WIDTH, Paint.Style.FILL, Color.YELLOW);
    }


    private static final String TAG = "DrawView";
    private ArrayList<Pair<Point, Point>> lines;
    private ArrayList<Pair<Point, Integer>> circles;
    private ArrayList<Point> dots;
    private ArrayList<Point> movingDrawingPoints;
    private Point startPoint;

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

    private static void setPaint(final Paint paint, final int width, Paint.Style style, final int color) {
        paint.setStrokeWidth(width);
        paint.setStyle(style);
        paint.setColor(color);
        paint.setAntiAlias(true);
    }

    public ArrayList<Pair<Point, Integer>> getCircles() {
        return circles;
    }

    public ArrayList<Point> getDots() {
        return dots;
    }

    public ArrayList<Pair<Point, Point>> getLines() {
        return lines;
    }

    public void clear() {
        this.init();
        invalidate();
    }

    private void init() {
        lines = new ArrayList<Pair<Point, Point>>();
        circles = new ArrayList<Pair<Point, Integer>>();
        dots = new ArrayList<Point>();
        movingDrawingPoints = new ArrayList<Point>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawMySelf(canvas);
    }

    public void drawMySelf(Canvas canvas) {

        //---draw dot ------------
        for (Point point : dots) {
            canvas.drawCircle(point.x, point.y, POINT_WIDTH/2, dotsPaint);
//            canvas.drawPoint(point.x, point.y, dotsPaint);
        }

        //----draw line-----------
        for (Pair<Point, Point> line : lines) {
            Point start = line.first, end = line.second;
            canvas.drawLine(start.x, start.y, end.x, end.y, linePaint);
            canvas.drawCircle(start.x, start.y, POINT_WIDTH/2, linePaint);
            canvas.drawCircle(end.x, end.y, POINT_WIDTH/2, linePaint);
        }

        //----draw circle---------
        for (Pair<Point, Integer> circle : circles) {
            Point center = circle.first;
            int r = circle.second;
            canvas.drawCircle(center.x, center.y, r, circlePaint);
        }

        //----draw moving line-----
        for (Point point : movingDrawingPoints) {
            canvas.drawCircle(point.x, point.y, POINT_WIDTH/2, movingPaint);
//            canvas.drawPoint(point.x, point.y, movingPaint);
        }
    }

    public Bitmap getBitmap() {
//        this.setDrawingCacheEnabled(true);
//        this.buildDrawingCache();
//        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
//        this.setDrawingCacheEnabled(false);

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        drawMySelf(canvas);

//        init();
//        this.invalidate();

        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point currentPoint = new Point((int) event.getX(), (int) event.getY());
        movingDrawingPoints.add(currentPoint);

//        switch (event.getAction() & MotionEvent.ACTION_MASK) {
//
//            case MotionEvent.ACTION_DOWN:
//                startPoint = new Point(currentPoint);
//                movingDrawingPoints.add(currentPoint);
//                break;
//
//            case MotionEvent.ACTION_UP:
//
//                // 拟合出一个圆
//                if (near(startPoint, currentPoint) && movingDrawingPoints.size() > POINT_NUMBER) {
//                    Pair<Point, Integer> temp = getCircle(movingDrawingPoints);
//                    circles.add(temp);
//                    Log.i(TAG, "circle: x = " + temp.first.x + " y = " + temp.first.y + " r = " + temp.second);
//                }
//                // 拟合出一个点
//                else if (near(startPoint, currentPoint) && movingDrawingPoints.size() < POINT_NUMBER) {
//                    Point temp = getDot(movingDrawingPoints);
//                    dots.add(temp);
//                    Log.i(TAG, "dot: x = " + temp.x + " y = " + temp.y);
//                }
//                // 拟合一条直线
//                else {
//                    Pair<Point, Point> temp = new Pair<Point, Point>(startPoint, currentPoint);
//                    lines.add(temp);
//                    Log.i(TAG, "line: sx = " + temp.first.x + " sy = " + temp.first.y + " ex = " + temp.second.x + " ey = " + temp.second.y);
//                }
//
//                movingDrawingPoints.clear();
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//
//        }

        this.invalidate();
        return true;
    }

    private Point getDot(List<Point> points) {
        int x = 0;
        int y = 0;
        for (Point point : points) {
            x += point.x;
            y += point.y;
        }
        return new Point(x / points.size(), y / points.size());
    }

    private Pair<Point, Integer> getCircle(List<Point> points) {
        Point p = getDot(points);
        double r = 0;
        for (Point point : points) {
            r += Math.sqrt(Math.pow(point.x - p.x, 2) + Math.pow(point.y - p.y, 2));
        }
        return new Pair<Point, Integer>(p, (int) (r / points.size()));
    }

    private Pair<Point, Integer> calcCircle(List<Point> points) {

        double X1 = 0;
        double Y1 = 0;
        double X2 = 0;
        double Y2 = 0;
        double X3 = 0;
        double Y3 = 0;
        double X1Y1 = 0;
        double X1Y2 = 0;
        double X2Y1 = 0;

        for (Point point : points) {
            X1 = X1 + point.x;
            Y1 = Y1 + point.y;
            X2 = X2 + point.x * point.x;
            Y2 = Y2 + point.y * point.y;
            X3 = X3 + point.x * point.x * point.x;
            Y3 = Y3 + point.y * point.y * point.y;
            X1Y1 = X1Y1 + point.x * point.y;
            X1Y2 = X1Y2 + point.x * point.y * point.y;
            X2Y1 = X2Y1 + point.x * point.x * point.y;
        }

        double C, D, E, G, H, N;
        double a, b, c;
        N = points.size();
        C = N * X2 - X1 * X1;
        D = N * X1Y1 - X1 * Y1;
        E = N * X3 + N * X1Y2 - (X2 + Y2) * X1;
        G = N * Y2 - Y1 * Y1;
        H = N * X2Y1 + N * Y3 - (X2 + Y2) * Y1;
        a = (H * D - E * G) / (C * G - D * D);
        b = (H * C - E * D) / (D * D - G * C);
        c = -(a * X1 + b * Y1 + X2 + Y2) / N;

        double A, B, R;
        A = a / (-2);
        B = b / (-2);
        R = Math.sqrt(a * a + b * b - 4 * c) / 2;

        return new Pair<Point, Integer>(new Point((int) A, (int) B), (int) R);
    }

    private boolean near(Point startPoint, Point endPoint) {
        double x2 = Math.pow(startPoint.x - endPoint.x, 2);
        double y2 = Math.pow(startPoint.y - endPoint.y, 2);
        return Math.sqrt(x2 + y2) <= POINT_WIDTH * 5;
    }
}