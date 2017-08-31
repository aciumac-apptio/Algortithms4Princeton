/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Name: Week 6 -> WordNet
 *
 */

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ShortestCommonAncestor {
    private Digraph G;
    private int dist;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle() || hasMultipleRoots(G)) {
            throw new IllegalArgumentException("Graph is not DAG or has multiple roots");
        }

        this.G = G;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        return length(new HashSet<>(Arrays.asList(v)), new HashSet<>(Arrays.asList(w)));
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        return ancestor(new HashSet<>(Arrays.asList(v)), new HashSet<>(Arrays.asList(w)));
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        int ancestor = ancestor(subsetA, subsetB);
        return dist;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        BreadthFirstDirectedPaths path_v = new BreadthFirstDirectedPaths(G, subsetA);
        BreadthFirstDirectedPaths path_w = new BreadthFirstDirectedPaths(G, subsetB);
        int distTo = Integer.MAX_VALUE;
        int ancestor = Integer.MAX_VALUE;
        for (int i = 0; i < G.V(); i++) {
            // If both sets of vertices have path to the given vertex and the distance is smaller than previous min
            if (path_v.hasPathTo(i) && path_w.hasPathTo(i) && distTo > path_v.distTo(i) + path_w.distTo(i)) {
                distTo = path_v.distTo(i) + path_w.distTo(i);
                ancestor = i;
            }
        }

        dist = distTo;
        return ancestor;
    }

    private boolean hasMultipleRoots(Digraph G) {
        int count = 0;
        for (int v = 0; v < G.V(); v++) {
            Bag<Integer> bag = (Bag<Integer>) G.adj(v);
            if (bag.isEmpty()) {
                count++;
            }
            if (count > 1) {
                return true;
            }
        }
        return false;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("wordnet/digraph25.txt");
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        System.out.println("Testing single vertex shortest common ancestor:");
        System.out.println("Ancestor 1 + 4: " + sca.ancestor(1, 4));
        System.out.println("Distance between 1 and 4: " + sca.length(1, 4));
        System.out.println();
        System.out.println("Ancestor 13 + 22: " + sca.ancestor(13, 22));
        System.out.println("Distance between 13 and 22: " + sca.length(13, 22));
        System.out.println();
        System.out.println("Ancestor 23 + 22: " + sca.ancestor(23, 22));
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
            ShortestCommonAncestor sca_1 = new ShortestCommonAncestor(G_1);
            System.out.println("Ancestor 2 + 3: " + sca_1.ancestor(2, 3));
            System.out.println("Distance between 2 and 3: " + sca_1.length(2, 3));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Confirm: Multiple Roots Present. Invalid Graph;");
        }
        System.out.println();

        System.out.println("Testing graph with a cycle:");
        In in_2 = new In("cycle.txt");
        Digraph G_2 = new Digraph(in_2);
        try {
            ShortestCommonAncestor sca_2 = new ShortestCommonAncestor(G_2);
            System.out.println("Ancestor 2 + 3: " + sca_2.ancestor(2, 3));
            System.out.println("Distance between 2 and 3: " + sca_2.length(2, 3));
            System.out.println();
        } catch (IllegalArgumentException e) {
            System.out.println("Confirm: A cycle is present;");
        }

    }
}
