import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author leer
 * Created at 6/7/18 4:54 PM
 */
public class Outcast {

  private final WordNet wordNet;
  private int[] distance;

  public Outcast(WordNet wordNet) {
    this.wordNet = wordNet;
  }

  /**
   * Given a list of WordNet nouns x1, x2, ..., xn,
   * which noun is the least related to the others?
   *
   * @param nouns list of nouns to identify
   * @return the outcast none(with max distance)
   */
  public String outcast(String[] nouns) {
    distance = new int[nouns.length];
    for (int i = 0; i < nouns.length; i++) {
      distance[i] = 0;
      for (int j = 0; j < nouns.length; j++) {
        if (!nouns[j].equals(nouns[i])) {
          distance[i] += wordNet.distance(nouns[i], nouns[j]);
        }
      }
    }
    int maxDistance = -1;
    int maxIndex = 0;
    for (int i = 0; i < nouns.length; i++) {
      if (distance[i] > maxDistance) {
        maxIndex = i;
      }
    }
    return nouns[maxIndex];
  }   // given an array of WordNet nouns, return an outcast

  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }

}
