package test_cages;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import engine.AdjacencyGraph;
import engine.BrokenMatrixGenerator;


public class MatrixGeneratorTest {
    
    @Test
    public void testThreeToFour() {
        List<AdjacencyGraph> previous = new ArrayList<AdjacencyGraph>();
        AdjacencyGraph line = new AdjacencyGraph(false);
        line.addRow(0, 0, 1);
        line.addRow(0, 0, 1);
        line.addRow(1, 1, 0);
        previous.add(line);
        
        AdjacencyGraph triangle = new AdjacencyGraph(false);
        triangle.addRow(0, 1, 1);
        triangle.addRow(1, 0, 1);
        triangle.addRow(1, 1, 0);
        previous.add(triangle);

        BrokenMatrixGenerator generator = new BrokenMatrixGenerator();
        List<AdjacencyGraph> next = generator.generate(previous, 4);
        for (AdjacencyGraph nextGraph : next) {
            String estr = nextGraph.asGraph().getSortedEdgeString();
            String nstr = nextGraph.toString();
            System.out.println(nstr + "\t" + estr + " " + nstr.length());
        }
    }
    
    
    @Test
    public void testFourToFive() {
        List<AdjacencyGraph> previous = new ArrayList<AdjacencyGraph>();
        
        AdjacencyGraph line = new AdjacencyGraph(false);
        line.addRow(0, 0, 0, 1);
        line.addRow(0, 0, 1, 0);
        line.addRow(0, 1, 0, 1);
        line.addRow(1, 0, 1, 0);
        previous.add(line);
        
        AdjacencyGraph tripod = new AdjacencyGraph(false);
        tripod.addRow(0, 0, 0, 1);
        tripod.addRow(0, 0, 0, 1);
        tripod.addRow(0, 0, 0, 1);
        tripod.addRow(1, 1, 1, 0);
        previous.add(tripod);
        
        AdjacencyGraph coathanger = new AdjacencyGraph(false);
        coathanger.addRow(0, 0, 0, 1);
        coathanger.addRow(0, 0, 1, 1);
        coathanger.addRow(0, 1, 0, 1);
        coathanger.addRow(1, 1, 1, 0);
        previous.add(coathanger);
        
        BrokenMatrixGenerator generator = new BrokenMatrixGenerator();
        List<AdjacencyGraph> next = generator.generate(previous, 5);
        for (AdjacencyGraph nextGraph : next) {
            String estr = nextGraph.asGraph().getSortedEdgeString();
            String nstr = nextGraph.toString();
            System.out.println(nstr + "\t" + estr + " " + nstr.length());
        }
    }

}
