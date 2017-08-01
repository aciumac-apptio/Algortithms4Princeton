
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import javax.sound.sampled.Line;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points)   {
        //this.points = points;
        // sort points
        if (points == null) {
            throw new NullPointerException();
        }

        // Throws exception if duplicate arguments
        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException();
                }
            }
        }

        Point[] copyPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(copyPoints);
        this.segments = new ArrayList<>();

        for (int p = 0; p < copyPoints.length; p++){
            for (int q = p + 1; q < copyPoints.length; q++){
                double slopepq = copyPoints[p].slopeTo(copyPoints[q]);
                for (int r = q + 1; r < points.length; r++) {
                    double slopepr = copyPoints[p].slopeTo(copyPoints[r]);
                    if (slopepq == slopepr) {
                        for (int s = r + 1; s < copyPoints.length; s++){
                            double slopeps = copyPoints[p].slopeTo(copyPoints[s]);
                            if (slopepq == slopeps) {
                                segments.add(new LineSegment(copyPoints[p],copyPoints[s]));
                            }
                        }
                    }
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments()   {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments()  {
        LineSegment[] lines = new LineSegment[segments.size()];
        return this.segments.toArray(lines);
    }

    public static void main(String[] args) {

        // read the n points from a file
        /*In in = new In(args[0]);
        int n = in.readInt();*/

/*        In in = new In("input8.txt");
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();*/
    }
}