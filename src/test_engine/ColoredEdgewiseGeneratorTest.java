package test_engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import engine.ColoredEdgewiseGenerator;
import engine.Graph;

public class ColoredEdgewiseGeneratorTest {
    
    public void groupByAutomorphism(List<Graph> graphs) {
        Map<String, Map<String, Integer>> autMap = 
            new HashMap<String, Map<String, Integer>>();
        
        for (Graph g : graphs) {
            String edgeString = g.getSortedEdgeString();
            if (autMap.containsKey(edgeString)) {
                String colorString = g.getSortedColorOnlyEdgeString();
                Map<String, Integer> colorMap = autMap.get(edgeString);
                if (colorMap.containsKey(colorString)) {
                    colorMap.put(colorString, colorMap.get(colorString) + 1);
                } else {
                    colorMap.put(colorString, 1);
                }
            } else {
                Map<String, Integer> colorMap = new HashMap<String, Integer>();
                colorMap.put(g.getSortedColorOnlyEdgeString(), 1);
                autMap.put(edgeString, colorMap);
            }
        }
        int i = 0;
        for (String autKey : autMap.keySet()) {
            System.out.println(
                    String.format(
                            "%3d\t%s\t%s", i, autKey, autMap.get(autKey).values()));
            i++;
        }
    }
    
    public void testForUniqueness(List<Graph> graphs) {
        Map<String, Integer> autMap = new HashMap<String, Integer>();
        for (Graph g : graphs) {
            String edgeString = g.getSortedEdgeString();
            if (autMap.containsKey(edgeString)) {
                autMap.put(edgeString, autMap.get(edgeString) + 1);
            } else {
                autMap.put(edgeString, 1);
            }
        }
        int index = 0;
        for (String edgeString : autMap.keySet()) {
            System.out.println(
                    index + " " + autMap.get(edgeString) + "x\t" + edgeString);
            index++;
        } 
    }
    
    public void print(List<Graph> graphs) {
        int i = 0;
        for (Graph graph : graphs) {
            try{
            System.out.println(String.format("%3d\t%s\t%s\t%s",
                    i,
                    graph.getSortedEdgeString(), 
                    graph.getSortedColorOnlyEdgeString(),
                    graph.getColorOnlyEdgeString()));
            }catch(IndexOutOfBoundsException iobe) {
                System.out.println("problem with graph " + graph.toString() + " " + graph.colors);
            }
            i++;
        }
    }
    
    @Test
    public void testSixesWithTwoColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 3);
        colorMap.put(1, 3);
        ColoredEdgewiseGenerator generator = 
            new ColoredEdgewiseGenerator(7, 4, colorMap);
        generator.generate();
        print(generator.graphs);
//        testForUniqueness(generator.graphs);
        groupByAutomorphism(generator.graphs);
    }
    
    @Test
    public void testFivesWithThreeColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 2);
        colorMap.put(1, 1);
        colorMap.put(2, 2);
        ColoredEdgewiseGenerator generator = 
            new ColoredEdgewiseGenerator(6, 4, colorMap);
        generator.generate();
        print(generator.graphs);
    }
    
    @Test
    public void testFivesWithTwoColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 2);
        colorMap.put(1, 3);
        ColoredEdgewiseGenerator generator = 
            new ColoredEdgewiseGenerator(6, 4, colorMap);
        generator.generate();
        print(generator.graphs);
//        testForUniqueness(generator.graphs);
        groupByAutomorphism(generator.graphs);
    }
    
    @Test
    public void testFoursWithTwoColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 2);
        colorMap.put(1, 2);
        ColoredEdgewiseGenerator generator = 
            new ColoredEdgewiseGenerator(5, 4, colorMap);
        generator.generate();
        print(generator.graphs);
//        testForUniqueness(generator.graphs);
        groupByAutomorphism(generator.graphs);
    }
    
    @Test
    public void testThreesWithTwoColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 1);
        colorMap.put(1, 2);
        ColoredEdgewiseGenerator generator = 
            new ColoredEdgewiseGenerator(4, 4, colorMap);
        generator.generate();
//        print(generator.graphs);
        groupByAutomorphism(generator.graphs);
    }

}
