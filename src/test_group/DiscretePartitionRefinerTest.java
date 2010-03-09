package test_group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openscience.cdk.group.Graph;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;



import cages.PermutationGenerator;

public class DiscretePartitionRefinerTest {
    
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
    
    @Test
    public void testColoredGraphs() {
//        String graphString = "0:1,0:2,1:3,2:3";
        String graphString = "0:1,0:3,1:2,2:3";
        Graph blanked = new Graph(graphString);
        Graph colored = new Graph(graphString);
        colored.setColors(0, 1, 1, 0);
        TestDiscretePartitionRefiner canonizer = new TestDiscretePartitionRefiner();
        SSPermutationGroup groupBlank = canonizer.getAutomorphismGroup(blanked);
        TestDiscretePartitionRefiner canonizerB = new TestDiscretePartitionRefiner();
        Partition partition = new Partition();
        partition.addCell(0, 3);
        partition.addCell(1, 2);
        SSPermutationGroup groupColor = canonizerB.getAutomorphismGroup(colored, partition);
        System.out.println("blank");
        int i = 0;
        for (Permutation p : groupBlank.all()) {
            System.out.println(i + " " + p);
            i++;
        }
        System.out.println("colored");
        int k = 0;
        for (Permutation p : groupColor.all()) {
            System.out.println(k + " " + p);
            k++;
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
