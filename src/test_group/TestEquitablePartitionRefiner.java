package test_group;

import java.util.List;
import java.util.Set;

import org.openscience.cdk.group.AbstractEquitablePartitionRefiner;
import org.openscience.cdk.group.IEquitablePartitionRefiner;

import engine.Graph;


/**
 * Implementation of an abstract equitable partition refiner for simple graphs.
 * 
 * @author maclean
 *
 */
public class TestEquitablePartitionRefiner extends AbstractEquitablePartitionRefiner 
                                       implements IEquitablePartitionRefiner {
    
    private Graph graph;
    
    private boolean useColors;
    
    public TestEquitablePartitionRefiner(Graph graph) {
        this(graph, false);
    }
    
    public TestEquitablePartitionRefiner(Graph graph, boolean useColors) {
        this.graph = graph;
        this.useColors = useColors;
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
        List<Integer> connected;
        if (useColors) {
            connected = graph.getSameColorConnected(vertexIndex);
        } else {
            connected = graph.getConnected(vertexIndex);
        }
        
        for (int i : connected) {
            if (block.contains(i)) {
                n++;
            }
        }
        return n;
    }

}
