package test_group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;



import cages.PermutationGenerator;
import engine.Graph;
import engine.GraphPermutor;

public class DiscretePartitionRefinerTest {
    
    public Permutation automorphicCheck(Graph graph) {
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        boolean isCanonical = refiner.isCanonical(graph);
        if (isCanonical) {
            System.out.println(graph + " is canonical");
            return new Permutation(graph.getVertexCount());
        } else {
            System.out.println(graph + " not canonical " + refiner.getBest());
            return refiner.getBest();
        }
    }
    
    @Test
    public void testAutGroup() {
        Permutation a = new Permutation(1, 0, 3, 2);
        Permutation b = new Permutation(3, 2, 1, 0);
        SSPermutationGroup generatorGroup = new SSPermutationGroup(4);
        generatorGroup.enter(a);
        generatorGroup.enter(b);
//        TestDiscretePartitionRefiner canonizer = new TestDiscretePartitionRefiner();
        for (Permutation p : generatorGroup.all()) {
            System.out.println(p);
        }
    }
    
    @Test
    public void automorphismWithRespectToColorsByBruteForce() {
        SSPermutationGroup symN = SSPermutationGroup.makeSymN(4);
        Graph g = new Graph("0:1,0:2,1:3,2:3");
        g.setColors(0, 1, 1, 3);
        String original = g.getSortedEdgeString();
        List<Integer> originalColors = g.colors;
        boolean isSimpleCanonical = false;
        boolean isColorCanonical = false;
        int i = 0;
        for (Permutation p : symN.all()) {
            String pestr = g.getSortedPermutedEdgeString(p.getValues());
            if (original.equals(pestr)) {
                isSimpleCanonical = true;
            } else {
                isSimpleCanonical = false;
            }
            List<Integer> permutedColors = g.getPermutedColorString(p);
            if (originalColors.equals(permutedColors)) {
                isColorCanonical = true;        
            } else {
                isColorCanonical = false;
            }
//            System.out.println(i + " " + p + " " + isSimpleCanonical + " " + isColorCanonical);
            if (isSimpleCanonical && isColorCanonical) {
                System.out.println(String.format("%3d %s CANONICAL", i, p));
            } else if (isSimpleCanonical && !isColorCanonical) {
                System.out.println(String.format("%3d %s NEARLY %s", i, p, permutedColors));
            } else {
                System.out.println(String.format("%3d %s", i, p));
            }
            i++;
        }
    }
    
    // TODO : this is a bit lazy - if colors are not in order (from 0), may fail
    public Partition partitionFromColors(Graph graph) {
        Partition partition = new Partition();
        for (int i = 0; i < graph.getVertexCount(); i++) {
            int color = graph.getColor(i);
            if (color < partition.size()) {
                partition.getCell(color).add(i);
            } else {
                partition.addSingletonCell(i);
            }
        }
        return partition;
    }
    
    public void bruteForceColorAutomorphism(Graph graph) {
        System.out.println("testing color automorphisms for " + graph 
                         + " with colors " + graph.colors);
        
        // full aut group from graph without regard to color
        TestDiscretePartitionRefiner colorBlindRefiner = 
            new TestDiscretePartitionRefiner(false);
        SSPermutationGroup autF = colorBlindRefiner.getAutomorphismGroup(graph);
        
        // partial aut group with regard to color
        TestDiscretePartitionRefiner colorfulRefiner = 
            new TestDiscretePartitionRefiner(true);
        Partition initial = partitionFromColors(graph);
        SSPermutationGroup aut = 
            colorfulRefiner.getAutomorphismGroup(graph, initial);

        System.out.println("AutG size " + autF.order() + " Aut size " + aut.order());
        
        // now filter the permutations by those that are color-automorphic
        String original = graph.getSortedColoredEdgeString();
        
        List<Permutation> autFPermutations = 
            filterColorAutPermutations(original, autF, graph);
        List<Permutation> autPermutations = aut.all();
//            filterColorAutPermutations(original, aut, graph);

        System.out.println("Full = " + autFPermutations.size() 
                       + " Color = " + autPermutations.size());
        for (Permutation p : autFPermutations) {
            if (autPermutations.contains(p)) {
                System.out.println(p + " contained in both");
            } else {
                System.out.println(p + " only in autF");
            }
        }
        System.out.println(autFPermutations);
        System.out.println(autPermutations);
        
    }
    
