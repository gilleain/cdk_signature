package test_group;

import java.util.HashSet;
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
     * @see org.openscience.cdk.group.AbstractEquitablePartitionRefiner#getConnected(int)
     */
    public Set<Integer> getConnected(int vertexIndex) {
        Set<Integer> connected = new HashSet<Integer>();
        for (int i : graph.getConnected(vertexIndex)) { connected.add(i); }
//        for (int i : graph.getSameColorConnected(vertexIndex)) { connected.add(i); }
        return connected;
    }

    /* (non-Javadoc)
     * @see org.openscience.cdk.group.AbstractEquitablePartitionRefiner#getNumberOfVertices()
     */
    public int getNumberOfVertices() {
        return graph.getVertexCount();
    }

    @Override
    public int neighboursInBlock(Set<Integer> block, int vertexIndex) {
        // TODO Auto-generated method stub
        return 0;
    }

}
