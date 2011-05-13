package test_engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import engine.EdgewiseGenerator;
import engine.Graph;

public class EdgewiseGeneratorTest {
    
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
    
    public void toFile(List<Graph> graphs, File file) {
        try {
            PrintWriter writer = new PrintWriter(file);
            for (Graph graph : graphs) {
                writer.println(graph.toString());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void eightRunUnique() {
        EdgewiseGenerator gen = new EdgewiseGenerator(9, 4);
        gen.generate();
        testForUniqueness(gen.graphs);
    }
    
    @Test
    public void fiveRunUnique() {
        EdgewiseGenerator gen = new EdgewiseGenerator(6, 4);
        gen.generate();
        testForUniqueness(gen.graphs);
    }
    
    @Test
    public void sevenRunUnique() {
        EdgewiseGenerator gen = new EdgewiseGenerator(8, 4);
        gen.generate();
        testForUniqueness(gen.graphs);
    }
    
    @Test
    public void sixRunUnique() {
        EdgewiseGenerator gen = new EdgewiseGenerator(7, 3);
        gen.generate();
        testForUniqueness(gen.graphs);
    }
    
    @Test
    public void fourRunUnique() {
        EdgewiseGenerator gen = new EdgewiseGenerator(5, 4);
        gen.generate();
        testForUniqueness(gen.graphs);
    }
    
    @Test
    public void fiveRunToFile() {
        EdgewiseGenerator gen = new EdgewiseGenerator(6, 4);
        gen.generate();
        toFile(gen.graphs, new File("sixes_nocolor.txt"));
    }
    
    @Test
    public void sixRunToFile() {
        EdgewiseGenerator gen = new EdgewiseGenerator(7, 4);
        gen.generate();
        toFile(gen.graphs, new File("sevens_nocolor.txt"));
    }

    
    @Test
    public void testGraphRoundtrip() {
        Graph graph = new Graph("0:1,2:3");
        String gstring = graph.toString();
        Graph newGraph = new Graph(gstring);
        Assert.assertEquals(
                graph.getSortedEdgeString(), newGraph.getSortedEdgeString());
    }

}
