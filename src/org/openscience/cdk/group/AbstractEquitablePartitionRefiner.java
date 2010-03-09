package org.openscience.cdk.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Refines a 'coarse' partition (with more blocks) to a 'finer' partition that
 * is equitable.
 * 
 * Closely follows algorithm 7.5 in CAGES. The basic idea is that the refiner
 * maintains a queue of blocks to refine, starting with all the initial blocks
 * in the partition to refine. These blocks are popped off the queue, and
 * 
 * @author maclean
 * @cdk.module group
 */
public abstract class AbstractEquitablePartitionRefiner {
    
    /**
     * The block of the partition that is being refined
     */
    private int currentBlockIndex;
    
    /**
     * The blocks to be refined, or at least considered for refinement
     */
    private Queue<Set<Integer>> blocksToRefine;
    
    /**
     * Gets from the graph the number of vertices. Abstract to allow different 
     * graph classes to be used (eg: IMolecule or IAtomContainer, etc).
     * 
     * @return the number of vertices
     */
    public abstract int getNumberOfVertices();
    
    /**
     * Determines the vertex indices (eg atom numbers) of the vertices
     * connected to the vertex with vertex index <code>vertexIndex</code>.
     *   
     * @param vertexIndex the index of the vertex to use
     * @return a Set of vertex indices
     */
    public abstract Set<Integer> getConnected(int vertexIndex); 
    
    /**
     * Refines the coarse partition <code>a</code> into a finer one.
     *  
     * @param a the partition to refine
     * @return a finer partition
     */
    public Partition refine(Partition a) {
        Partition b = new Partition(a);
        
        // start the queue with the blocks of a in reverse order
        this.blocksToRefine = new LinkedList<Set<Integer>>();
        for (int i = 0; i < b.size(); i++) {
            this.blocksToRefine.add(b.copyBlock(i));
        }
        
        int numberOfVertices = getNumberOfVertices();
        while (!blocksToRefine.isEmpty()) {
            Set<Integer> t = blocksToRefine.remove();
            currentBlockIndex = 0;
            int l = b.size();
            while (currentBlockIndex < l && l < numberOfVertices) {
                if (!b.isDiscreteCell(currentBlockIndex)) {

                    // get the neighbor invariants for this block
                    Map<Integer, SortedSet> invariants = getInvariants(b, t);

                    // split the block on the basis of these invariants
                    split(invariants, b);
                }
                currentBlockIndex++;
            }

            // the partition is discrete
            if (b.size() == numberOfVertices) {
                return b;
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
    private Map<Integer, SortedSet> getInvariants(
            Partition partition, Set<Integer> targetBlock) {
        Map<Integer, SortedSet> setList = new HashMap<Integer, SortedSet>();
        for (int u : partition.getCell(currentBlockIndex)) {
//            int h = intersectionSize(targetBlock, getConnected(u));
            int h = neighboursInBlock(targetBlock, u);
            if (setList.containsKey(h)) {
                setList.get(h).add(u);
            } else {
                SortedSet set = new TreeSet();
                set.add(u);
                setList.put(h, set);
            }
        }
//        System.out.println("target block " + targetBlock + " inv " + setList);
        return setList;
    }
    
    /**
     * Split the current block using the invariants calculated in getInvariants.
     * 
     * @param invariants a map of neighbor counts to elements
     * @param partition the partition that is being refined
     */
    private void split(Map<Integer, SortedSet> invariants, Partition partition) {
        int nonEmptyInvariants = invariants.keySet().size();
        if (nonEmptyInvariants > 1) {
            List<Integer> invariantKeys =  new ArrayList<Integer>();
            invariantKeys.addAll(invariants.keySet());
            partition.removeCell(currentBlockIndex);
            int k = currentBlockIndex;
            // TODO : allow both increasing and decreasing
            Collections.sort(invariantKeys, Collections.reverseOrder());
            for (int h : invariantKeys) {
                SortedSet setH = invariants.get(h);
//                System.out.println("adding block " + setH + " at " + k + " h=" + h);
                partition.insertCell(k, setH);
                blocksToRefine.add(setH);
                k++;
                currentBlockIndex += nonEmptyInvariants - 1;
            }
        }
    }
    
    public abstract int neighboursInBlock(Set<Integer> block, int vertexIndex);
    
    /**
     * Find |a &cap; b| - that is, the size of the intersection between a and b.
     * 
     * @param a a set of numbers
     * @param b a set of numbers
     * @return the size of the intersection
     */
    private int intersectionSize(Set<Integer> a, Set<Integer> b) {
        Set intersection = new HashSet(a);
        intersection.retainAll(b);
        return intersection.size();
    }

}
