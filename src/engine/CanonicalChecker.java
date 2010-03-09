package engine;

import org.openscience.cdk.group.Graph;
import org.openscience.cdk.group.SSPermutationGroup;

import test_group.TestDiscretePartitionRefiner;

import cages.Permutor;

public class CanonicalChecker {
    
  
    public static boolean isCanonical(Graph graph) {
        if (!graph.edgesInOrder()) return false;
        Permutor permutor = new Permutor(graph.getVertexCount());
        String original = graph.getSortedEdgeString();
        while (permutor.hasNext()) {
            int[] p = permutor.getNextPermutation();
            String permuted = graph.getSortedPermutedEdgeString(p);
            
            if (original.compareTo(permuted) > 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isCanonical2(Graph graph) {
        if (!graph.edgesInOrder()) return false;
        int n = graph.getVertexCount();
        SSPermutationGroup symN = SSPermutationGroup.makeSymN(n);
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        SSPermutationGroup autN = refiner.getAutomorphismGroup(graph);
        
        String original = graph.getSortedEdgeString();
        TraversalBacktracker traveller = new TraversalBacktracker(original, symN, graph, autN);
        symN.apply(traveller);
        return traveller.checkedAll();
    }
    
    public static boolean isCanonical3(Graph graph) {
        if (!graph.edgesInOrder()) return false;
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        refiner.isCanonical(graph);
        return refiner.getFirst().isIdentity();
    }
    
    public static boolean isCanonical4(Graph graph) {
        if (!graph.edgesInOrder()) return false;
//        if (!graph.colorsInOrder()) return false;
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        refiner.isCanonical(graph);
        return refiner.getFirst().isIdentity();
    }
}
