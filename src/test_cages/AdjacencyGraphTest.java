package test_cages;

import org.junit.Test;

import engine.AdjacencyGraph;


public class AdjacencyGraphTest {
    
    @Test
    public void testBasicUsage() {
        AdjacencyGraph g = new AdjacencyGraph(true);
        System.out.println(g.asGraph().toString());
        g = g.makeNew(1);
        System.out.println(g.asGraph().toString());
        g = g.makeNew(2);
        System.out.println(g.asGraph().toString());
    }
    
    @Test
    public void testCreateFromIntArray() {
        AdjacencyGraph line = new AdjacencyGraph(false);
        line.addRow(0, 0, 0, 1);
        line.addRow(0, 0, 1, 0);
        line.addRow(0, 1, 0, 1);
        line.addRow(1, 0, 1, 0);
        System.out.println(line.asGraph().getSortedEdgeString());
    }

}
