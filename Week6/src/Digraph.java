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
public class Digraph {
    private final int V;
    private final int E;
    private Set<Integer>[] adj;

    public Digraph(int V) {
        this.V = V;
        this.E = 0;
        adj = (Set<Integer>[]) new Set[V];
        for(int v = 0; v < V; v++) {
            adj[v] = new HashSet<>();
        }
    }

    public Digraph(In in) {
        try {
            this.V = in.readInt();
            if (V < 0) throw new IllegalArgumentException("number of vertices in a Digraph must be nonnegative");
            adj = (Set<Integer>[]) new Set[V];
            for(int v = 0; v < V; v++) {
                adj[v] = new HashSet<>();
            }
            this.E = in.readInt();
            if (E < 0) throw new IllegalArgumentException("number of edges in a Digraph must be nonnegative");
            for (int i = 0; i < E; i++) {
                int v = in.readInt();
                int w = in.readInt();
                validateVertex(v);
                validateVertex(w);
                addEdge(v, w);
            }

        } catch (NoSuchElementException e){
            throw new IllegalArgumentException("invalid input format in Graph constructor", e);
        }

    }
    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public void addEdge(int v, int w) {
        // Add only one edge
        adj[v].add(w);
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

/*    public Digraph reverse() {

    }*/

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
                str.append("NO OUTBOUND CONNECTIONS.");
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static void main(String[] args) {
        In in = new In("tinyDG.txt");
        Digraph G = new Digraph(in);
/*
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                StdOut.println(v + " is connected to " + w);
            }
        }*/
        StdOut.println(G);
        DirectedDFS search = new DirectedDFS(G, 0);
        System.out.println(search.printPathTo(2));
        System.out.println(search.printPathTo(4));
        System.out.println(search.printPathTo(11));

        StdOut.println();
        StdOut.println("Topological Sort: DFS");
        In in1 = new In("tinyDAG.txt");
        Digraph G1 = new Digraph(in1);
        StdOut.println(G1);
        DepthFirstOrder topo = new DepthFirstOrder(G1);
        StdOut.println(topo.reversePost());
    }
}
