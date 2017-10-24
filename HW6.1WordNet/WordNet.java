/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Modified: 10/23/17
 * Name: WordNet
 */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    // need to be able to support the following lookup
    // "punk" -> 18,21
    // 0 -> "juvenile juvenile_person"
    private Map<Integer, Set<String>> idToNouns;
    private Map<String, Set<Integer>> nounsToId;
    private Digraph G;
    private SAP shrt;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        //Build symbol tables
        In in = new In(synsets);
        idToNouns = new HashMap<>();
        nounsToId = new HashMap<>();
        while (in.hasNextLine()) {
            String[] tokens = in.readLine().split(",");
            int id = Integer.parseInt(tokens[0]);
            // Mapping nouns to id
            String[] nouns = tokens[1].split("\\s+");
            Set<String> set = new HashSet<>();
            Collections.addAll(set, nouns);
            idToNouns.put(id, set);

            //Mapping ids to nouns
            for (int i = 0; i < nouns.length; i++) {
                Set<Integer> s;
                if (nounsToId.containsKey(nouns[i])) {
                    s = nounsToId.get(nouns[i]);
                    s.add(id);
                } else {
                    s = new HashSet<>();
                    s.add(id);
                }
                nounsToId.put(nouns[i], s);
            }

        }
        in.close();

        // Creating Digraph
        this.G = new Digraph(idToNouns.keySet().size());
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] tokens = in.readLine().split(",");
            for (int i = 1; i < tokens.length; i++) {
                G.addEdge(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[i]));
            }
        }
        in.close();

        // Creating a SAP object (also checking for violation of DAG/multiple roots rule)
        DirectedCycle finder = new DirectedCycle(G);
        if (finder.hasCycle() || hasMultipleRoots(G)) {
            throw new IllegalArgumentException("Graph is not DAG or has multiple roots");
        }

        this.shrt = new SAP(G);
    }

    // all WordNet nouns
    public Iterable<String> nouns() {
        return nounsToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounsToId.containsKey(word);
    }

    // Checks if valid nouns were passed as arguments
    private void nounCheck(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sap(String noun1, String noun2) {
        nounCheck(noun1, noun2);

        Set<Integer> a = nounsToId.get(noun1);
        Set<Integer> b = nounsToId.get(noun2);
        int anc = shrt.ancestor(a, b);
        Set<String> ancestor = idToNouns.get(anc);
        StringBuilder str = new StringBuilder();
        for (String s : ancestor) {
            str.append(s + " ");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        nounCheck(noun1, noun2);

        Set<Integer> a = nounsToId.get(noun1);
        Set<Integer> b = nounsToId.get(noun2);
        return shrt.length(a, b);
    }

    private boolean hasMultipleRoots(Digraph G) {
        int count = 0;
        boolean[] arr = new boolean[G.V()];
        // Checks if the vertex has incoming edge
        for (int v = 0; v < G.V(); v++) {
            Iterator<Integer> iter = G.adj(v).iterator();
            while (iter.hasNext()) {
                arr[iter.next()] = true;
            }
        }

        for (int v = 0; v < G.V(); v++) {
            Iterator<Integer> iter = G.adj(v).iterator();
            // If vertex has only incoming edges -> root
            if (!iter.hasNext() && arr[v]) {
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

    }
}