package test_cages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.junit.Test;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;

public class ComparisonTest {
    
    @Test
    public void threeThreeTest() {
        Partition partition = new Partition();
        partition.addCell(0, 1, 2);
        partition.addCell(3, 4, 5);
        colorEquivalenceClasses(partition, true);
    }
    
    @Test
    public void twoThreeFourTest() {
        Partition partition = new Partition();
        partition.addCell(0, 1);
        partition.addCell(2, 3, 4);
        partition.addCell(5, 6, 7, 8);
        colorEquivalenceClasses(partition, false);
    }
    
    @Test
    public void sixThreeTest() {
        Partition partition = new Partition();
        partition.addCell(0, 1, 2);
        partition.addCell(3, 4, 5, 6, 7, 8);
        colorEquivalenceClasses(partition, false);
    }
    
    public void colorEquivalenceClasses(Partition partition, boolean print) {
        int n = partition.numberOfElements();
        List<Permutation> generators = new ArrayList<Permutation>();
        for (int cellIndex = 0; cellIndex < partition.size(); cellIndex++) {
            SortedSet<Integer> cell = partition.getCell(cellIndex);
            Permutation generatorA = cellToPermutationA(cell, n);
            System.out.println(cellIndex + " A = " + generatorA);
            generators.add(generatorA);
            if (cell.size() > 2) {
                Permutation generatorB = cellToPermutationB(cell, n);
                System.out.println(cellIndex + " B = " + generatorB);
                generators.add(generatorB);
            }
        }
        SSPermutationGroup group = new SSPermutationGroup(n, generators);
        List<Permutation> all = group.all();
        System.out.println(all.size());
        if (print) {
            for (Permutation p : all) {
                System.out.println(p);
            }
        }
    }
    
    private Permutation cellToPermutationA(SortedSet<Integer> cell, int n) {
        if (cell.size() < 2) {
            return new Permutation(n);
        } else {
            int[] p = new int[n];
            for (int i = 0; i < n; i++) { p[i] = i; }
            Iterator<Integer> itr = cell.iterator(); 
            int first = itr.next();
            int second = itr.next();
            p[second] = first;
            p[first] = second;
            return new Permutation(p);
        }
    }

    private Permutation cellToPermutationB(SortedSet<Integer> cell, int n) {
        if (cell.size() < 2) {
            return new Permutation(n);
        } else {
            int[] p = new int[n];
            for (int i = 0; i < n; i++) { p[i] = i; }
            int[] c = new int[cell.size()];
            int i = 0;
            for (int item : cell) {
                c[i] = item;
                i++;
            }
            p[c[0]] = c[c.length - 1];
            for (int j = 1; j < c.length; j++) {
                p[c[j]] = c[j - 1];
            }
            return new Permutation(p);
        }
    }

}
