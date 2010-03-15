package test_deterministic;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.openscience.cdk.deterministic.DeterministicEnumerator2;

public class DeterministicEnumerator2Test {
    
    public void run(Map<String, Integer> counts) {
        DeterministicEnumerator2 detEn = new DeterministicEnumerator2();
        detEn.generate(counts);
    }
    
    @Test
    public void testC2H4() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 2);
       counts.put("H", 4);
       run(counts);
    }
    
    @Test
    public void testC2H6() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 2);
       counts.put("H", 6);
       run(counts);
    }
    
    @Test
    public void testC3H6() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 3);
       counts.put("H", 6);
       run(counts);
    }
    
    @Test
    public void testC3H8() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 3);
       counts.put("H", 8);
       run(counts);
    }
    
    @Test
    public void testC4H8() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 4);
       counts.put("H", 8);
       run(counts);
    }
    
    @Test
    public void testC4H10() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 4);
       counts.put("H", 10);
       run(counts);
    }
    
    @Test
    public void testC5H10() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 5);
       counts.put("H", 10);
       run(counts);
    }
    
    @Test
    public void testC5H12() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 5);
       counts.put("H", 12);
       run(counts);
    }
    
    @Test
    public void testC6H14() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 6);
       counts.put("H", 14);
       run(counts);
    }
    
    @Test
    public void testC8H18() {
       Map<String, Integer> counts = new HashMap<String, Integer>();
       counts.put("C", 8);
       counts.put("H", 18);
       run(counts);
    }
    
    public static void main(String[] args) {
        new DeterministicEnumerator2Test().testC3H6();
    }

}
