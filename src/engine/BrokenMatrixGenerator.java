package engine;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.group.Graph;

import test_group.TestDiscretePartitionRefiner;


/**
 * Generates graphs by adding to canonical adjacency matrices. Since canonical
 * checking by partition refinement prefers those matrices that have a top
 * diagonal that is minimal, the procedure in this class guarantees that all
 * these canonical structures (at least) are visited. 
 * 
 * THIS APPROACH CANNOT WORK :(
 * 
 * @author maclean
 *
 */
public class BrokenMatrixGenerator {
    
    private TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
    
//    private int max;
    
    public List<AdjacencyGraph> generate(int max) {
//        this.max = max;
        List<AdjacencyGraph> results = new ArrayList<AdjacencyGraph>();
        results.add(new AdjacencyGraph(true));
        generate(results, 0);
        return results;
    }
    
    public boolean isCanonical(AdjacencyGraph graph) {
        Graph edgeGraph = graph.asGraph();
        return refiner.isCanonical(edgeGraph);
    }
    
    public List<AdjacencyGraph> generate(List<AdjacencyGraph> previousGeneration, int n) {
        List<AdjacencyGraph> nextGeneration = new ArrayList<AdjacencyGraph>();
        int maxValue = ((int)Math.pow(2, n)) / 2;
        for (AdjacencyGraph graph : previousGeneration) {
            for (int i = 1; i < maxValue; i++) {
                AdjacencyGraph child = graph.makeNew(i);
                if (isCanonical(child)) {
                    nextGeneration.add(child);
                    System.out.println(" CANON");
                } else {
                    System.out.println("");
                }
            }
        }
        return nextGeneration;
    }

}
