/**
 * Created: Artiom Ciumac
 * Date (mm/dd/yy): 8/31/17
 * Name: WordNet
 *
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.net = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int max_dist = 0;
        String farNoun = null;
        for (int i = 0; i < nouns.length; i++) {
            int dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                dist += net.distance(nouns[i], nouns[j]);
            }

            // Check if this is the new max distance
            if (dist > max_dist) {
                farNoun = nouns[i];
                max_dist = dist;
            }
        }

        return farNoun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("wordnet/synsets.txt", "wordnet/hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        String[] arr = {"wordnet/outcast5.txt", "wordnet/outcast8.txt", "wordnet/outcast11.txt"};
        for (int t = 0; t < arr.length; t++) {
            In in = new In(arr[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(arr[t] + ": " + outcast.outcast(nouns));
        }

    }
}