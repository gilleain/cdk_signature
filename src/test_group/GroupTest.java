package test_group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.group.Graph;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;



import cages.OrbitCounter;
import cages.PermutationGenerator;
import cages.TrivialAutomorphismGenerator;

public class GroupTest {
    
    public int factorial(int n) {
        if (n == 1) return n;
        else return n * factorial(n - 1);
    }
    
    public Graph makeCycleGraph(int n) {
        Graph graph = new Graph();
        for (int i = 0; i < n - 1; i++) {
            graph.makeEdge(i, i + 1);
        }
        graph.makeEdge(0, n - 1);
        return graph;
    }
    
    public Graph makeChainGraph(int n) {
        Graph graph = new Graph();
        for (int i = 0; i < n - 1; i++) {
            graph.makeEdge(i, i + 1);
        }
        return graph;
    }
    
    public Graph makeCompleteGraph(int n) {
        Graph graph = new Graph();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                graph.makeEdge(i, j);
            }
        }
        return graph;
    }
    
    public SSPermutationGroup makeAutByBruteForce(Graph graph) {
        List<Permutation> automorphisms = 
            TrivialAutomorphismGenerator.generate(graph);
        int n = graph.getVertexCount();
        
        SSPermutationGroup autG = new SSPermutationGroup(n);
        for (Permutation automorphism : automorphisms) {
            autG.enter(automorphism);
        }
        return autG;
    }
    
    public SSPermutationGroup makeAutByPartitionRefinement(Graph graph) {
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        refiner.isCanonical(graph);
        return refiner.getGroup();
    }
    
    /**
     * Make a group where every possible permutation of [0...n-1] is added. Note
     * that this is a very inefficient way to make the group, but makes testing
     * easier.
     * 
     * @param n the size of the permutation (group)
     * @return the resulting group
     */
    public SSPermutationGroup makeCompleteGroup(int n) {
        PermutationGenerator permutations = new PermutationGenerator(n);
        Permutation identity = new Permutation(n);
        SSPermutationGroup g = 
            new SSPermutationGroup(identity);
        // add each permutation generated to the group
        while (permutations.hasNext()) {
            Permutation p = permutations.next();
            g.enter(p);
        }
        return g;
    }
    
    @Test
    public void testBruteForceColorAutomorphism() {
        Graph graph = new Graph("0:1,0:2,1:3,2:3");
        int[] colors = new int[] {0, 0, 1, 1};
        for (int color : colors) { graph.colors.add(color); }
        String ses = graph.getSortedColoredEdgeString();
        String sces = graph.getSortedColorOnlyEdgeString();
        SSPermutationGroup symN = SSPermutationGroup.makeSymN(4);
        int i = 0;
        
        for (Permutation p : symN.all()) {
            String spes = graph.getSortedPermutedColoredEdgeString(p.getValues());
            String spces = graph.getSortedPermutedColoredOnlyEdgeString(p.getValues());
            int[] permColors = new int[colors.length];
            for (int j = 0; j < colors.length; j++) {
                permColors[j] = colors[p.get(j)];
            }
            boolean colAut = Arrays.equals(colors, permColors);
            boolean graAut = ses.equals(spes);
            boolean col2Aut = sces.equals(spces);
            String is = String.format("%3d", i);
            if (col2Aut) {
                System.out.println(is + " " + p + " " + spes 
                        + " " + Arrays.toString(permColors) + " "  
                        + col2Aut + " " + spces + " COLOR");
            } else if (graAut && colAut) {
                System.out.println(is + " " + p + " " + spes 
                        + " " + Arrays.toString(permColors) + " "  
                        + col2Aut + " " + spces + " GRA + COL ");
            } else if (graAut) {
                System.out.println(is + " " + p + " " + spes 
                        + " " + Arrays.toString(permColors) + " "
                        + col2Aut + " " + spces + " GRA ");
            } else {
                System.out.println(is + " " + p + " " + spes 
                        + " " + Arrays.toString(permColors) + " "
                        + col2Aut + " " + spces);
            }
            i++;
        }
    }
    
    @Test
    public void testMakeSymN() {
        for (int i = 2; i < 15; i++) {
            SSPermutationGroup group = SSPermutationGroup.makeSymN(i);
            System.out.println(i + " " + group.orderAsLong() + " " + group.toString());
        }
    }
    
    @Test
    public void testAddingPermutations() {
        int n = 6;
        int nFactorial = factorial(n);
//        SSPermutationGroup g = makeCompleteGroup(n);
        SSPermutationGroup g = SSPermutationGroup.makeSymN(n); 
        
        // now, check that we can regenerate all the same permutations
        List<Permutation> all = g.all();
        Assert.assertEquals(nFactorial, all.size());
    }
    
    @Test
    public void testCycleGraph() {
        
        // make a cycle of size n
        int n = 4;
        Graph graph = makeCycleGraph(n);
        
        String cert = graph.getSortedEdgeString();
        
        // make the permutation group of size n
        SSPermutationGroup group = makeCompleteGroup(n);
        for (int i = 0; i < n; i++) {
            for (Permutation permutation : group.getLeftTransversal(i)) {
                int[] p = permutation.getValues();
                String spes = graph.getSortedPermutedEdgeString(p);
                String pes = graph.getPermutedEdgeString(p);
                boolean id = spes.equals(cert); 
                System.out.println(
                 i + "\t" + id + "\t" + spes + "\t" + pes + "\t" + permutation);
            }
        }
    }
    
    @Test
    public void makeAutGroupFromGenerators() {
        int n = 8;
        // cube
        Graph graph = new Graph("0:1,0:2,0:4,1:3,1:5,2:3,2:6,3:7,4:5,4:6,5:7,6:7");
        String cert = graph.getSortedEdgeString();
        
        // generators for the automorphism group
        List<Permutation> generators = new ArrayList<Permutation>();
        generators.add(new Permutation(1, 3, 5, 7, 0, 2, 4, 6));
        generators.add(new Permutation(1, 3, 0, 2, 5, 7, 4, 6));
        
        SSPermutationGroup group = new SSPermutationGroup(n, generators);
        for (int i = 0; i < n; i++) {
            int j = 0;
            for (Permutation permutation : group.getLeftTransversal(i)) {
                String spes = graph.getSortedPermutedEdgeString(permutation.getValues()); 
                System.out.println(
                        i + "," + j + "\t"
                        + permutation + "\t" 
                        + permutation.toCycleString() + "\t"
                        + Arrays.toString(permutation.getType()) + "\t"
                        + spes.equals(cert));
                j++;
            }
        }
        int i = 0;
        for (Permutation p : group.all()) {
            String spes = graph.getSortedPermutedEdgeString(p.getValues());
            System.out.println(
                    i + "\t" + p +"\t" + p.toCycleString()
                    + "\t" + Arrays.toString(p.getType()) + "\t"
                    + "\t" + spes + "\t" + spes.equals(cert));
            i++;
        }
    }
    
    @Test
    public void testOrbitCounter() {
        int n = 4;
        SSPermutationGroup group = makeCompleteGroup(n);
        OrbitCounter counter = new OrbitCounter(n, group);
        int[] counts = counter.count();
        System.out.println(group.order());
        System.out.println(Arrays.toString(counter.getV3()));
        System.out.println(Arrays.toString(counts));
    }
   
    public void testTransversal(int n, Graph graph) {
        
        // Sym(n) : make the total symmetry group
        SSPermutationGroup group = SSPermutationGroup.makeSymN(n);
        
        // Aut(G) : make the automorphism group for this graph
//        SSPermutationGroup subgroup = makeAutByBruteForce(graph);
        SSPermutationGroup subgroup = makeAutByPartitionRefinement(graph);
        
        // the graph invariants
        SortedSet<String> edgeStrings = new TreeSet<String>();
        
        // generate the traversal, and the invariants for each graph permutation
        List<Permutation> traversal = group.transversal(subgroup); 
        for (Permutation p : traversal) {
            String spes = graph.getSortedPermutedEdgeString(p.getValues());
            System.out.println(p + "\t" + p.toCycleString() + "\t" + spes);
            edgeStrings.add(spes);
        }
        
        System.out.println(
                          " |Aut(G)| = " + subgroup.order()
                        + ", |Sym(N)| = " + group.order() 
                        + ", |Aut(G) in Sym(N)| = " + traversal.size()
                        + ", |Sym(N)|/|Aut(G)| = " + group.order() / subgroup.order());
        
        // check that each permutation induces a different edge string
        Assert.assertEquals(traversal.size(), edgeStrings.size());
        System.out.println(edgeStrings.first());
    }
    
    @Test
    public void testCycleGraphTransversal() {
        int n = 6;
        Graph graph = makeCycleGraph(n);
        testTransversal(n, graph);
    }
    
    @Test
    public void testChainGraphTransversal() {
        int n = 5;
        Graph graph = makeChainGraph(n);
        testTransversal(n, graph);
    }
    
    @Test
    public void testCompleteGraphTransversal() {
        int n = 5;
        Graph graph = makeCompleteGraph(n);
        testTransversal(n, graph);
    }
    
}
