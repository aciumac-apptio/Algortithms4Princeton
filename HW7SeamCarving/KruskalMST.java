import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.UF;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class KruskalMST {
    public Queue<Edge> mst = new LinkedList<>();

    public KruskalMST(EdgeWeightedGraph G) {
        // Build priority queue
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        for (Edge e : G.edges()){
            pq.add(e);
        }
        UF uf = new UF(G.V());
        // Loop until we run out of edges or size of span.tree reached G.V() - 1
        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            // Greedely add edges to mst
            Edge e = pq.remove();
            int v = e.either();
            int w = e.other(v);
            if (!uf.connected(v, w)) {
                // Edge v-w does not create a cycle
                uf.union(v, w);
                // Add edge to mst
                mst.add(e);
            }
        }
    }

    public Queue<Edge> edges() {
        return mst;
    }
}
