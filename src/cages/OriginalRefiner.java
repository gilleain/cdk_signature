package cages;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openscience.cdk.group.Partition;

import engine.Graph;


public class OriginalRefiner {
    
    private Graph graph;
    
    private int numberOfVertices;
    
    private int currentBlockIndex;
    
    private List<Set<Integer>> blocksToRefine;
    
    private Set universe;
    
    public OriginalRefiner(Graph graph) {
        this.graph = graph;
        this.numberOfVertices = graph.getVertexCount();
    }
    
    public Partition refine(Partition a) {
        Partition b = new Partition(a);
        
        // start the queue with the blocks of a in reverse order
        this.blocksToRefine = new LinkedList<Set<Integer>>();
        for (int i = 0; i < b.size(); i++) {
            this.blocksToRefine.add(b.copyBlock(i));
        }
        
        this.universe = makeFullSet(numberOfVertices);
        
        int blocksLeft = b.size() - 1;
        while (blocksLeft != 0) {
            blocksLeft--;
            Set<Integer> t = blocksToRefine.get(blocksLeft);
            if (universe.containsAll(t)) {
                universe.removeAll(t);
                currentBlockIndex = 0;
                int l = b.size();
                while (currentBlockIndex < l && l < numberOfVertices) {
                    if (!b.isDiscreteCell(currentBlockIndex)) {
                        
                        // get the neighbor invariants for this block
                        Map<Integer, SortedSet> invariants = 
                            this.getNeighborInvariants(b, t);
                        
                        // split the block on the basis of these invariants
                        blocksLeft = splitAndUpdate(invariants, b,  blocksLeft);
                    }
                    currentBlockIndex++;
                }
                
                // the partition is discrete
                if (b.size() == numberOfVertices) {
                    return b;
                }
            }
        }
        return b;
    }
    
    /**
     * Gets the neighbor invariants for the block j as a map of 
     * |N<sub>g</sub>(v) &cap; T| to elements of the block j. That is, the
     * size of the intersection between the set of neighbors of element v in
     * the graph and the target block T. 
     *  
     * @param partition the current partition
     * @param targetBlock the current target block of the partition
     * @return a map of set intersection sizes to elements
     */
    private Map<Integer, SortedSet> getNeighborInvariants(
            Partition partition, Set<Integer> targetBlock) {
        Map<Integer, SortedSet> setList = new HashMap<Integer, SortedSet>();
        for (int u : partition.getCell(currentBlockIndex)) {
            int h = intersectionSize(targetBlock, getConnected(u));
            if (setList.containsKey(h)) {
                setList.get(h).add(u);
            } else {
                SortedSet set = new TreeSet();
                set.add(u);
                setList.put(h, set);
            }
        }
        return setList;
    }
    
    private int splitAndUpdate(
            Map<Integer, SortedSet> invariants, Partition partition, int n) {
        
        int nonEmptyInvariants = invariants.keySet().size();
        
        if (nonEmptyInvariants > 1) {
//            partition.increaseSize(nonEmptyInvariants - 1);
//            for (int h = partition.size() - 1; h > currentBlockIndex; h--) {
//                partition.copyBlock(partition, nonEmptyInvariants - 1 + h, h);
//            }
            partition.removeCell(currentBlockIndex);
            int k = 0;
            for (int h = 0; h < numberOfVertices; h++) {
                SortedSet setH = invariants.get(h);
                if (setH != null) {
//                    partition.setBlock(currentBlockIndex + k, setH);
                    partition.addCell(setH);
                    blocksToRefine.add(setH);
                    universe.addAll(setH);
                    k++;
                    n++;
                }
                currentBlockIndex += nonEmptyInvariants - 1;
            }
        }
        return n;
    }
    
    private List<Integer> getConnected(int vertexIndex) {
        return this.graph.getConnected(vertexIndex);
    }
    
    private int intersectionSize(Set<Integer> a, List<Integer> b) {
        Set intersection = new HashSet();
        for (int i : b) { intersection.add(i); }
        intersection.retainAll(a);
        return intersection.size();
    }

    private Set makeFullSet(int n) {
        Set full = new HashSet(n);
        for (int i = 0; i < n; i++) {
            full.add(i);
        }
        return full;
    }

}
