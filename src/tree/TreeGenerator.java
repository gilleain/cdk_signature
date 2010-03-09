package tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple labelled tree generator.
 * 
 * @author maclean
 *
 */
public class TreeGenerator {
    
    private Map<String, Integer> degreeMap;
    
    private List<LabelledTree> trees;
    
    public TreeGenerator(Map<String, Integer> degreeMap) {
        this.degreeMap = degreeMap;
    }
    
    public int maximumDegree(String key) {
        return this.degreeMap.get(key);
    }
    
    private Map<String, Integer> copyMap(
            Map<String, Integer> map, String labelToRemove) {
        Map<String, Integer> currentCounts = new HashMap<String, Integer>();
        for (String key : map.keySet()) {
            int count = map.get(key);
            if (key.equals(labelToRemove)) {
                if (count > 1) {
                    currentCounts.put(key, count - 1);
                } else {
                    continue;
                }
            } else {
                currentCounts.put(key, count);
            }
        }
        return currentCounts;
    }
    
    public List<LabelledTree> generate(Map<String, Integer> maxCounts) {
        trees = new ArrayList<LabelledTree>();
        String label = maxCounts.keySet().iterator().next();    // blech!
        Map<String, Integer> currentCounts = copyMap(maxCounts, label);
        LabelledTree tree = new LabelledTree(label);
        generate(tree, currentCounts);
        return trees;
    }
    
    private void generate(LabelledTree tree, Map<String, Integer> currentCounts) {
//        System.out.println(tree + " " + tree.isCanonical());
        if (!tree.isCanonical()) return;
        if (currentCounts.isEmpty()) {
            if (tree.isCanonical()) {
                trees.add(tree);
//                System.out.println("Adding " + tree);
            } else {
//                System.out.println("Reject " + tree);
            }
            return;
        }
        
        for (int i = 0; i < tree.size(); i++) {
            for (String label : currentCounts.keySet()) {
                int count = currentCounts.get(label);
                if (count <= 0) continue;
                int n = tree.numberOfNeighboursOfNode(i);
                int m = maximumDegree(tree.getNode(i).label);
                if (n < m) {
                    generate(tree.growByChild(i, label), 
                            copyMap(currentCounts, label));
                }
            }
        }
    }

}
