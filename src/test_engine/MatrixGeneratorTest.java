package test_engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.group.Graph;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;

import test_group.TestDiscretePartitionRefiner;



import engine.MatrixGenerator;

public class MatrixGeneratorTest {
    
    public Graph makeSetSystemGraph(String[] labels) {
        // labels assumed to be ordered
        Map<String, Integer> map = new HashMap<String, Integer>();
        int current = 0;
        for (String label : labels) {
            if (map.containsKey(label)) {
                current = map.get(label);
            } else {
                current++;
                map.put(label, current);
            }
        }
        
        int blockOffset = labels.length -1;
        
        Graph graph = new Graph();
        for (int i = 0; i < labels.length; i++) {
            graph.makeEdge(i, blockOffset + map.get(labels[i]));
        }
        return graph;
    }
    
    @Test
    public void testGenerate() {
        List<String> strings = new ArrayList<String>();
        int n = 6;
        MatrixGenerator.run(n, strings);
        Set<String> stringSet = new HashSet<String>();
        stringSet.addAll(strings);
        Assert.assertEquals(stringSet.size(), strings.size());
    }
    
    @Test
    public void testTransversal() {
        Graph graph = new Graph("0:2,0:3,1:2,1:3");
//        String[] labels = new String[] {"C", "C", "O", "N"};
        int labelN = 4;
        Partition partition = new Partition();
        partition.addCell(0, 1);
        partition.addCell(2);
        partition.addCell(3);
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        SSPermutationGroup labelGroup = refiner.getAutomorphismGroup(graph, partition);
        SSPermutationGroup graphGroup = refiner.getAutomorphismGroup(graph);
        SSPermutationGroup symLabelN = SSPermutationGroup.makeSymN(labelN);
        System.out.println(labelGroup.order());
        int i = 0;
        for (Permutation permutation : labelGroup.all()) {
            System.out.println("label group (" + i + ") = " + permutation);
            i++;
        }
        int j = 0;
        for (Permutation permutation : graphGroup.all()) {
            System.out.println("graph group (" + j + ") = " + permutation);
            j++;
        }
        int k = 0;
        for (Permutation permutation : symLabelN.transversal(graphGroup)) {
            System.out.println("transversal(label, graph) (" + k + ") = " + permutation);
            k++;
        }
    }
    
    @Test
    public void testSetSystem() {
        String[] labels = new String[] {"C", "C", "O", "N"};
        Graph graph = makeSetSystemGraph(labels);
        System.out.println(graph);
        Partition partition = new Partition();
        partition.addCell(0, 1, 2, 3);
        partition.addCell(4, 5, 6);
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        SSPermutationGroup labelGroup = refiner.getAutomorphismGroup(graph, partition);
        int i = 0;
        for (Permutation permutation : labelGroup.all()) {
            System.out.println("label group (" + i + ") = " + permutation + " " + permutation.toCycleString());
            i++;
        }
        int labelN = graph.getVertexCount();
        SSPermutationGroup symLabelN = SSPermutationGroup.makeSymN(labelN);
        int k = 0;
        for (Permutation permutation : symLabelN.transversal(labelGroup)) {
            System.out.println("transversal(label, graph) (" + k + ") = " + permutation);
            k++;
        }
    }
    
    @Test
    public void testInitialGroup() {
        int n = 4;
        SSPermutationGroup group  = new SSPermutationGroup(n);
        group.enter(new Permutation(0, 3, 2, 1));
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        Graph graph = new Graph("0:1,0:3,1:2,2:3");
        SSPermutationGroup labelGroup = refiner.getAutomorphismGroup(graph, group);
        int i = 0;
        for (Permutation permutation : labelGroup.all()) {
            System.out.println("label group (" + i + ") = " + permutation + " " + permutation.toCycleString());
            i++;
        }
        SSPermutationGroup graphGroup = refiner.getAutomorphismGroup(graph);
        int j = 0;
        for (Permutation permutation : graphGroup.all()) {
            System.out.println("graph group (" + j + ") = " + permutation);
            j++;
        }
    }

}
