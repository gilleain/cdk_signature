package test_engine;

import org.junit.Test;

import engine.Graph;
import engine.PartitioningGenerator;

public class PartitioningGeneratorTest {
    
    @Test
    public void basicTest() {
        int valence = 10;
        int numberOfAtoms = 4;
        int maxValence = 4;
        
        for (Graph g : PartitioningGenerator.generate(
                valence, numberOfAtoms, maxValence)) {
            System.out.println(g);
        }
    }

}
