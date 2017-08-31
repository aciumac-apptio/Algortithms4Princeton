/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Name: Week 6 -> Based on Algs4PrincetonLectures
 *
 */

import java.util.Arrays;
import java.util.Stack;

public class DirectedDFS {
    private boolean[] marked;
    private int[] edgeTo;
    private int s;
    private int count;

    public DirectedDFS(Digraph G, int s){
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
       /// Arrays.fill(edgeTo, Integer.MAX_VALUE);
        this.s = s;
        dfs(G, s);
    }

    private void dfs(Digraph G, int v) {
        count++;
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                dfs(G, w);
                edgeTo[w] = v;
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

    public int count() {
        return count;
    }

}
