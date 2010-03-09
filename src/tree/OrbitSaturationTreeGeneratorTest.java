package tree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class OrbitSaturationTreeGeneratorTest {
    
    public OrbitSaturationTreeGenerator makeTreeGenerator() {
        Map<String, Integer> degreeMap = new HashMap<String, Integer>();
        degreeMap.put("C", 4);
        degreeMap.put("H", 1);
        degreeMap.put("O", 2);
        
        return new OrbitSaturationTreeGenerator(degreeMap);
    }
    
    public void printSet(List<LabelledTree> trees) {
        Set<String> stringSet = new HashSet<String>();
        for (LabelledTree tree : trees) {
            System.out.println("adding " + tree + " canon " + tree.toCanonicalString());
            stringSet.add(tree.toCanonicalString());
        }
        for (String string : stringSet) {
            System.out.println(string);
        }
    }
    
    @Test
    public void c3H8O() {
        OrbitSaturationTreeGenerator generator = makeTreeGenerator();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("C", 3);
        counts.put("H", 8);
        counts.put("O", 1);
        List<LabelledTree> trees = generator.generate(counts);
        printSet(trees);
    }
    
    @Test
    public void c3H8() {
        OrbitSaturationTreeGenerator generator = makeTreeGenerator();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("C", 3);
        counts.put("H", 8);
        List<LabelledTree> trees = generator.generate(counts);
        printSet(trees);
    }
    
    @Test
    public void c4H10() {
        OrbitSaturationTreeGenerator generator = makeTreeGenerator();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("C", 4);
        counts.put("H", 10);
        List<LabelledTree> trees = generator.generate(counts);
        printSet(trees);
    }
    
    @Test
    public void c5H12() {
        OrbitSaturationTreeGenerator generator = makeTreeGenerator();
        Map<String, Integer> counts = new HashMap<String, Integer>();
        counts.put("C", 5);
        counts.put("H", 12);
        List<LabelledTree> trees = generator.generate(counts);
        printSet(trees);
    }

}