    public List<Permutation> filterColorAutPermutations(
            String original, SSPermutationGroup group, Graph graph) {
        List<Permutation> colorAut = new ArrayList<Permutation>();
        for (Permutation p : group.all()) {
            String permuted = 
                graph.getSortedPermutedColoredEdgeString(p.getValues());
            if (original.equals(permuted)) {
                colorAut.add(p);
            }
        }
        return colorAut;
    }
    
    public void testColoredAutFromInitialPartition(Graph graph) {
        Partition initial = partitionFromColors(graph);
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
//        SSPermutationGroup aut = refiner.getAutomorphismGroup(graph, initial);
        SSPermutationGroup aut = refiner.getAutomorphismGroup(graph);
        String original = graph.getSortedColoredEdgeString();
        System.out.println("original = " + original + " initial " + initial);
        for (Permutation p : aut.all()) {
            String permuted = graph.getSortedPermutedColoredEdgeString(p.getValues());
            System.out.println(permuted + " " + original.equals(permuted) + " " + p);
        }
    }
    
    @Test
    public void testPermutations() {
        Graph graph = new Graph("0:1,0:3,1:2,2:3");
        GraphPermutor permutor = new GraphPermutor(graph);
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        boolean isCanonical = refiner.isCanonical(graph);
        System.out.println("Initial graph " 
                + graph.getSortedEdgeString() + " " 
                + refiner.getHalfMatrixString());
        while (permutor.hasNext()) {
            Graph permutation = permutor.next();
            refiner = new TestDiscretePartitionRefiner();
            isCanonical = refiner.isCanonical(permutation);
            if (isCanonical) {
                System.out.println("CANON " + permutation.getSortedEdgeString());
            } else {
                System.out.println("BEST " + permutation.getSortedEdgeString() 
                        + " = " + refiner.getBest());
            }
        }
    }
    
    @Test
    public void testTwistane() {
        String graphString = "0:1,0:2,0:3,1:4,1:5,2:6,3:7,4:8,5:9,6:8,6:9,7:9";
        Graph coloredA = new Graph(graphString);
        Permutation transform = automorphicCheck(coloredA);
        Graph permuted = coloredA.getPermutedGraph(transform.getValues());
        System.out.println(permuted.getSortedEdgeString());
    }
    
    @Test
    public void testColoredCubane() {
        String graphString = "0:1,0:2,0:3,1:4,1:5,2:4,2:6,3:5,3:6,4:7,5:7,6:7";
        Graph coloredA = new Graph(graphString);
        coloredA.setColors(0, 1, 1, 1, 0, 0, 0, 1);
        bruteForceColorAutomorphism(coloredA);
        Graph coloredB = new Graph(graphString);
        coloredB.setColors(0, 1, 1, 1, 2, 2, 2, 0);
        bruteForceColorAutomorphism(coloredB);
    }
    
    @Test
    public void testColoredOctagon() {
        String graphString = "0:1,0:2,1:3,2:4,3:5,4:6,5:7,6:7";
        Graph coloredA = new Graph(graphString);
        coloredA.setColors(0, 1, 1, 0, 0, 1, 1, 0);
//        testColoredAutFromInitialPartition(coloredA);
        bruteForceColorAutomorphism(coloredA);
        Graph coloredB = new Graph(graphString);
        coloredB.setColors(0, 0, 0, 0, 1, 1, 1, 1);
//        testColoredAutFromInitialPartition(coloredB);
        bruteForceColorAutomorphism(coloredB);
    }
    
    @Test
    public void testColoredHexagon() {
//        String graphString = "0:1,0:2,1:3,2:4,3:5,4:5";
        String graphString = "0:1,0:5,1:2,2:3,3:4,4:5";
        Graph coloredA = new Graph(graphString);
        automorphicCheck(coloredA);
        coloredA.setColors(0, 1, 1, 0, 0, 1);
//        testColoredAutFromInitialPartition(coloredA);
//        bruteForceColorAutomorphism(coloredA);
        Graph coloredB = new Graph(graphString);
        coloredB.setColors(0, 0, 0, 1, 1, 1);
//        testColoredAutFromInitialPartition(coloredB);
//        bruteForceColorAutomorphism(coloredB);
    }
    
