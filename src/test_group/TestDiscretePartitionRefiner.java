package test_group;

import org.openscience.cdk.group.AbstractDiscretePartitionRefiner;
import org.openscience.cdk.group.IEquitablePartitionRefiner;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;

import engine.Graph;


/**
 * Test implementation of a discrete partition refiner for simple graphs.
 * 
 * @author maclean
 *
 */
public class TestDiscretePartitionRefiner extends AbstractDiscretePartitionRefiner {
    
    private Graph graph;
    
    private boolean useColors;
    
    public TestDiscretePartitionRefiner() {
        this(false);
    }
    
    public TestDiscretePartitionRefiner(boolean useColors) {
        this.useColors = useColors;
    }
    
    private void setup(Graph graph) {
        int n = graph.getVertexCount();
        this.graph = graph;
        SSPermutationGroup group = new SSPermutationGroup(new Permutation(n));
        IEquitablePartitionRefiner refiner = 
            new TestEquitablePartitionRefiner(graph, useColors);
        setup(group, refiner);
    }
    
    private void setup(Graph graph, SSPermutationGroup group) {
        IEquitablePartitionRefiner refiner = 
            new TestEquitablePartitionRefiner(graph, useColors);
        setup(group, refiner);
    }

    public boolean isCanonical(Graph graph) {
        setup(graph);
        refine(Partition.unit(graph.getVertexCount()));
        return firstIsIdentity();
    }
    
    public boolean isCanonical(Graph graph, Partition initialPartition) {
        setup(graph);
        refine(initialPartition);
        return firstIsIdentity();
    }
    
    public SSPermutationGroup getAutomorphismGroup(
            Graph graph, Partition partition) {
        setup(graph);
        refine(partition);
        return getGroup();
    }
    
    public SSPermutationGroup getAutomorphismGroup(Graph graph) {
        setup(graph);
        int n = graph.getVertexCount();
        Partition unit = Partition.unit(n);
        refine(unit);
        return getGroup();
    }
    
    public SSPermutationGroup getAutomorphismGroup(
            Graph graph, SSPermutationGroup group) {
        setup(graph, group);
        refine(Partition.unit(graph.getVertexCount()));
        return getGroup();
    }
    
    @Override
    public int getVertexCount() {
        return graph.getVertexCount();
    }

    @Override
    public boolean isConnected(int i, int j) {
        return graph.isConnected(i, j);
    }

}
