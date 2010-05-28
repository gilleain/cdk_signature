package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class DeterministicEnumerator2 {
    
    /**
     * Convenience instance of a builder
     */
    private IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public List<FragmentGraph> generate(Map<String, Integer> fragmentCounts) {
        List<FragmentGraph> solutions = new ArrayList<FragmentGraph>();
        for (String label : fragmentCounts.keySet()) {
            FragmentGraph initialGraph = 
                new FragmentGraph(fragmentCounts, builder, label);
            enumerate(initialGraph, solutions);
        }
        return solutions;
    }
    
    private void enumerate(FragmentGraph graph, List<FragmentGraph> solutions) {
//        System.out.println("enumerating " + graph);
        if (graph.isFullySaturated() && graph.allUsed()) {
//            if (CanonicalChecker.isCanonical(graph)) {
            if (CanonicalChecker.edgesInOrder(graph.getAtomContainer())) {
                solutions.add(graph);
            }
//            }
        } else {
//            String label = graph.getNextUnsaturatedLabel();
//            if (label == null) return;
            for (String label : graph.getUniqueLabels()) {
                for (FragmentGraph child : saturateLabel(label, graph)) {
                    enumerate(child, solutions);
                }
            }
        }
    }
    
    private List<FragmentGraph> saturateLabel(String label, FragmentGraph graph) {
        List<FragmentGraph> labelSolutions = new ArrayList<FragmentGraph>();
        saturateLabel(label, graph, labelSolutions);
        return labelSolutions;
    }
    
    private void saturateLabel(
            String label, FragmentGraph graph, List<FragmentGraph> graphs) {
        int atomIndex = graph.getAtomIndexForLabel(label);
        if (atomIndex == -1) {    // no atoms with this label?
            graphs.add(graph);
        } else {
            for (FragmentGraph child : saturateAtom(atomIndex, graph)) {
                saturateLabel(label, child, graphs);
            }
        }
    }
    
    private List<FragmentGraph> saturateAtom(int atomIndex, FragmentGraph graph) {
        List<FragmentGraph> atomSolutions = new ArrayList<FragmentGraph>();
        saturateAtom(atomIndex, graph, atomSolutions);
        return atomSolutions;
    }
    
    private void saturateAtom(
            int atomIndex, FragmentGraph graph, List<FragmentGraph> graphs) {
        if (graph.isSaturated(atomIndex)) {
            System.out.println("saturating atom " + atomIndex + " in graph " + graph + " saturated");
            graphs.add(graph);
            return;
        } else {
            // fragments that still have unused instances
            List<String> unusedLabels = graph.unusedLabels();
            for (String label : unusedLabels) {
                FragmentGraph child = new FragmentGraph(graph);
                child.bond(label, atomIndex, builder);
//                if (CanonicalChecker.isCanonicalByRefinement(graph)) {
                if (isCanonical(child)) {
                    System.out.println("saturating atom " + atomIndex + " in graph " + graph + " canonical extension with " + label  + " " + child);
                    saturateAtom(atomIndex, child, graphs);
                } else {
                    System.out.println("saturating atom " + atomIndex + " in graph " + graph + " rejected extension with " + label + " " + child);
                }
            }
            
            // atoms that can still be attached to
            List<Integer> unsaturatedAtoms = graph.unsaturatedAtoms();
            for (int otherAtomIndex : unsaturatedAtoms) {
                if (otherAtomIndex == atomIndex) continue;
                if (graph.canIncreaseBondOrder(atomIndex, otherAtomIndex)) {
                    FragmentGraph child = new FragmentGraph(graph);
                    child.bond(atomIndex, otherAtomIndex, builder);
                    if (isCanonical(child)) {
                        System.out.println("saturating atom " + atomIndex + " in graph " + graph + " canonical cyclisation to " + otherAtomIndex + " " + child);
                        saturateAtom(atomIndex, child, graphs);
                    } else {
                        System.out.println("saturating atom " + atomIndex + " in graph " + graph + " rejected cyclisation to " + otherAtomIndex + " " + child);
                    }
                }
            }
        }
    }
    
    private boolean isCanonical(FragmentGraph graph) {
        return AlternateCanonicalChecker.isCanonicalByVisitor(
                graph.getAtomContainer());
//        return CanonicalChecker.isCanonicalWithColorPartition(
//              graph.getAtomContainer());
//        return CanonicalChecker.isCanonicalWithSignaturePartition(
//                graph.getAtomContainer());
//        return CanonicalChecker.isCanonicalByReconstruction(
//              graph.getAtomContainer());
        
    }
}
