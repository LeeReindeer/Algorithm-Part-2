import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @author leer
 * Created at 6/7/18 4:54 PM
 * Use BFS
 * @see edu.princeton.cs.algs4.BreadthFirstDirectedPaths
 */
public final class SAP {

  private final Digraph digraph;

  // cache, HashSet for vertices, Pair for ancestor and length
  private HashMap<HashSet<Integer>, Pair<Integer, Integer>> ancestorMap = new HashMap<>();

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    this.digraph = G;
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    checkVertex(Arrays.asList(v, w));
    HashSet<Integer> set = new HashSet<>(Arrays.asList(v, w));
    if (ancestorMap.get(set) == null) {
      ancestor(v, w);
    }
    int length = ancestorMap.get(set).getValue();
    return length == Integer.MAX_VALUE ? -1 : length;
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    checkVertex(Arrays.asList(v, w));
    HashSet<Integer> set = new HashSet<>(Arrays.asList(v, w));
    if (ancestorMap.get(set) != null) {
//      System.out.println("get from map!");
      return ancestorMap.get(set).getKey();
    }

    BreadthFirstDirectedPaths BFS1 = new BreadthFirstDirectedPaths(digraph, v);
    BreadthFirstDirectedPaths BFS2 = new BreadthFirstDirectedPaths(digraph, w);
    int ancestor = -1;
    int shortestLength = Integer.MAX_VALUE;

    for (int i = 0; i < digraph.V(); i++) {
      if (BFS1.hasPathTo(i) && BFS2.hasPathTo(i)) {
        int len = BFS1.distTo(i) + BFS2.distTo(i);
        if (len < shortestLength) {
          ancestor = i;
          shortestLength = len;
        }
      }
    }

    if (ancestor != -1) {
      // if v and w has path
      if (BFS1.hasPathTo(w) || BFS2.hasPathTo(v)) {
        if (BFS1.distTo(w) < shortestLength) {
          shortestLength = BFS1.distTo(w);
        }
        if (BFS2.distTo(v) < shortestLength) {
          shortestLength = BFS2.distTo(v);
        }
      }
    }

    ancestorMap.put(set, new Pair<>(ancestor, shortestLength));
    return ancestor;
  }

  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    checkVertex(v);
    checkVertex(w);
    HashSet<Integer> set = buildSet(v, w);
    if (ancestorMap.get(set) == null) {
      ancestor(v, w);
    }
    int shortestLength = ancestorMap.get(set).getKey();
    return shortestLength == Integer.MAX_VALUE ? -1 : shortestLength;
  }

  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    checkVertex(v);
    checkVertex(w);
    HashSet<Integer> set = buildSet(v, w);
    if (ancestorMap.get(set) != null) {
//      System.out.println("get from map");
      return ancestorMap.get(set).getKey();
    }

    BreadthFirstDirectedPaths BFS1 = new BreadthFirstDirectedPaths(digraph, v);
    BreadthFirstDirectedPaths BFS2 = new BreadthFirstDirectedPaths(digraph, w);
    int ancestor = -1;
    int shortestLength = Integer.MAX_VALUE;

    for (int i = 0; i < digraph.V(); i++) {
      if (BFS1.hasPathTo(i) && BFS2.hasPathTo(i)) {
        int len = BFS1.distTo(i) + BFS2.distTo(i);
        if (len < shortestLength) {
          ancestor = i;
//          System.out.println(ancestor);
          shortestLength = len;
        }
      }
    }
    ancestorMap.put(set, new Pair<>(ancestor, shortestLength));
    return ancestor;
  }

  private void checkVertex(Iterable<Integer> vertex) {
    if (vertex == null || !vertex.iterator().hasNext()) {
      throw new IllegalArgumentException();
    }
    for (Integer v : vertex) {
      if (v == null || v >= digraph.V() || v < 0) {
        throw new IllegalArgumentException();
      }
    }
  }

  private HashSet<Integer> buildSet(Iterable<Integer> v, Iterable<Integer> w) {
    HashSet<Integer> set = new HashSet<>();
    if (v != null) {
      for (int vv : v) {
        set.add(vv);
      }
    }
    if (w != null) {
      for (int ww : w) {
        set.add(ww);
      }
    }
    return set;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    Digraph digraph = new Digraph(new In(args[0]));
//    System.out.println(digraph);
    SAP sap = new SAP(digraph);
    while (!StdIn.isEmpty()) {
      int v = StdIn.readInt();
      int w = StdIn.readInt();
      int length = sap.length(v, w);
      int ancestor = sap.ancestor(v, w);
      System.out.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
    // v = {13, 23 ,24};
    // w = {6, 16, 17}
//      List<Integer> v = Arrays.asList(13, 23, 24);
//      List<Integer> w = Arrays.asList(6, 16, 17);
//      List<Integer> w = Arrays.stream(StdIn.readAllInts()).boxed().collect(Collectors.toList());
//      int length   = sap.length(v, w);
//      int ancestor = sap.ancestor(v, w);
//      StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//    System.out.println(digraph);
//    System.out.println(sap.ancestor(7, 8)); //3
//    System.out.println(sap.ancestor(7, 8));
//    System.out.println(sap.ancestor(8, 7));
//    System.out.println(sap.ancestor(9, 2)); //0
//    System.out.println(sap.ancestor(6, 3)); //-1
//    System.out.println(sap.ancestor(3, 12));
  }
}
