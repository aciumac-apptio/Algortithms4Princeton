/**
 * Created by eugenec on 7/5/2017.
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.Queue;

public class KdTree {
    private Node root;
    private int size;

    private class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, Node lb, Node rt, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
        }
    }

    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = insert(root, p, true, new RectHV(0, 0, 1, 1));
    }

    private Node insert(Node n, Point2D p, boolean useX, RectHV rect) {
        if (n == null) {
            n = new Node(p, null, null, rect);
            size++;
        }
        if (n.p.equals(p)) {
            return n;
        } else if (useX) {
            if (p.x() < n.p.x()) {
                rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
                n.lb = insert(n.lb, p, !useX, rect);
            } else {
                rect = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
                n.rt = insert(n.rt, p, !useX, rect);
            }
        } else {
            if (p.y() < n.p.y()) {
                rect = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
                n.lb = insert(n.lb, p, !useX, rect);
            } else {
                rect = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
                n.rt = insert(n.rt, p, !useX, rect);
            }

        }

        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(root, p, true);
    }

    private boolean contains(Node n, Point2D p, boolean useX) {
        if (n != null) {
            if (n.p.equals(p)) {
                return true;
            } else if (useX) {
                if (p.x() < n.p.x()) {
                    return contains(n.lb, p, !useX);
                } else {
                    return contains(n.rt, p, !useX);
                }
            } else {
                if (p.y() < n.p.y()) {
                    return contains(n.lb, p, !useX);
                } else {
                    // takes care of equal case
                    return contains(n.rt, p, !useX);
                }
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, true);
    }

    private void draw(Node n, boolean useX) {
        if (n == null) {
            return;
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();
        StdDraw.setPenRadius();
        if (useX) {
            //Draw red vertical line
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            //Draw blue horizontal line
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        draw(n.lb, !useX);
        draw(n.rt, !useX);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        Queue<Point2D> queue = new LinkedList<Point2D>();
        return range(rect, queue, root);
    }

    private Queue<Point2D> range(RectHV rect, Queue<Point2D> queue, Node n) {
        if (n == null) {
            return queue;
        }

        if (rect.contains(n.p)) {
            queue.add(n.p);
        }

        //not rect.contains(n.lb.p)
        if (n.lb != null && n.lb.rect.intersects(rect)) {
            queue = range(rect, queue, n.lb);
        }
        if (n.rt != null && n.rt.rect.intersects(rect)) {
            queue = range(rect, queue, n.rt);
        }

        return queue;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        if (isEmpty()) {
            return null;
        }

        return nearest(root, p, root.p);
    }

    private Point2D nearest(Node n, Point2D p, Point2D champion) {
        if (n == null) {
            return champion;
        }

        if (p.equals(n.p)) {
            return p;
        }

        if (n.p.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
            champion = n.p;
        }

        if (n.lb != null && n.lb.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
            champion = nearest(n.lb, p, champion);
        }

        if (n.rt != null && n.rt.rect.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
            champion = nearest(n.rt, p, champion);
        }

        return champion;
    }


    public static void main(String[] args) {
        /*KdTree kdtree = new KdTree();
        System.out.println(kdtree.size());
       *//* double[] x = {0.7, 0.5, 0.2, 0.4, 0.9, 0.9, 0.5};
        double[] y = {0.2, 0.4, 0.3, 0.7, 0.6, 0.6, 0.4};
        for (int i = 0; i < x.length; i++) {
            kdtree.insert(new Point2D(x[i], y[i]));
            kdtree.draw();
            System.out.println(kdtree.size());
        }
        System.out.println();*//*

        In in = new In("circle10.txt");
        while (in.hasNextLine()){
            double x1 = Double.parseDouble(in.readString());
            double y1 = Double.parseDouble(in.readString());
            kdtree.insert(new Point2D(x1, y1));
            kdtree.draw();
        }

        //Point2D pt = kdtree.nearest(new Point2D(0.1, 0.9));
        Point2D pt = kdtree.nearest(new Point2D(0.81, 0.30));
        System.out.println(pt.toString());



        System.out.println();*/
    }



}
