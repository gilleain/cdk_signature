package test_group;

import org.junit.Test;
import org.openscience.cdk.group.Graph;
import org.openscience.cdk.group.Partition;




public class EquitablePartitionRefinerTest {
    
    @Test
    public void testRefiningToEquitable() {
        Graph g = new Graph("0:1,0:3,0:7,1:2,1:4,2:3,2:6,3:4,4:5,5:6,5:7,6:7");
        TestEquitablePartitionRefiner refiner = new TestEquitablePartitionRefiner(g);
        Partition coarse = new Partition();
        coarse.addCell(0);
        coarse.addCell(1, 2, 3, 4, 5, 6, 7);
        Partition finer = refiner.refine(coarse);
        System.out.println(finer);
    }
    
    @Test
    public void testRefiningToEquitable2() {
        Graph g = new Graph("0:1,0:3,0:7,1:2,1:4,2:3,2:6,3:4,4:5,5:6,5:7,6:7");
        TestEquitablePartitionRefiner refiner = new TestEquitablePartitionRefiner(g);
        Partition coarse = new Partition();
        coarse.addCell(0);
        coarse.addCell(2, 4);
        coarse.addCell(5, 6);
        coarse.addCell(7);
        coarse.addCell(1, 3);
        // split block 1 with [2, 4] into [[2], [4]]
        Partition finer1 = coarse.splitBefore(1, 2);
        Partition finest1 = refiner.refine(finer1);
        System.out.println(finer1 + " -> " + finest1);
        
        // split block 1 with [2, 4] into [[4], [2]]
        Partition finer2 = coarse.splitBefore(1, 4);
        Partition finest2 = refiner.refine(finer2);
        System.out.println(finer2 + " -> " + finest2);
    }
    
    @Test
    public void testColors() {
        Graph g = new Graph("0:1,0:2,1:3,2:3");
        g.setColors(0, 1, 1, 0);
        
        TestEquitablePartitionRefiner refiner = new TestEquitablePartitionRefiner(g);
        Partition coarse = new Partition();
        coarse.addCell(0);
        coarse.addCell(1, 2, 3);
        Partition finer = refiner.refine(coarse);
        System.out.println(coarse + " -> " + finer);
    }
    

}
