package test_cages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;

import cages.RepresentativeLister;

public class RepresentativeListerTest {
    
    @Test
    public void testMinRep() {
        int n = 8;
        List<Permutation> generators = new ArrayList<Permutation>();
        generators.add(new Permutation(new int[] {1, 3, 5, 7, 0, 2, 4, 6}));
        generators.add(new Permutation(new int[] {1, 3, 0, 2, 5, 7, 4, 6}));
        
        SSPermutationGroup group = 
            new SSPermutationGroup(n, generators);
        SortedSet<Integer> A = new TreeSet<Integer>();
        Collections.addAll(A, 1, 2, 7);
//        SortedSet reps = group.minRep(A);
//        System.out.println(reps);
        List<SortedSet<Integer>> R = new ArrayList<SortedSet<Integer>>();
        R.add(A);
        RepresentativeLister lister = new RepresentativeLister(n, group);
        List<SortedSet<Integer>> S = lister.orbReps(R);
        System.out.println(S);
    }

}
