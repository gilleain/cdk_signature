package test_engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openscience.cdk.group.Graph;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;

import engine.MultiEdgeColoredGenerator;

public class MultiEdgeColoredGeneratorTest {
    
    public void printSmiles(MultiEdgeColoredGenerator generator) {
        Map<String, Integer> freqs = new HashMap<String, Integer>();
        SmilesGenerator smilesGenerator = new SmilesGenerator();
        for (IMolecule container : toMolecules(generator)) {
            try {
                String sm = smilesGenerator.createSMILES((IMolecule) container);
                if (freqs.containsKey(sm)) {
                    freqs.put(sm, freqs.get(sm) + 1);
                } else {
                    freqs.put(sm, 1);
                }
            } catch (Exception e){
                System.out.println("error" + e);
            }
        }
        for (String key : freqs.keySet()) {
            System.out.println(String.format("%3d\t%s", freqs.get(key), key));
        }
    }
    
    public List<IMolecule> toMolecules(MultiEdgeColoredGenerator generator) {
        IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
        List<IMolecule> molecules = new ArrayList<IMolecule>();
        for (Graph graph : generator.graphs) {
            IMolecule molecule = builder.newMolecule();
            for (int color : graph.colors) {
                String symbol = generator.colorStringMap.get(color);
                molecule.addAtom(builder.newAtom(symbol));
            }
            for (Graph.Edge edge : graph.edges) {
                molecule.addBond(edge.a, edge.b, getOrder(edge));
            }
            molecules.add(molecule);
        }
        
        return molecules;
    }
    
    public IBond.Order getOrder(Graph.Edge edge) {
        switch (edge.o) {
            case 1: return IBond.Order.SINGLE;
            case 2: return IBond.Order.DOUBLE;
            case 3: return IBond.Order.TRIPLE;
            default: return IBond.Order.SINGLE;
        }
    }
    
    public void groupByAutomorphism(List<Graph> graphs) {
        Map<String, Map<String, Integer>> autMap = 
            new HashMap<String, Map<String, Integer>>();
        
        for (Graph g : graphs) {
            String edgeString = g.getSortedEdgeStringWithEdgeOrder();
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
//                            "%3d\t%s\t%s", i, autKey, autMap.get(autKey).values()));
                            "%3d\t%s\t%s", i, autKey, autMap.get(autKey)));
            i++;
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
    
    public Map<Integer, String> standardColorStringMap() {
        Map<Integer, String> colorStringMap = new HashMap<Integer, String>();
        colorStringMap.put(0, "C");
        colorStringMap.put(1, "N");
        colorStringMap.put(2, "O");
        colorStringMap.put(3, "H");
        return colorStringMap;
    }
    
    public Map<Integer, Integer> standardDegreeMap() {
        Map<Integer, Integer> colorDegreeMap = new HashMap<Integer, Integer>();
        colorDegreeMap.put(0, 4);
        colorDegreeMap.put(1, 3);
        colorDegreeMap.put(2, 2);
        colorDegreeMap.put(3, 1);
        return colorDegreeMap;
    }
    
    public MultiEdgeColoredGenerator standard(Map<Integer, Integer> colorMap) {
        Map<Integer, String> standardStrings = standardColorStringMap();
        Map<Integer, Integer> standardDegrees = standardDegreeMap();
        
        return new MultiEdgeColoredGenerator(
                colorMap, standardStrings, standardDegrees);
    }
    
    @Test
    public void testSixesWithStandardDegreeAndTwoColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 3);
        colorMap.put(1, 3);
        
        MultiEdgeColoredGenerator generator = standard(colorMap);
        generator.generate(6);
//        print(generator.graphs);
        groupByAutomorphism(generator.graphs);
    }
    
    @Test
    public void testSevensWithStandardDegreeAndCHO() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 2);
        colorMap.put(2, 1);
        colorMap.put(3, 4);
        
        MultiEdgeColoredGenerator generator = standard(colorMap);
            
        generator.generate(5);
//        print(generator.graphs);
//        groupByAutomorphism(generator.graphs);
        printSmiles(generator);
    }
    
    @Test
    public void testFivesWithStandardDegreeAndTwoColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 3);
        colorMap.put(1, 2);
        
        MultiEdgeColoredGenerator generator = standard(colorMap);
            
        generator.generate(5);
//        print(generator.graphs);
//        groupByAutomorphism(generator.graphs);
        printSmiles(generator);
    }

    @Test
    public void testFoursWithStandardDegreeAndTwoColors() {
        Map<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
        colorMap.put(0, 2);
        colorMap.put(1, 2);
        
        MultiEdgeColoredGenerator generator = standard(colorMap);
            
        generator.generate(4);
//        print(generator.graphs);
//        groupByAutomorphism(generator.graphs);
        printSmiles(generator);
    }

}