    @Test
    public void testColoredPentagon() {
//        String graphString = "0:1,0:2,1:3,2:4,3:4";
        String graphString = "0:1,0:4,1:2,2:3,3:4";
        Graph coloredA = new Graph(graphString);
        automorphicCheck(coloredA);
        coloredA.setColors(0, 1, 1, 1, 0);
//        testColoredAutFromInitialPartition(coloredA);
//        bruteForceColorAutomorphism(coloredA);
        Graph coloredB = new Graph(graphString);
        coloredB.setColors(0, 1, 0, 1, 0);
//        testColoredAutFromInitialPartition(coloredB);
//        bruteForceColorAutomorphism(coloredB);
    }
    
    @Test
    public void testColoredSquare() {
//        String graphString = "0:1,0:2,1:3,2:3";
        String graphString = "0:1,0:3,1:2,2:3";
        Graph coloredA = new Graph(graphString);
        automorphicCheck(coloredA);
        coloredA.setColors(0, 1, 0, 1);
//        testColoredAutFromInitialPartition(coloredA);
//        bruteForceColorAutomorphism(coloredA);
        Graph coloredB = new Graph(graphString);
        coloredB.setColors(0, 1, 1, 0);
//        testColoredAutFromInitialPartition(coloredB);
//        bruteForceColorAutomorphism(coloredB);
    }
    
    @Test
    public void testColoredGraphs() {
        String graphString = "0:1,0:2,1:3,2:3";
//        String graphString = "0:1,0:3,1:2,2:3";
        Graph blanked = new Graph(graphString);
        Graph colored = new Graph(graphString);
//        colored.setColors(0, 1, 1, 0);
        colored.setColors(1, 0, 1, 0);
        TestDiscretePartitionRefiner canonizer = new TestDiscretePartitionRefiner();
        SSPermutationGroup groupBlank = canonizer.getAutomorphismGroup(blanked);
        TestDiscretePartitionRefiner canonizerB = new TestDiscretePartitionRefiner();
        Partition partition = new Partition();
//        partition.addCell(0, 3);
//        partition.addCell(1, 2);
        partition.addCell(0, 2);
        partition.addCell(1, 3);
        SSPermutationGroup groupColor = canonizerB.getAutomorphismGroup(colored, partition);
//        SSPermutationGroup groupColor = canonizerB.getAutomorphismGroup(colored);
        System.out.println("blank");
        int i = 0;
        for (Permutation p : groupBlank.all()) {
            System.out.println(i + " " + p);
            i++;
        }
        System.out.println("colored");
        int j = 0;
        for (Permutation p : groupColor.all()) {
            System.out.println(j + " " + p);
            j++;
        }
        SSPermutationGroup symN = SSPermutationGroup.makeSymN(blanked.getVertexCount());
        int k = 0;
        HashMap<String, Integer> counts = new HashMap<String, Integer>(); 
        for (Permutation p : symN.transversal(groupColor)) {
            String spces = colored.getSortedPermutedColoredEdgeString(p.getValues());
            System.out.println(k + " " + spces);
            if (counts.containsKey(spces)) {
                counts.put(spces, counts.get(spces) + 1);
            } else {
                counts.put(spces, 1);
            }
            k++;
        }
        for (String spces : counts.keySet()) {
            System.out.println(spces + " " + counts.get(spces));
        }
    }
    
    @Test
    public void testGenerateAut() {
//        Graph g = new Graph("0:1,0:3,0:7,1:2,1:4,2:3,2:6,3:4,4:5,5:6,5:7,6:7");

        Graph g = new Graph("0:1,0:2,1:3,2:3");
//        Graph g = new Graph("0:4,1:3,2:3,2:4");
        
        TestDiscretePartitionRefiner canonizer = new TestDiscretePartitionRefiner();
        boolean yes = canonizer.isCanonical(g);
        System.out.println(yes);
        
        SSPermutationGroup group = canonizer.getGroup();
        System.out.println(group.order());
        
        Permutation best = canonizer.getBest();
        System.out.println("CERTIFICATE (" + best + ") : "+ canonizer.getCertificate());
        
        Permutation first = canonizer.getFirst();
        System.out.println("CERTIFICATE (" +first + ") : "+canonizer.calculateCertificate(first));
        
        String spesFirst = g.getSortedPermutedEdgeString(first.getValues());
        String spesBest = g.getSortedPermutedEdgeString(best.getValues());
        
        System.out.println("SPES (" + first + ") : "+ spesFirst);
        System.out.println("SPES (" + best + ") : "+ spesBest);
        
        int i = 0;
        for (Permutation p : group.all()) {
            System.out.println(i + " " + p);
            i++;
        }
    }
    
