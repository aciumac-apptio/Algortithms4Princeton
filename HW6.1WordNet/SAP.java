/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Modified: 10/23/17
 * Name: Week 6 -> WordNet
 */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.Arrays;
import java.util.HashSet;

public class SAP {
    private final Digraph G;
    //private Stack<Integer> path;   // not required, but for personal enjoyment (want to print the path taken)

    // constructor takes a rooted DAG as argument
    public SAP(Digraph G) {
        this.G = G;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v >= G.V() || v < 0 || w >= G.V() || w < 0) {
            throw new IndexOutOfBoundsException();
        }

        return distOrAnc(new HashSet<>(Arrays.asList(v)), new HashSet<>(Arrays.asList(w)), false);
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v >= G.V() || v < 0 || w >= G.V() || w < 0) {
            throw new IndexOutOfBoundsException();
        }
        return distOrAnc(new HashSet<>(Arrays.asList(v)), new HashSet<>(Arrays.asList(w)), true);
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        return distOrAnc(subsetA, subsetB, false);
    }

    // Helper method that returns common ancestor or the distance between the sets of vertices
    private int distOrAnc(Iterable<Integer> subsetA, Iterable<Integer> subsetB, boolean ancest) {
        if (subsetA == null || subsetB == null) {
            throw new NullPointerException();
        }

        validateVertices(subsetA);
        validateVertices(subsetB);

        BreadthFirstDirectedPaths path_v = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths path_w = new BreadthFirstDirectedPaths(G, subsetB);

        int distTo = -1;
        int ancestor = -1;
        for (int i = 0; i < G.V(); i++) {
            // If both sets of vertices have path to the given vertex and the distance is smaller than previous min
            if (path_v.hasPathTo(i) && path_w.hasPathTo(i) && (distTo == -1 || distTo > path_v.distTo(i) + path_w.distTo(i))) {
                distTo = path_v.distTo(i) + path_w.distTo(i);
                ancestor = i;
            }
        }

        // Return common ancestor or the distance between the vertices
        if (ancest) {
            return ancestor;
        } else {
            return distTo;
        }
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        return distOrAnc(subsetA, subsetB, true);
    }

    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = G.V();
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
/*        In in = new In("wordnet/digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sca = new SAP(G);
        System.out.println("Distance between 2 and 6: " + sca.length(2, 6));*/

/*        In in = new In("wordnet/digraph1.txt");
        Digraph G = new Digraph(in);
        SAP sca = new SAP(G);
        System.out.println(sca.hasMultipleRoots(G));*/
        /*SAP sca = new SAP(G);
        System.out.println("Testing single vertex shortest common ancestor:");
        System.out.println("Ancestor 1 + 4: " + sca.ancestor(1, 4));
        System.out.println("Distance between 1 and 4: " + sca.length(1, 4));
        System.out.println();*/
/*        System.out.println("Ancestor 13 + 22: " + sca.ancestor(13, 22));
        System.out.println("Distance between 13 and 22: " + sca.length(13, 22));
        System.out.println();*/
        /*System.out.println("Ancestor 23 + 22: " + sca.ancestor(23, 22));
        System.out.println("Distance between 23 and 22: " + sca.length(23, 22));
        System.out.println();

        System.out.println("Testing multiple vertex shortest common ancestor:");
        System.out.println("Ancestor {7,21} and {4}: " + sca.ancestor(new ArrayList<>(Arrays.asList(7, 21)), new ArrayList<>(Arrays.asList(4))));
        System.out.println("Distance between 7,21 and 4: " + sca.length(new ArrayList<>(Arrays.asList(7, 21)), new ArrayList<>(Arrays.asList(4))));
        System.out.println();
        System.out.println("Ancestor {13,23,24} and {6,16,17}: " + sca.ancestor(new ArrayList<>(Arrays.asList(13, 23, 24)), new ArrayList<>(Arrays.asList(6, 16, 17))));
        System.out.println("Distance between 13,23,24 and 6,16,17: " + sca.length(new ArrayList<>(Arrays.asList(13, 23, 24)), new ArrayList<>(Arrays.asList(6, 16, 17))));
        System.out.println();

        System.out.println("Testing single common ancestor on graph with multiple roots:");
        In in_1 = new In("double_root.txt");
        Digraph G_1 = new Digraph(in_1);
        try {
            SAP sca_1 = new SAP(G_1);
            System.out.println("Ancestor 2 + 3: " + sca_1.ancestor(2, 3));
            System.out.println("Distance between 2 and 3: " + sca_1.length(2, 3));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Confirm: Multiple Roots Present. Invalid Graph;");
        }
        System.out.println();
*/
    }
}
