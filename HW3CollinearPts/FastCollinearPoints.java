import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 * Created by eugenec on 6/3/2017.
 */
public class FastCollinearPoints {
    // finds all line segments containing 4 or more points
    private ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        this.segments = new ArrayList<>();
        Point[] ps = points.clone();
        Arrays.sort(ps);

        // Throws exception if duplicate arguments
        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException();
                }
            }
        }

        List<List<Point>> setOfPts = new ArrayList<>();

        for (int p = 0; p < points.length; p++) {
            // Sort by slope with respect on points[p]
            Point[] copyPoints = ps.clone();
            Point sortedBy = copyPoints[p];
            Arrays.sort(copyPoints, copyPoints[p].slopeOrder());
            List<Point> collinearPts = new ArrayList<>();  // segments
            Point prev = copyPoints[0];

            for (int i = 1; i < copyPoints.length; i++) {
                // If slopes match
                if (sortedBy.slopeTo(prev) == sortedBy.slopeTo(copyPoints[i])) {
                    // If set is empty add previous pt
                    if (collinearPts.isEmpty()) {
                        collinearPts.add(prev);
                    }
                    // if the slope changes
                    // Check if  slope changes
                    Iterator<Point> itr = collinearPts.iterator();
                    Point one = itr.next();
                    // if slope changes
                     if (sortedBy.slopeTo(one) != sortedBy.slopeTo(copyPoints[i])) {
                         if (collinearPts.size() >= 3) {
                             //  if collinearPts.size() >= 3
                             //    Add elements of a set to a list
                             List<Point> list = new ArrayList<>();
                             list.addAll(collinearPts);
                             list.add(sortedBy);
                             Collections.sort(list);

                             List<Point> arr = new ArrayList<>();
                             arr.add(list.get(0));
                             arr.add(list.get(list.size()-1));
                             if (!setOfPts.contains(arr)) {
                                 setOfPts.add(arr);
                                 segments.add(new LineSegment(list.get(0), list.get(list.size()-1)));
                             }
                         }
                         //    Empty collinear points
                         collinearPts = new ArrayList<>();
                         //    add prev and current to the collinear pts
                         collinearPts.add(prev);
                     }
                    collinearPts.add(copyPoints[i]);

                }

                prev = copyPoints[i];
            }

            // Add any "forgotten" items
            if(!collinearPts.isEmpty() && collinearPts.size() >= 3) {
                List<Point> list = new ArrayList<>();
                list.addAll(collinearPts);
                list.add(sortedBy);
                //  Add elements to the main setOfLists -> sort first
                Collections.sort(list);
                List<Point> arr = new ArrayList<>();
                arr.add(list.get(0));
                arr.add(list.get(list.size()-1));
                if (!setOfPts.contains(arr)) {
                    setOfPts.add(arr);
                    segments.add(new LineSegment(list.get(0), list.get(list.size()-1)));
                }
            }

        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] lines = new LineSegment[segments.size()];
        return this.segments.toArray(lines);
    }

    public static void main(String[] args) {
        // read the n points from a file
        /*In in = new In(args[0]);
        int n = in.readInt();*/

        In in = new In("rs1423.txt");
        int n = in.readInt();

        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
