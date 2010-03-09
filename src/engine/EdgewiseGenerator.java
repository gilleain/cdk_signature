package engine;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.group.Graph;

/**
 * Extremely simple graph generator that checks for canonical graphs at each 
 * step.
 * 
 * @author maclean
 *
 */
public class EdgewiseGenerator {
    
    public List<Graph> graphs;
    
    public int maxLength;
    
    public int maxDegree;
    
    public EdgewiseGenerator(int maxLength, int maxDegree) {
        this.graphs = new ArrayList<Graph>();
        this.maxLength = maxLength;
        this.maxDegree = maxDegree;
    }
    
    public void generate() {
        Graph initialGraph = new Graph();
        this.generate(initialGraph);
    }
    
    public void generate(Graph g) {
        if (g.getVertexCount() >= this.maxLength) return;
        
        if (g.getVertexCount() < 2 || CanonicalChecker.isCanonical3(g)) {
            System.out.println("CANON\t" + g);
            if (g.getVertexCount() == this.maxLength - 1 && g.isConnected()) {
                this.graphs.add(g);
            }
        } else {
            System.out.println(".....\t" + g);
            return;
        }
        
//        int n = Math.max(2, g.getVertexCount() + 1);
        int n = Math.max(2, g.getVertexCount());
        for (int i = 0; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                if (canAddBond(i, j, g)) {
                    generate(g.makeNew(i, j));
                } else {
                    continue;
                }
            }
        }
    }
    
    public boolean canAddBond(int i, int j, Graph g) {
        return     i != j 
                && !g.isConnected(i, j)
                && !g.saturated(i, maxDegree)
                && !g.saturated(j, maxDegree);
    }
}
