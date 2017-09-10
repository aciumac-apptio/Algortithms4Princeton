import java.util.HashSet;
import java.util.Set;

public class EdgeWeightedGraph {
    private final int V;
    private final Set<Edge>[] adj;       //same as with Graph, but adjacency lists of Edges instead of integers

    public EdgeWeightedGraph(int V) {
        this.V = V;
        adj = (Set<Edge>[]) new Set[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new HashSet<Edge>();
        }

    }

    // Add edge to both adjacency lists
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        adj[v].add(e);
        adj[w].add(e);
    }

    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

    public Iterable<Edge> edges() {
        Set<Edge> list = new HashSet<>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) {
                    list.add(e);
                }
                // only add one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }

}
