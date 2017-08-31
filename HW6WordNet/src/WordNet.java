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

    // Prints path from first noun to second.
    // NOT REQUIRED
    private String printPath(String noun1, String noun2) {
        nounCheck(noun1, noun2);
        Set<Integer> a = nounsToId.get(noun1);
        Set<Integer> b = nounsToId.get(noun2);
        int anc = shrt.ancestor(b, a);
        Stack<Integer> stack = shrt.path();
        StringBuilder str = new StringBuilder();
        while (!stack.isEmpty()) {
            Set<String> set = idToNouns.get(stack.pop());
            for (String s : set){
                str.append(s + ", ");
            }
            str.delete(str.length()-2, str.length());
            str.append(" ->\n");
        }

        return str.toString();
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");
        System.out.println("Testing distance(), sca() and custom printPath() methods:");
        System.out.println("Print path from \"George_Bush\" to \"Putin\":");
        System.out.println("Total distance between \"George_Bush\" to \"Putin\" is: " + net.distance("George_Bush", "Putin"));
        System.out.println("Common ancestor for \"George_Bush\" and \"Putin\" is: " + net.sca("George_Bush", "Putin"));
        System.out.println(net.printPath("George_Bush", "Putin"));

        System.out.println("Print path from \"George_Bush\" to \"chimpanzee\":");
        System.out.println("Total distance between \"George_Bush\" to \"chimpanzee\" is: " + net.distance("George_Bush", "chimpanzee"));
        System.out.println("Common ancestor for \"George_Bush\" and \"chimpanzee\" is: " + net.sca("George_Bush", "chimpanzee"));
        System.out.println(net.printPath("George_Bush", "chimpanzee"));

        System.out.println("Print path from \"Putin\" to \"chimpanzee\":");
        System.out.println("Total distance between \"Putin\" to \"chimpanzee\" is: " + net.distance("Putin", "chimpanzee"));
        System.out.println("Common ancestor for \"Putin\" and \"chimpanzee\" is: " + net.sca("Putin", "chimpanzee"));
        System.out.println(net.printPath("Putin", "chimpanzee"));

        System.out.println("Print path from \"waif\" from \"punk\":");
        System.out.println("Total distance between \"waif\" to \"punk\" is: " + net.distance("waif", "punk"));
        System.out.println("Common ancestor for  \"waif\" and \"punk\" is: " + net.sca("waif", "punk"));
        System.out.println(net.printPath("waif", "punk"));

        System.out.println("Print path from \"abracadabra\" from \"menace\":");
        System.out.println("Total distance between \"abracadabra\" to \"menace\" is: " + net.distance("abracadabra", "menace"));
        System.out.println("Common ancestor for  \"abracadabra\" and \"menace\" is: " + net.sca("abracadabra", "menace"));
        System.out.println(net.printPath("abracadabra", "menace"));

        System.out.println("Testing isNoun methods:");
        System.out.println("Is bond a noun?: " + net.isNoun("bond"));
        System.out.println("Is George_Bush a noun?: " + net.isNoun("George_Bush"));
        System.out.println("Is JFK a noun?: " + net.isNoun("JFK"));
        System.out.println("Is abracadabra a noun?: " + net.isNoun("abracadabra"));
        System.out.println();

        System.out.println("Testing hypernymsWrong8BFS:");
        WordNet net_1 = new WordNet("wordnet/synsets8.txt", "wordnet/hypernyms8WrongBFS.txt");
        System.out.println("Shortest ancestor between e(4) and c(2) should be d(3): " + net_1.sca("e", "c"));
        System.out.println("Shortest distance between e(4) and c(2) should be 4: " + net_1.distance("e", "c"));

        System.out.println("Testing hypernyms8ManyAncestorsBFS:");
        WordNet net_2 = new WordNet("wordnet/synsets8.txt", "wordnet/hypernyms8ManyAncestors.txt");
        System.out.println();
        System.out.println("Shortest ancestor between e(4) and c(2) should be e(4): " + net_2.sca("e", "c"));
        System.out.println("Shortest distance between e(4) and c(2) should be 1: " + net_2.distance("e", "c"));

    }
}