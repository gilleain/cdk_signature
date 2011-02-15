package degree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DegreeSequenceGenerator {
    
    public class Graph {
        
        private class Edge {
            public int a;
            public int b;
            public Edge(int a, int b) {
                if (a < b) {
                    this.a = a; 
                    this.b = b;
                } else {
                    this.a = b; 
                    this.b = a;
                }
            }
            public Edge copy() {
                return new Edge(a, b);
            }
            public String toString() { return String.format("%s-%s", a, b); }
        }
        
        public List<Edge> edges;
        
        public Graph() {
            edges = new ArrayList<Edge>();
        }

        public Graph connectAll(int vertex, List<Integer> adjacent) {
            Graph graphCopy = copy();
            for (int j : adjacent) {
                graphCopy.connect(vertex, j);
            }
            return graphCopy;
        }

        /**
         * Add an edge to the graph
         * @param a
         * @param b
         */
        public void connect(int a, int b) {
            edges.add(new Edge(a, b));
        }

        public Graph copy() {
            Graph copy = new Graph();
            for (Edge e : this.edges) { copy.edges.add(e.copy()); }
            return copy;
        }
        
        public String toString() {
            return edges.toString(); 
        }
        
    }
    
    public class DegreeSequence {
        
        private class DegreeComparator implements Comparator<Integer> {

            public int compare(Integer o1, Integer o2) {
                return -degreeMap.get(o1).compareTo(degreeMap.get(o2));
            }
            
        }
        
        private DegreeComparator degreeComparator = new DegreeComparator();
        
        private Map<Integer, Integer> degreeMap;
        
        private List<Integer> vertexIndicesByDegree;
        
        private boolean activeConnection;
        
        private int firstConnectionIndex;
        
        private int secondConnectionIndex;
        
        public DegreeSequence() {
            degreeMap = new HashMap<Integer, Integer>();
            vertexIndicesByDegree = new ArrayList<Integer>();
            
            // these are for checking that connections are valid
            activeConnection = false;
            firstConnectionIndex = -1;
            secondConnectionIndex = -1;
        }
        
        public DegreeSequence(List<Integer> degrees) {
            this();
            Collections.sort(degrees, Collections.reverseOrder());
            
            // initially, the vertices are ordered by the degrees
            int vertexCount = 0;
            for (Integer degree : degrees) {
                degreeMap.put(vertexCount, degree);
                vertexIndicesByDegree.add(vertexCount);
                vertexCount++;
            }
            
        }
        
        public DegreeSequence(DegreeSequence other) {
            this();
            int i = 0;
            for (Integer vertexIndex : other.degreeMap.keySet()) {
                degreeMap.put(vertexIndex, other.degreeMap.get(vertexIndex));
                vertexIndicesByDegree.add(other.vertexIndicesByDegree.get(i));
                i++;
            }
            Collections.sort(vertexIndicesByDegree, degreeComparator);
        }

        /**
         * @return true if all the degrees are 0
         */
        public boolean isEmpty() {
            return nonZeroCount() == 0;
        }

        /**
         * @return the number of non-zero values
         */
        public int nonZeroCount() {
            int count = 0;
            for (Integer vertexIndex : degreeMap.keySet()) {
                if (degreeMap.get(vertexIndex) > 0) {
                    count++;
                }
            }
            return count;
        }

        /**
         * @return the highest value in the degree sequence
         */
        public int max() {
            int max = 0;
            for (Integer vertexIndex : degreeMap.keySet()) {
                int degree = degreeMap.get(vertexIndex);
                
                // XXX - this will be the /first/ highest encountered...
                if (degree > max) {
                    max = degree;
                }
            }
            return max;
        }

        /**
         * @return true if this sequence passes the basic tests for graphicality
         */
        public boolean isGraphical() {
            int max = 0;
            int sum = 0;
            int nonZeroCount = 0;
            for (Integer vertexIndex : degreeMap.keySet()) {
                int degree = degreeMap.get(vertexIndex);
                if (activeConnection && 
                        (vertexIndex == firstConnectionIndex 
                      || vertexIndex == secondConnectionIndex)) {
                    degree--;
                }
                if (degree > max) {
                    max = degree;
                }
                sum += degree;
                if (degree > 0) {
                    nonZeroCount++;
                }
            }
            
            return max < nonZeroCount && sum % 2 == 0;
        }

        /**
         * @return the vertex index with the highest target degree
         */
        public int getFirst() {
            int max = 0;
            int maxIndex = 0;
            for (int vertexIndex : degreeMap.keySet()) {
                int degree = degreeMap.get(vertexIndex);
                if (degree > max) {
                    max = degree;
                    maxIndex = vertexIndex;
                }
            }
            return maxIndex;
        }
        
        /**
         * @return the vertex index with the lowest (nonzero) target degree
         */
        public int getLast() {
            int min = degreeMap.size(); // bit of a hack...
            int minIndex = 0;
            for (int vertexIndex : degreeMap.keySet()) {
                int degree = degreeMap.get(vertexIndex);
                if (degree > 0 && degree <= min) {    // XXX get the last seen
                    min = degree;
                    minIndex = vertexIndex;
                }
            }
            return minIndex;
        }

        public int getVertexIndexForDegreeIndex(int k) {
            return vertexIndicesByDegree.get(k);
        }

        /**
         * @param i the vertex index to remove
         * @param intSet the adjacency set to reduce by
         * @return a new degree sequence with intSet removed
         */
        public DegreeSequence reduceBy(int i, List<Integer> intSet) {
            DegreeSequence reduced = new DegreeSequence();
            for (int vertexIndex : degreeMap.keySet()) {
                int degree = degreeMap.get(vertexIndex);
                if (intSet.contains(vertexIndex)) {
                    reduced.set(vertexIndex, degree - 1);
                } else if (vertexIndex == i) {
                    reduced.set(vertexIndex, 0);
                } else {
                    reduced.set(vertexIndex, degree);
                }
            }
            for (int v : vertexIndicesByDegree) {
                reduced.vertexIndicesByDegree.add(v);
            }
            Collections.sort(reduced.vertexIndicesByDegree, degreeComparator);
            return reduced;
        }
        
        public void reduce(int vertexIndex) {
            set(vertexIndex, degreeMap.get(vertexIndex) - 1);
        }

        public void set(int vertexIndex, int degree) {
            degreeMap.put(vertexIndex, degree);
        }
        
        public void connect(int vertexIndexA, int vertexIndexB) {
            reduce(vertexIndexA);
            reduce(vertexIndexB);
        }

        /**
         * Attempt a connection between these two vertices.
         * 
         * @param first
         * @param k
         */
        public void attemptConnection(int first, int k) {
            firstConnectionIndex = first;
            secondConnectionIndex = k;
            activeConnection = true;
        }

        /**
         * Commit to the connection made in attemptConnection.
         */
        public void commitConnection() {
            connect(firstConnectionIndex, secondConnectionIndex);
            activeConnection = false;
        }

        /**
         * Undo an attempted connection.
         */
        public void undoConnection() {
            firstConnectionIndex = -1;
            secondConnectionIndex = -1;
            activeConnection = false;
        }
        
        public String toString() {
            return degreeMap.values().toString();
        }
        
    }
    
    public void gen(int... degrees) {
        List<Integer> degreeList = new ArrayList<Integer>();
        for (int degree : degrees) { degreeList.add(degree); }
        DegreeSequence degSeq = new DegreeSequence(degreeList);
        gen(degSeq, new Graph());
    }
    
    public void gen(DegreeSequence degSeq, Graph graph) {
        // if the sequence is [0,0,...,0] then we finish
        boolean isEmpty = degSeq.isEmpty(); 
        if (isEmpty) {
            System.out.println(graph + " " + degSeq);
            return;
        }
        
        // this is the index of the vertex with the highest degree
        int first = degSeq.getFirst();
        
        // the vertex with the lowest (non zero) degree
        int last = degSeq.getLast();
        
        // the rightmost Adjacency set of the first vertex
        List<Integer> Ar = new ArrayList<Integer>();
        
        // make a copy of the degree sequence to use to make Ar
        DegreeSequence arCopy = new DegreeSequence(degSeq);
        
        // connect the first to the last
        Ar.add(last);
        arCopy.connect(first, last);
        
        // tracks the rightmost index
        int k = last - 1;
        
        // a 'stub' is a half-bond : this is the count for the first vertex
        int stubCount = arCopy.max() - 1;   // -1 because of 1-n connection 
        while (stubCount > 0 && k > 0) {
            int partnerIndex = arCopy.getVertexIndexForDegreeIndex(k);
            arCopy.attemptConnection(first, partnerIndex);
            if (arCopy.isGraphical()) {
                Ar.add(k);
                arCopy.commitConnection();
                stubCount--;
            } else {
                arCopy.undoConnection();
            }
            k--;
        }
        
        // could occur if the degree sequence is not graphical 
        if (stubCount != 0) {
            return; // TODO ? or raise an error?
        }
        
        int maxRank = colexRank(Ar);
        for (int rank = 0; rank < maxRank; rank++) {
            List<Integer> adjacentIndices = new ArrayList<Integer>();
            for (int d : colexUnrank(rank, Ar.size(), last)) {
                adjacentIndices.add(degSeq.getVertexIndexForDegreeIndex(d));
            }
            DegreeSequence degSeqReduced = degSeq.reduceBy(first, adjacentIndices);
            System.out.println(degSeqReduced + " " + adjacentIndices);
            gen(degSeqReduced, graph.connectAll(first, adjacentIndices));
        }
    }

    public static List<Integer> colexUnrank(int rank, int k, int size) {
        List<Integer> intSet = new ArrayList<Integer>();
        int x = size;
        for (int i = 1; i <= k; i++) {
            while (x > 0 && binomial(x, k + 1 - i) > rank) {
                x--;
            }
            intSet.add(x + 1);
            rank = rank - binomial(x, k + 1 - i);
        }
        Collections.reverse(intSet);
        return intSet;
    }

    public static int colexRank(List<Integer> intSet) {
        Collections.sort(intSet, Collections.reverseOrder());
        int rank = 0;
        int k = intSet.size();
        for (int i = 1; i <= k; i++) {
            rank += binomial(intSet.get(i - 1) - 1, k + 1 - i);
        }
        return rank;
    }
    
    private static int binomial(int n, int r) {
        if (r < 0 || n < r) return 0;
        if ((2 * r) > n) r = n - r;
        int b = 1;
        if (r > 0) {
            for (int i = 0; i < r; i++) {
                b = (b * (n - i)) / (i + 1);
            }
        }
        return b;
    }

}
