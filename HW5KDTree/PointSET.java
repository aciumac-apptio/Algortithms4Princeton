/**
 * Created by eugenec on 7/5/2017.
 */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.*;

public class PointSET {
    private final Set<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D pt : points) {
            pt.draw();
        }
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Queue<Point2D> queue = new LinkedList<Point2D>();
        for (Point2D pt : points) {
            if (rect.contains(pt)) {
                queue.add(pt);
            }
        }
        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (points.isEmpty()) {
            return null;
        }

        Iterator<Point2D> itr = points.iterator();
        Point2D champion = itr.next();
        while (itr.hasNext()) {
            Point2D test = itr.next();
            if (test.distanceSquaredTo(p) < champion.distanceSquaredTo(p)){
                champion = test;
            }
        }
        return champion;
    }

}
