package cages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.group.Permutation;

import engine.Graph;



/**
 * Not a generator of trivial automorphisms, but a trivial implementation of an
 * automorphism generator (for graphs). It does this by running through every
 * permutation of n, for a graph with n vertices. Each one is checked to see if
 * it is an automorphism.
 * 
 * Obviously this is horribly inefficient, since there are n! such permutations.
 * 
 * 
 * @author maclean
 * 
 */
public class TrivialAutomorphismGenerator {

    public static List<Permutation> generate(Graph graph) {
        int n = graph.getVertexCount();
//        List<Permutation> aut = new ArrayList<Permutation>();
        Map<String, Permutation> autMap = new HashMap<String, Permutation>();
        PermutationGenerator generator = new PermutationGenerator(n);
        Permutation identity = new Permutation(n);
        
        String initialInvariant = 
            TrivialAutomorphismGenerator.invariant(graph, identity);
        while (generator.hasNext()) {
            Permutation permutation = generator.next();
            String invariant = 
                TrivialAutomorphismGenerator.invariant(graph, permutation);
            String unsortedInvariant = 
                graph.getPermutedEdgeString(permutation.getValues());
            if (initialInvariant.equals(invariant)) {
//                aut.add(permutation);
//                if (!autMap.containsKey(invariant)) {
                if (!autMap.containsKey(unsortedInvariant)) {
//                    autMap.put(invariant, permutation);
                    autMap.put(unsortedInvariant, permutation);
                    System.out.println("putting " + unsortedInvariant 
                                    + " and " + permutation 
                                    + " aka " + permutation.toCycleString());
                }
            }
        }
//        return aut;
        List<Permutation> aut = new ArrayList<Permutation>();
        aut.addAll(autMap.values());
        return aut;
    }
    
    private static String invariant(Graph graph, Permutation permutation) {
        return graph.getSortedPermutedEdgeString(permutation.getValues());
    }
}