    @Test
    public void testMinimalMatrixCanonicalChain() {
        List<Graph> graphs = new ArrayList<Graph>();
        graphs.add(new Graph("0:1"));
        graphs.add(new Graph("0:2,1:2"));
        graphs.add(new Graph("0:3,1:3,2:3"));
        graphs.add(new Graph("0:3,1:2,2:3"));
        graphs.add(new Graph("0:4,1:2,2:3,3:4"));
        
        TestDiscretePartitionRefiner canonizer = new TestDiscretePartitionRefiner();
        for (Graph g : graphs) {
            System.out.println(g + " " + canonizer.isCanonical(g));
        }
    }
    
    @Test
    public void testMaximalMatrixCanonicalChain() {
        List<Graph> graphs = new ArrayList<Graph>();
        graphs.add(new Graph("0:1"));
        graphs.add(new Graph("0:1,0:2"));
        graphs.add(new Graph("0:1,0:2,1:3"));
        graphs.add(new Graph("0:1,0:2,1:3,2:4"));
        
        TestDiscretePartitionRefiner canonizer = new TestDiscretePartitionRefiner();
        for (Graph g : graphs) {
            System.out.println(g + " " + canonizer.isCanonical(g));
        }
    }

    
    @Test
    public void testIsCanonical() {
//        Graph g = new Graph("0:1,0:3,1:2,2:3");
        Graph g = new Graph("0:1,0:2");
        int n = g.getVertexCount();
        TestDiscretePartitionRefiner canonizer = new TestDiscretePartitionRefiner();
        PermutationGenerator generator = new PermutationGenerator(n);
        Map<String, Integer> cosetMap = new HashMap<String, Integer>();
        int cosetIndex = 0;
        while (generator.hasNext()) {
            Permutation permutation = generator.next();
            Graph pg = g.getPermutedGraph(permutation.getValues());
            boolean result = canonizer.isCanonical(pg);
            String ses = pg.getSortedEdgeString();
            if (!cosetMap.containsKey(ses)) {
                cosetIndex++;
                cosetMap.put(ses, cosetIndex);
            }
            Permutation first = canonizer.getFirst();
            System.out.println(pg.toString() + "\t" + result + "\t" 
                    + permutation 
                    + "\t" + first.isIdentity()
                    + "\t" + ses + "\t" + cosetMap.get(ses)
                    + "\t" + canonizer.getHalfMatrixString()
                    + "\t" + first);
        }
    }
    
    @Test
    public void testSubtree1() {
        Graph g = new Graph("0:1,0:3,0:7,1:2,1:4,2:3,2:6,3:4,4:5,5:6,5:7,6:7");
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        Partition initial = new Partition();
        initial.addCell(0);
        initial.addCell(2, 4);
        initial.addCell(5, 6);
        initial.addCell(7);
        initial.addCell(1, 3);
        
        boolean yes = refiner.isCanonical(g, initial);
        System.out.println("CERTIFICATE (" 
                + refiner.getBest() + ") : "
                + refiner.getCertificate()
                + " " + yes);
    }
    
    @Test
    public void testSubtree2() {
        Graph g = new Graph("0:1,0:3,0:7,1:2,1:4,2:3,2:6,3:4,4:5,5:6,5:7,6:7");
        TestDiscretePartitionRefiner refiner = new TestDiscretePartitionRefiner();
        Partition initial = new Partition();
        initial.addCell(1);
        initial.addCell(3);
        initial.addCell(5, 6, 7);
        initial.addCell(0, 2, 4);
        
        boolean yes = refiner.isCanonical(g, initial);
        System.out.println("CERTIFICATE (" +refiner.getBest() 
                + ") : "+ refiner.getCertificate()
                + " " + yes);
    }

}
