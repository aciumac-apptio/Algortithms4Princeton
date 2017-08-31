/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Name: Week 6 -> Based on Algs4PrincetonLectures
 *
 */

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BreadthFirstPaths {
    private boolean[] marked;
    private int[] edgeTo;
    private int s;

    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        bfs(G, s);
    }

    private void bfs(Graph G, int s) {
        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        marked[s] = true;
        while (!q.isEmpty()) {
            int v = q.remove();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    q.add(w);
                    marked[w] = true;
                    edgeTo[w] = v;
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked(v);
    }

    public Iterable<Integer> pathTo (int v) {
        if(!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    public String printPathTo(int v) {
        StringBuilder str = new StringBuilder();
        if (hasPathTo(v)) {
            Iterable<Integer> path = pathTo(v);
            for (Integer i : path) {
                str.insert(0,i + " - ");
            }
            str.delete(str.length() - 3, str.length());
        } else {
            str.append("NO PATH FOUND TO VERTEX " + v  + ".");
        }
        str.insert(0,"Path from " + s + " to " + v + ": ");
        return str.toString();
    }

    /**
     * Is there a path between the source vertex {@code s} and vertex {@code v}?
     * @param v the vertex
     * @return {@code true} if there is a path, {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean marked(int v) {
        validateVertex(v);
        return marked[v];
    }

    private void validateVertex(int v) {
        int V = marked.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

}
