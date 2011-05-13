package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Simplistic, temporary wrapper around a graph
 *
 */
public class AdjacencyGraph {
    
    private List<int[]> rows;
    
    public AdjacencyGraph(boolean makeFirstTwoRows) {
        this.rows = new ArrayList<int[]>();
        if (makeFirstTwoRows) {
            this.rows.add(new int[] {0, 1});
            this.rows.add(new int[] {1, 0});
        }
    }
    
    public int size() {
        return this.rows.size();
    }
    
    public void addRow(int... values) {
        this.rows.add(values);
    }
    
    public AdjacencyGraph makeNew(int x) {
        int n = this.size() + 1;
        AdjacencyGraph graph = new AdjacencyGraph(false);
        int[] bits = new int[n];
        for (int y = n - 1; y > -1; y--) {
            bits[n - y - 1] = (x >> y) & 1;
        }
        
        graph.rows.add(bits);
        for (int r = 0; r < n - 1; r++) {
            int[] row = this.rows.get(r);
            int[] newRow = new int[n];
            newRow[0] = bits[r + 1];
            for (int i = 0; i < n - 1; i++) {
                newRow[i + 1] = row[i];
            }
            graph.rows.add(newRow);
        }
        System.out.print("made " + graph.toString() 
                + " from "  + this.toString() 
                + " and " + x + " = " + Arrays.toString(bits)
                + " " + graph.asGraph().getSortedEdgeString());
        return graph;
    }
    
    public Graph asGraph() {
        Graph g = new Graph();
        for (int i = 0; i < this.rows.size(); i++) {
            int[] row = this.rows.get(i);
            for (int j = i + 1; j < row.length; j++) {
                if (row[j] == 1) {
                    g.makeEdge(i, j);
                }
            }
        }
        return g;
    }
    
    public String toString() {
        int i = 0;
        String s = "";
        for (int[] row : this.rows) {
            for (int j = i + 1; j < rows.size(); j++) {
                if (row[j] == 1) {
                    s += "1";
                } else {
                    s += "0";
                }
            }
            i++;
        }
        return s;
    }
}
