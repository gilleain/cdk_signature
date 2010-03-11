package test_group;

import java.util.Set;

import org.openscience.cdk.group.AbstractEquitablePartitionRefiner;
import org.openscience.cdk.group.Graph;
import org.openscience.cdk.group.IEquitablePartitionRefiner;


/**
 * Implementation of an abstract equitable partition refiner for simple graphs.
 * 
 * @author maclean
 *
 */
public class TestEquitablePartitionRefiner extends AbstractEquitablePartitionRefiner 
                                       implements IEquitablePartitionRefiner {
    
    private Graph graph;
    
    public TestEquitablePartitionRefiner(Graph graph) {
        this.graph = graph;
    }

    /* (non-Javadoc)
     * @see org.openscience.cdk.group.AbstractEquitablePartitionRefiner#getNumberOfVertices()
     */
    public int getNumberOfVertices() {
        return graph.getVertexCount();
    }

    @Override
    public int neighboursInBlock(Set<Integer> block, int vertexIndex) {
        int n = 0;
        for (int i : graph.getConnected(vertexIndex)) {
            if (block.contains(i)) {
                n++;
            }
        }
        return n;
    }

}
