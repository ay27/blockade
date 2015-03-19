package bitman.ay27.blockade.activity;

import android.graphics.Point;
import android.util.Log;
import bitman.ay27.blockade.widget.DrawView;

import java.util.ArrayList;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/10.
 */

/**
 * input two groups of line, then juxtapose whether the two groups of line
 * is similar.
 */
public class CurveProcessor {

    /**
     * every segment's length
     */
    public static final double SEGMENT_LENGTH = 120.0;
    public static final double START_POINT_DIS_THRESHOLD = 100.0;
    public static final double FIT_DOT_THRESHOLD = 16.0;
    public static final double FIT_DOT_TIME_THRESHOLD = 50.0;
    public static final double ANGLE_CHAIN_THRESHOLD1 = Math.tan(Math.PI / 6.0);
    public static final double ANGLE_CHAIN_THRESHOLD2 = Math.tan(Math.PI / 3.0);
    public static final double CHAIN_MATCHING_TOLERANCE = 10.0;
    public static final double PRECISION_THRESHOLD = 0.00001;
    public static final double RHYTHM_ENDPOINT_TIME_THRESHOLD = 1000;
    public static final double RHYTHM_SEGMENT_THRESHOLD = 1000;
    private static final String TAG = "CurveProcessor";

    private ArrayList<ArrayList<DrawView.LinePoint>> lines1, lines2;
    private ArrayList<ArrayList<Segment>> segments1, segments2;
    private ArrayList<ArrayList<Double>> chainCode1, chainCode2;
    private ArrayList<Integer> num_of_segments;


    public CurveProcessor(ArrayList<ArrayList<DrawView.LinePoint>> lines1, ArrayList<ArrayList<DrawView.LinePoint>> lines2) {
        this.lines1 = lines1;
        this.lines2 = lines2;
    }

