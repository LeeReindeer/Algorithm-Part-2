import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author leer
 * Created at 6/7/18 9:17 AM
 */
public class WordNet {

  // total vertices from synsets.txt file
  private final static int VERTICES = 82192;
  private final static int EDGES = 84505;


  /**
   * a word can have many ids(of different meaning)
   * and an id can has many synsets words
   * <p>
   * find id by word
   */
  private HashMap<String, ArrayList<Integer>> synsetsIdMap;
  // find synsets(spilt by space) by id
  private HashMap<Integer, String> synsetsMap;
  //private HashMap<Integer, String> glossMap;

  private SAP sap = null;

  private Digraph hypernymsGraph;

  /**
   * constructor takes the name of the two input files
   * Any advice on how to read in and parse the synset and hypernym data files?
   * Use the readLine() method in our In library to read in the data one line at a time.
   * Use the split() method in Java's String library to divide a line into fields.
   * You can find an example using split() in Domain.java. Use Integer.parseInt() to convert string id numbers into integers.
   */
  public WordNet(String synsets, String hypernyms) {
    checkValid(synsets, hypernyms);

    synsetsIdMap = new HashMap<>((int) (VERTICES * 1.5));
    synsetsMap = new HashMap<>(VERTICES);
    hypernymsGraph = new Digraph(VERTICES);

    In input1 = new In(synsets);
    In input2 = new In(hypernyms);
    // read synsets
    while (!input1.isEmpty()) {
      String line = input1.readLine();
      String[] slices = line.split(","); // 3 slices
      int id = Integer.parseInt(slices[0]);
      String[] synsetWords = slices[1].split(" "); // split with space
//      String gloss = slices[2];
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < synsetWords.length; i++) {
        sb.append(synsetWords[i]);
        if (i != synsetWords.length - 1) {
          sb.append(" ");
        }

        ArrayList<Integer> idList = synsetsIdMap.get(synsetWords[i]);
        if (idList == null) {
          idList = new ArrayList<>();
        }
        idList.add(id);
        synsetsIdMap.put(synsetWords[i], idList);
      }
      synsetsMap.put(id, sb.toString());
    }
    input1.close();

    while (!input2.isEmpty()) {
      String line = input2.readLine();
      String[] slices = line.split(","); // >= 2 slices
      int vertex = Integer.parseInt(slices[0]);
      for (int i = 1; i < slices.length; i++) {
        int ancestor = Integer.parseInt(slices[i]);
        hypernymsGraph.addEdge(vertex, ancestor);
      }
    }
    input2.close();

    sap = new SAP(hypernymsGraph);
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    return synsetsMap.values();
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    checkValid(word, "");
    return getIdByWord(word) != null;
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    checkValid(nounA, nounB);
    ArrayList<Integer> idA = getIdByWord(nounA);
    ArrayList<Integer> idB = getIdByWord(nounB);
    return sap.length(idA, idB);
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    checkValid(nounA, nounB);
    ArrayList<Integer> idA = getIdByWord(nounA);
    ArrayList<Integer> idB = getIdByWord(nounB);
    return getWordById(sap.ancestor(idA, idB));
  }

  private ArrayList<Integer> getIdByWord(String noun) {
    return synsetsIdMap.get(noun);
  }

  private String getWordById(int id) {
    return synsetsMap.get(id);
  }

  private void checkValid(String s1, String s2) {
    if (s1 == null || s2 == null) {
      throw new IllegalArgumentException();
    }
  }

  // do unit testing of this class
  public static void main(String[] args) {
    WordNet wordNet = new WordNet(args[0], args[1]);
    System.out.println("vertex: " + wordNet.hypernymsGraph.V());
    String nounA = "worm";
    String nounB = "bird";
    System.out.printf("id of \"%s\": %s %n", nounA, wordNet.getIdByWord(nounA));
    System.out.printf("id of \"%s\": %s %n", nounB, wordNet.getIdByWord(nounB));
    System.out.printf("sap of \"%s\" and \"%s\": %s %n", nounA, nounB, wordNet.sap(nounA, nounB));
  }

}
