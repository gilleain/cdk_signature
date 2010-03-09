package tree;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class TreeGeneratorTest {
    
    public TreeGenerator makeCHTreeGenerator() {
        Map<String, Integer> degreeMap = new HashMap<String, Integer>();
        degreeMap.put("C", 4);
        degreeMap.put("H", 1);
        
        return new TreeGenerator(degreeMap);
    }
    
    public void toString(TreeGenerator generator, Map<String, Integer> counts) {
        int i = 0;
        for (LabelledTree tree : generator.generate(counts)) {
            System.out.println(
                    String.format(
                            "%3d\t%s\t%s", i, tree, tree.toCanonicalString()));
            Assert.assertEquals(tree.toString(), tree.toCanonicalString());
            i++;
        }
    }
    
    @Test
    public void testC2H6() {
        TreeGenerator generator = makeCHTreeGenerator();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("C", 2);
        counts.put("H", 6);
        toString(generator, counts);
    }
    
    @Test
    public void testC3H8() {
        TreeGenerator generator = makeCHTreeGenerator();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("C", 3);
        counts.put("H", 8);
        toString(generator, counts);
    }
    
    @Test
    public void testC4H10() {
        TreeGenerator generator = makeCHTreeGenerator();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("C", 4);
        counts.put("H", 10);
        toString(generator, counts);
    }

}
