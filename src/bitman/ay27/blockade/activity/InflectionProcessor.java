package bitman.ay27.blockade.activity;

import android.graphics.Point;
import bitman.ay27.blockade.widget.DrawView;

import java.util.ArrayList;

/**
 * Proudly to user Intellij IDEA.
 * Created by ay27 on 15/2/11.
 */
public class InflectionProcessor {

    public static final double FIT_DOT_THRESHOLD = 16.0;
    public static final double FIT_DOT_TIME_THRESHOLD = 50.0;
    public static final double PRECISION_THRESHOLD = 0.00001;
    public static final double RHYTHM_INFLECTION_POINT_TIME_THRESHOLD = 1000;
    private ArrayList<ArrayList<DrawView.LinePoint>> lines1, lines2;
    private Inflection inflection1, inflection2;


    public InflectionProcessor(ArrayList<ArrayList<DrawView.LinePoint>> lines1, ArrayList<ArrayList<DrawView.LinePoint>> lines2) {
        this.lines1 = deNoise(lines1);
        this.lines2 = deNoise(lines2);
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

    public boolean juxtapose() {
        if (lines1.size() != lines2.size()) {
            return false;
        }

        for (int i = 0; i < lines1.size(); i++) {
            inflection1 = new Inflection(lines1.get(i));
            inflection2 = new Inflection(lines2.get(i));
            if (!compare(inflection1, inflection2)) {
                return false;
            }
        }
        return true;
    }

    public Inflection getInflection1() {
        return inflection1;
    }

    public Inflection getInflection2() {
        return inflection2;
    }

    public ArrayList<ArrayList<DrawView.LinePoint>> getLines1() {
        return lines1;
    }

    public ArrayList<ArrayList<DrawView.LinePoint>> getLines2() {
        return lines2;
    }

    private boolean compare(Inflection inflection1, Inflection inflection2) {
        if (inflection1.inflectionPoint.size() != inflection2.inflectionPoint.size()) {
            return false;
        }

        for (int i = 0; i < inflection1.inflectionPoint.size(); i++) {
            Inflection.InflectionPoint p1 = inflection1.inflectionPoint.get(i);
            Inflection.InflectionPoint p2 = inflection2.inflectionPoint.get(i);

            if (p1.s1 * p2.s1 <= PRECISION_THRESHOLD
                    ||
                    p1.s2 * p2.s2 <= PRECISION_THRESHOLD
                    ||
                    !near(p1.point, p2.point)) {
                return false;
            }
        }

        return true;
    }

    private boolean near(DrawView.LinePoint p1, DrawView.LinePoint p2) {
        return Math.abs(p1.time - p2.time) < RHYTHM_INFLECTION_POINT_TIME_THRESHOLD && get_distance(p1.point, p2.point) < FIT_DOT_THRESHOLD;
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

    public static class Inflection {
        private static final double DEVIATION = 50.0;
        private ArrayList<DrawView.LinePoint> line;
        ArrayList<InflectionPoint> inflectionPoint;

        public Inflection(ArrayList<DrawView.LinePoint> line) {
            this.line = line;
            inflectionPoint = new ArrayList<InflectionPoint>();
            work();
        }

        public ArrayList<DrawView.LinePoint> getInflectionPoint() {
            ArrayList<DrawView.LinePoint> result =new ArrayList<DrawView.LinePoint>();
            for (InflectionPoint point : inflectionPoint) {
                result.add(point.point);
            }

            return result;
        }

        /**
         * calculate the inflection point of the line
         */
        private void work() {
            if (line.size() < 4)
                return;

            double s1;
            double s2;

//            inflectionPoint.add(new InflectionPoint(line.get(0), 0, 0));
//
//            for (DrawView.LinePoint point : line) {
//                InflectionPoint last = inflectionPoint.get(inflectionPoint.size()-1);
//                if (point.time - last.point.time > DEVIATION) {
//                    inflectionPoint.add(new InflectionPoint(point, 0, 0));
//                }
//            }

            for (int i = 0; i < line.size() - 3; i++) {
                s1 = calcS(line.get(i).point, line.get(i + 1).point, line.get(i + 2).point);
                s2 = calcS(line.get(i + 1).point, line.get(i + 2).point, line.get(i + 3).point);
//                if (Math.abs(Math.abs(s1)-Math.abs(s2)) - DEVIATION >= PRECISION_THRESHOLD) {
                if (s1 * s2 < -DEVIATION) {
                    inflectionPoint.add(new InflectionPoint(line.get(i + 2), s1, s2));
                }
            }
        }

        // S = (y-y1)*(x2-x1)+(y1-y2)*(x-x1)
        private double calcS(Point p1, Point p2, Point p3) {
            return (p3.y - p1.y) * (p2.x - p1.x) + (p1.y - p2.y) * (p3.x - p1.x);
        }

        public static class InflectionPoint {
            DrawView.LinePoint point;
            double s1, s2;

            public InflectionPoint(DrawView.LinePoint point, double s1, double s2) {
                this.point = point;
                this.s1 = s1;
                this.s2 = s2;
            }
        }

    }
}
