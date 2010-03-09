package engine;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.group.Graph;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;


public class TraversalBacktracker implements SSPermutationGroup.Backtracker {
    
    private Graph graph;
    
    private SSPermutationGroup aut;
    
    private List<Permutation> results;
    
    private boolean finished;
    
    private int maxSize;
    
    private int size;
    
    private String original;
    
    public TraversalBacktracker(
            String original, 
            SSPermutationGroup symN, Graph g, SSPermutationGroup aut) {
        this.graph = g;
        this.aut = aut;
        this.results = new ArrayList<Permutation>();
        this.maxSize = symN.order() / aut.order();
        this.size = g.getVertexCount();
        this.original = original;
    }
    
    public void applyTo(Permutation p) {
        String permuted = graph.getSortedPermutedEdgeString(p.getValues());
        
        if (original.compareTo(permuted) > 0) {
            finished = true;
            return;
        }
        
        for (Permutation f : results) {
            Permutation h = f.invert().multiply(p);
            if (aut.test(h) == size) {
                return;
            }
        }
        results.add(p);
        if (results.size() >= this.maxSize) {
            this.finished = true;
        }
    }

    public boolean finished() {
        return finished;
    }
    
    public boolean checkedAll() {
        return this.results.size() == this.maxSize;
    }
    
}
