package test_group;

import java.util.Arrays;

import org.junit.Test;
import org.openscience.cdk.group.Permutation;


public class PermutationTest {
    
    @Test
    public void testGetType() {
        Permutation p = new Permutation(new int[] {1, 2, 3, 4, 0});
        System.out.println(Arrays.toString(p.getType()));
    }

}
