import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Name: Week 6 -> Based on Algs4PrincetonLectures
 *
 */
public class Graph {
    private final int V;
    private final int E;
    private Set<Integer>[] adj;

    public Graph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Set<Integer>[]) new Set[V];
        for(int v = 0; v < V; v++) {
            adj[v] = new HashSet<Integer>();
        }
    }

    public Graph(In in) {
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Graph must be nonnegative");
            adj = (Set<Integer>[]) new Set[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new HashSet<Integer>();
            }
            this.E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                validateVertex(v);
                validateVertex(w);
                addEdge(v, w);
            }
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < adj.length; i++) {
            str.append("Vertex " + i + " is connected to: ");
            for (int w : adj[i]) {
                str.append(w + ", ");
            }
            if (str.subSequence(str.length() - 2, str.length()).equals(", ")) {
                str.delete(str.length() - 2, str.length() - 1);
            } else {
                str.append("NO CONNECTIONS.");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        In in = new In("tinyG.txt");
        Graph G = new Graph(in);
        StdOut.println(G);

        DepthFirstPaths search = new DepthFirstPaths(G, 0);
        for (int v = 0; v < G.V(); v++) {
            if (search.marked(v))
                StdOut.print(v + " ");
        }

        StdOut.println();
        if (search.count() != G.V()) StdOut.println("NOT connected");
        else                         StdOut.println("connected");

        // Path to a vertex;
        StdOut.println();
        StdOut.println(search.printPathTo(6));
        StdOut.println(search.printPathTo(4));
        StdOut.println(search.printPathTo(2));
        StdOut.println(search.printPathTo(11));
        StdOut.println(search.printPathTo(8));
        StdOut.println();

        StdOut.println("BREADTH FIRST SEARCH:");
        BreadthFirstPaths breadth = new BreadthFirstPaths(G, 0);
        StdOut.println();
        StdOut.println(breadth.printPathTo(6));
        StdOut.println(breadth.printPathTo(4));
        StdOut.println(breadth.printPathTo(2));
        StdOut.println(breadth.printPathTo(11));
        StdOut.println(breadth.printPathTo(8));
        StdOut.println();

        StdOut.println("CC:");
        CC components = new CC(G);
        StdOut.println("Vertex " + 0 + " and " + 1 + " are connected?: " + components.connected(0,1));
        StdOut.println("Vertex " + 0 + " and " + 7 + " are connected?: " + components.connected(0,7));
        StdOut.println("Testing id method: ");
        for (int v = 0; v < G.V(); v++) {
            StdOut.println("ID number of vertex " + v + " is: " + components.id(v));
        }

    }
}
