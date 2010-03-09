package org.openscience.cdk.group;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Refiner for atom containers, which refines partitions of the atoms to
 * equitable partitions. Used by the {@link CDKDiscretePartitionRefiner}.
 * 
 * @author maclean
 * @cdk.module group
 *
 */
public class CDKEquitablePartitionRefiner extends
        AbstractEquitablePartitionRefiner implements IEquitablePartitionRefiner {
    
    /**
     * The bonds in the atom container, expressed as a list of maps from 
     * connected atoms to bond orders. So, for each atom, there is a map from
     * all connected atoms to the bond orders for the bond between them.
     */
    private List<Map<Integer, Integer>> connectionTable;
    
    public CDKEquitablePartitionRefiner(List<Map<Integer, Integer>> connectionTable) {
        this.connectionTable = connectionTable;
    }

    @Override
    public Set<Integer> getConnected(int vertexIndex) {
        return new HashSet(this.connectionTable.get(vertexIndex).keySet());
    }

    public int neighboursInBlock(Set<Integer> block, int vertexIndex) {
        int neighbours = 0;
        Map<Integer, Integer> connectedOrders = connectionTable.get(vertexIndex); 
        for (int connected : connectedOrders.keySet()) {
            if (block.contains(connected)) {
                neighbours += connectedOrders.get(connected);
            }
        }
        return neighbours;
    }
    
    @Override
    public int getNumberOfVertices() {
        return connectionTable.size();
    }

}
