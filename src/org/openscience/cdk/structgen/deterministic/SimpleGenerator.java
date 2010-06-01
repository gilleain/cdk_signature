package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class SimpleGenerator {
    
    /**
     * Convenience instance of a builder
     */
    private IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();    
    
    public List<FragmentGraph> graphs;
    
    public SimpleGenerator() {
        graphs = new ArrayList<FragmentGraph>();
    }
    
    public void generate(Map<String, Integer> fragmentCounts) {
        List<String> labels = new ArrayList<String>(fragmentCounts.keySet());
        Collections.sort(labels);
        String initialLabel = labels.get(0);
        FragmentGraph initialGraph = 
            new FragmentGraph(fragmentCounts, builder, initialLabel);
        generate(initialGraph);
        
//        for (String label : fragmentCounts.keySet()) {
//            FragmentGraph initialGraph = 
//                new FragmentGraph(fragmentCounts, builder, label);
//            generate(initialGraph);
//        }
    }
    
    public void generate(FragmentGraph graph) {
        if (!isCanonical(graph)) return;
        
        if (graph.isFullySaturated() && graph.allUsed()) {
            if (isCanonical(graph)) {
                graphs.add(graph);
            }
        } else {
            int n = Math.max(1, graph.getVertexCount());
            for (int i = 0; i < n; i++) {
                
                // cycles and multiple bonds
                for (int j = i; j < n; j++) {
                    if (canAddBond(i, j, graph)) {
                        FragmentGraph child = new FragmentGraph(graph);
                        child.bond(i, j, builder);
                        System.out.println("cycle " + i + " " + j + " " + child);
                        generate(child);
                    }
                }
                
                // new bonds
//                for (String l : graph.unusedLabels()) {
//                    if (canAddBond(i, l, graph)) {
//                        FragmentGraph child = new FragmentGraph(graph);
//                        child.bond(l, i, builder);
//                        System.out.println("bond " + i + " " + l + " " + child);
//                        generate(child);
//                    }
//                }
                String l = graph.getFirstUnusedLabel();
                if (l != null && canAddBond(i, l, graph)) {
                      FragmentGraph child = new FragmentGraph(graph);
                      child.bond(l, i, builder);
                      System.out.println("bond " + i + " " + l + " " + child);
                      generate(child);
                }
            }
        }
    }
    
    private boolean canAddBond(int i, int j, FragmentGraph g) {
        return i != j
            && !g.isSaturated(i)
            && !g.isSaturated(j)
            && g.canIncreaseBondOrder(i, j);
    }
    
    private boolean canAddBond(int i, String label, FragmentGraph g) {
        return !g.isSaturated(i);
    }
    
    private boolean isCanonical(FragmentGraph graph) {
        return 
//        CanonicalChecker.edgesInOrder(graph.getAtomContainer()) && 
//        CanonicalChecker.isCanonicalWithColorPartition(graph.getAtomContainer());
        CanonicalChecker.isCanonicalWithSignaturePartition(graph.getAtomContainer());
//        CanonicalChecker.isCanonical(graph.getAtomContainer());
//        AlternateCanonicalChecker.isCanonicalByVisitor(
//                graph.getAtomContainer());
    }

}
