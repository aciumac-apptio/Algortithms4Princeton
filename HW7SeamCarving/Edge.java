


public class Edge implements Comparable<Edge> {
    private final int v, w;
    private final double weight;

    // Create a weighted edge v-w
    public Edge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    // Either endpoint
    public int either() {
        return v;
    }

    // The endpoint that's not v
    public int other(int vertex) {
        if (vertex == v) return this.w;
        else return this.v;
    }

    // Compare edges by weight
    public int compareTo(Edge that) {
        if (this.weight < that.weight) {
            return -1;
        } else if (this.weight > that.weight) {
            return +1;
        } else {
            return 0;
        }
    }

}
