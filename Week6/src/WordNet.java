/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Name: WordNet
 *
 */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    // need to be able to support the following lookup
    // "punk" -> 18,21
    // 0 -> "juvenile juvenile_person"
    private Map<Integer, Set<String>> idToNouns;
    private Map<String, Set<Integer>> nounsToId;
    private Digraph G;
    private ShortestCommonAncestor shrt;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new NullPointerException();
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

        // Creating a ShortestCommonAncestor object (also checking for violation of DAG/multiple roots rule)
        this.shrt = new ShortestCommonAncestor(G);
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

    // all WordNet nouns
    public Iterable<String> nouns() {
        return nounsToId.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        }
        return nounsToId.keySet().contains(word);
    }

    // Checks if valid nouns were passed as arguments
    private void nounCheck(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new NullPointerException();
        }

        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
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

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("wordnnet/synsets.txt", "wordnet/hypernyms.txt");
        System.out.println();
        System.out.println(net.sca("waif", "punk"));
        System.out.println(net.distance("waif", "punk"));
        System.out.println(net.isNoun("john"));
        System.out.println(net.isNoun("bond"));

        System.out.println("Testing hypernymsWrong8BFS:");
        WordNet net_1 = new WordNet("wordnet/synsets8.txt", "wordnet/hypernyms8WrongBFS.txt");
        System.out.println();
        System.out.println("Shortest ancestor between e(4) and c(2) should be d(3): " + net_1.sca("e", "c"));
        System.out.println("Shortest distance between e(4) and c(2) should be 4: " + net_1.distance("e", "c"));

        System.out.println("Testing hypernyms8ManyAncestorsBFS:");
        WordNet net_2 = new WordNet("wordnet/synsets8.txt", "wordnet/hypernyms8ManyAncestors.txt");
        System.out.println();
        System.out.println("Shortest ancestor between e(4) and c(2) should be e(4): " + net_2.sca("e", "c"));
        System.out.println("Shortest distance between e(4) and c(2) should be 1: " + net_2.distance("e", "c"));

    }
}