    private static double get_distance(Point a, Point b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    /**
     * bind two LinePoint to one LinePoint, make it average.
     */
    private static DrawView.LinePoint bindDot(DrawView.LinePoint p1, DrawView.LinePoint p2) {
//        Point tmp = new Point((p1.point.x + p2.point.x) / 2, (p1.point.y + p2.point.y) / 2);
        Point tmp = new Point(p1.point);
        return new DrawView.LinePoint(tmp, (p1.time + p2.time) / 2);
    }

    public ArrayList<ArrayList<DrawView.LinePoint>> getSegments1() {
        ArrayList<ArrayList<DrawView.LinePoint>> result = new ArrayList<ArrayList<DrawView.LinePoint>>();
        for (ArrayList<Segment> segments : segments1) {
            ArrayList<DrawView.LinePoint> oneLine = new ArrayList<DrawView.LinePoint>();
            for (Segment segment : segments) {
                oneLine.add(segment.firstPoint());
                oneLine.add(segment.points.get(segment.points.size() - 1));
            }
            result.add(oneLine);
        }

        return result;
    }

    public ArrayList<ArrayList<DrawView.LinePoint>> getSegments2() {
        ArrayList<ArrayList<DrawView.LinePoint>> result = new ArrayList<ArrayList<DrawView.LinePoint>>();
        for (ArrayList<Segment> segments : segments2) {
            ArrayList<DrawView.LinePoint> oneLine = new ArrayList<DrawView.LinePoint>();
            for (Segment segment : segments) {
                oneLine.add(segment.firstPoint());
                oneLine.add(segment.points.get(segment.points.size() - 1));
            }
            result.add(oneLine);
        }

        return result;
    }

    public boolean juxtapose() {
        lines1 = deNoise(lines1);
        lines2 = deNoise(lines2);
        segments1 = split_curve(lines1);
        segments2 = split_curve(lines2);
        chainCode1 = calc_included_angle_chain(segments1);
        chainCode2 = calc_included_angle_chain(segments2);

        boolean result = compare_start_point(lines1, lines2);
        Log.i(TAG, "start point compare result :" + result);
        result = result && compare_included_angle_chain(chainCode1, chainCode2);
        Log.i(TAG, "compare included angle chain result :" + result);
        result = result && compare_rhythm(segments1, segments2);
        Log.i(TAG, "compare rhythm :" + result);
        return result;
    }

    private ArrayList<ArrayList<DrawView.LinePoint>> deNoise(ArrayList<ArrayList<DrawView.LinePoint>> lines) {
        ArrayList<ArrayList<DrawView.LinePoint>> result = new ArrayList<ArrayList<DrawView.LinePoint>>();
        for (ArrayList<DrawView.LinePoint> oneLine : lines) {
            ArrayList<DrawView.LinePoint> line = new ArrayList<DrawView.LinePoint>();
            DrawView.LinePoint lastOne;
            line.add(lastOne = oneLine.get(0));
            for (int i = 1; i < oneLine.size(); i++) {
                if (get_distance(lastOne.point, oneLine.get(i).point) - FIT_DOT_THRESHOLD <= PRECISION_THRESHOLD
                        && Math.abs(lastOne.time - oneLine.get(i).time) - FIT_DOT_TIME_THRESHOLD <= PRECISION_THRESHOLD) {
                    DrawView.LinePoint tmp = bindDot(lastOne, oneLine.get(i));
                    line.remove(lastOne);
                    line.add(lastOne = tmp);
                } else {
                    line.add(lastOne = oneLine.get(i));
                }
            }
            result.add(line);
        }
        return result;
    }

    private boolean compare_start_point(ArrayList<ArrayList<DrawView.LinePoint>> lines1, ArrayList<ArrayList<DrawView.LinePoint>> lines2) {
        if (lines1.size() != lines2.size()) {
            return false;
        }

        for (int i = 0; i < lines1.size(); i++) {
            double dis = get_distance(lines1.get(i).get(0).point, lines2.get(i).get(0).point);
            if (dis - START_POINT_DIS_THRESHOLD >= PRECISION_THRESHOLD) {
                return false;
            }
        }

        return true;
    }

    private ArrayList<ArrayList<Segment>> split_curve(ArrayList<ArrayList<DrawView.LinePoint>> lines) {
        ArrayList<ArrayList<Segment>> result = new ArrayList<ArrayList<Segment>>();

        if (num_of_segments == null) {
            num_of_segments = new ArrayList<Integer>();
            for (ArrayList<DrawView.LinePoint> oneLine : lines) {
                ArrayList<Segment> tmp = split_one_curve(oneLine, SEGMENT_LENGTH);
                result.add(tmp);
                num_of_segments.add(tmp.size());
            }
        } else {
            for (int i = 0; i < lines.size(); i++) {
                double left = 1.0, right = 2 * SEGMENT_LENGTH, mid = SEGMENT_LENGTH;
                ArrayList<DrawView.LinePoint> oneLine = lines.get(i);
                final int num_of_segment = num_of_segments.get(i);
                ArrayList<Segment> tmp = null;
                while ((left <= right) && (tmp = split_one_curve(oneLine, mid)).size() != num_of_segment) {
                    if (tmp.size() > num_of_segment) {
                        left = mid;
                        mid = (right + mid) / 2 + 1;
                    } else {
                        right = mid;
                        mid = (left + mid) / 2 - 1;
                    }
                }
                result.add(tmp);
            }
        }

        return result;
    }

    private ArrayList<Segment> split_one_curve(ArrayList<DrawView.LinePoint> oneLine, final double segment_length) {
        ArrayList<Segment> result = new ArrayList<Segment>();
        Segment currentSegment = null;
        for (DrawView.LinePoint point : oneLine) {
            if (currentSegment == null) {
                currentSegment = new Segment();
            }
            if (currentSegment.contain(point, segment_length)) {
                currentSegment.points.add(point);
            } else {
                currentSegment.fitting();
                result.add(currentSegment);

                currentSegment = new Segment();
                currentSegment.points.add(point);
            }
        }
        if (currentSegment.points.size() >= 2) {
            currentSegment.fitting();
            result.add(currentSegment);
        }

        return result;
    }

    private ArrayList<ArrayList<Double>> calc_included_angle_chain(ArrayList<ArrayList<Segment>> lines) {
        ArrayList<ArrayList<Double>> result = new ArrayList<ArrayList<Double>>();

        for (ArrayList<Segment> oneLine : lines) {
            result.add(calc_one_line_included_angle_chain(oneLine));
        }

        return result;
    }

    private ArrayList<Double> calc_one_line_included_angle_chain(ArrayList<Segment> oneLine) {
        ArrayList<Double> result = new ArrayList<Double>();

        if (oneLine.size() < 2)
            return result;

        for (int i = 1; i < oneLine.size(); i++) {
            result.add(oneLine.get(i).a - oneLine.get(i - 1).a);
        }

        return result;
    }

    private boolean compare_included_angle_chain(ArrayList<ArrayList<Double>> chainCode1, ArrayList<ArrayList<Double>> chainCode2) {
        if (chainCode1.size() != chainCode2.size()) {
            return false;
        }

        for (int i = 0; i < chainCode1.size(); i++) {
            if (!compare_one_line_included_angle_chain(chainCode1.get(i), chainCode2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean compare_one_line_included_angle_chain(ArrayList<Double> chain1, ArrayList<Double> chain2) {
        double deviationTolerance = 0.0;

        if (chain1.size() != chain2.size())
            return false;

        for (int i = 0; i < chain1.size(); i++) {
            double tmp = Math.abs(chain1.get(i) - chain2.get(i));
            if (tmp - ANGLE_CHAIN_THRESHOLD1 <= PRECISION_THRESHOLD) {
                deviationTolerance = Math.max(0.0, deviationTolerance - 1.0);
            } else if (tmp - ANGLE_CHAIN_THRESHOLD2 <= PRECISION_THRESHOLD) {
                deviationTolerance += 1.0;
            } else {
                deviationTolerance += 2.0;
            }

            if (deviationTolerance - CHAIN_MATCHING_TOLERANCE >= PRECISION_THRESHOLD) {
                return false;
            }
        }
        return true;
    }

    private boolean compare_rhythm(ArrayList<ArrayList<Segment>> segments1, ArrayList<ArrayList<Segment>> segments2) {
        if (segments1.size() != segments2.size()) {
            return false;
        }

        for (int i = 0; i < segments1.size(); i++) {
            if (!compare_one_line_rhythm(segments1.get(i), segments2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean compare_one_line_rhythm(ArrayList<Segment> segment1, ArrayList<Segment> segment2) {

        if (segment1.size() != segment2.size()) {
            return false;
        }

        for (int i = 0; i < segment1.size(); i++) {
            double l1 = segment1.get(i).getLeftTime(), l2 = segment2.get(i).getLeftTime();
            double r1 = segment1.get(i).getRightTime(), r2 = segment2.get(i).getRightTime();

            if (l2 - l1 - RHYTHM_ENDPOINT_TIME_THRESHOLD >= PRECISION_THRESHOLD
                    ||
                    r2 - r1 - RHYTHM_ENDPOINT_TIME_THRESHOLD >= PRECISION_THRESHOLD
                    ||
                    Math.abs((r1 - l1) - (r2 - l2)) - RHYTHM_SEGMENT_THRESHOLD >= PRECISION_THRESHOLD) {
                return false;
            }
        }

        return true;
    }

    private static final class Segment {
        ArrayList<DrawView.LinePoint> points;

        /// y = ax+b, slope = a
        private double a = 0.0, b = 0.0;
        private double leftTime = 0.0, rightTime = 0.0;

        private boolean hasDone = false;

        public Segment() {
            points = new ArrayList<DrawView.LinePoint>();
        }

        DrawView.LinePoint firstPoint() {
            if (points != null && !points.isEmpty())
                return points.get(0);
            return null;
        }

        /**
         * check if the point is in this segment, use the SEGMENT_LENGTH to judge.
         */
        boolean contain(DrawView.LinePoint point) {
            if (points.size() < 2)
                return true;

            for (DrawView.LinePoint p : points) {
                double dis = get_distance(p.point, point.point);
                if (dis - SEGMENT_LENGTH >= PRECISION_THRESHOLD) {
                    return false;
                }
            }

            return true;

//            double dis = get_distance(point.point, firstPoint().point);
//            return dis - SEGMENT_LENGTH <= PRECISION_THRESHOLD;
        }

        public boolean contain(DrawView.LinePoint point, double segment_length) {
            if (points.size() < 2)
                return true;

            for (DrawView.LinePoint p : points) {
                double dis = get_distance(p.point, point.point);
                if (dis - segment_length >= PRECISION_THRESHOLD) {
                    return false;
                }
            }

            return true;
        }

        /**
         * use Least squares to fit a line.
         * y = ax+b
         * a = sum((Xi-avgX)*(Yi-avgY)) / sum((Xi-avgX)^2)
         * b = avgY - a*avgX
         */
        public void fitting() {

            double average_x = 0.0, average_y = 0.0;
            for (DrawView.LinePoint point : points) {
                average_x += point.point.x;
                average_y += point.point.y;
            }
            average_x = average_x / (double) points.size();
            average_y = average_y / (double) points.size();

            double molecular = 0.0, denominator = 0.0;
            for (DrawView.LinePoint point : points) {
                molecular += (point.point.x - average_x) * (point.point.y - average_y);
                denominator += (point.point.x - average_x) * (point.point.x - average_x);
            }
            this.a = molecular / denominator;
            this.b = average_y - this.a * average_x;

            for (int i = 0; i < points.size() / 2; i++) {
                this.leftTime += points.get(i).time;
            }
            this.leftTime = leftTime / ((double) points.size() / 2.0);
            for (int i = points.size() / 2; i < points.size(); i++) {
                this.rightTime += points.get(i).time;
            }
            this.rightTime = rightTime / ((double) points.size() / 2.0);

            hasDone = true;
        }

        public double getA() {
            if (!hasDone) {
                fitting();
            }
            return a;
        }

        public double getB() {
            if (!hasDone) {
                fitting();
            }
            return b;
        }

        public double getLeftTime() {
            if (!hasDone) {
                fitting();
            }
            return leftTime;
        }

        public double getRightTime() {
            if (!hasDone) {
                fitting();
            }
            return rightTime;
        }

    }
}
