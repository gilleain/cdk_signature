package tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrbitSaturationTreeGenerator {
    
    private List<LabelledTree> trees;
    
    private Map<String, Integer> degreeMap;
    
    public OrbitSaturationTreeGenerator(Map<String, Integer> degreeMap) {
        this.degreeMap = degreeMap;
    }
    
    public List<LabelledTree> generate(Map<String, Integer> maxCounts) {
        trees = new ArrayList<LabelledTree>();
        saturateTree(new LabelledTree(maxCounts));
        return trees;
    }
    
    private void saturateTree(LabelledTree tree) {
        boolean isCanonical = tree.isCanonical();
        boolean isComplete = tree.isComplete();
        if (isCanonical) {
            if (isComplete) {
                trees.add(tree);
                return;
            }
            System.out.println(tree + "is canonical BUT NOT complete");
            String orbitLabel = tree.getUnsaturatedOrbitLabel(degreeMap);
//          System.out.println(tree+ " isCanonical" + isCanonical 
//                  + " isComplete" + isComplete + ", so trying label " + orbitLabel);
          if (orbitLabel.equals("")) return;
          for (LabelledTree child : saturateOrbit(tree, orbitLabel)) {
              saturateTree(child);
          }
        } else {
            String orbitLabel = tree.getUnsaturatedOrbitLabel(degreeMap);
//            System.out.println(tree+ " isCanonical" + isCanonical 
//                    + " isComplete" + isComplete + ", so trying label " + orbitLabel);
            if (orbitLabel.equals("")) return;
            for (LabelledTree child : saturateOrbit(tree, orbitLabel)) {
                saturateTree(child);
            }
        }
    }
    
    private List<LabelledTree> saturateOrbit(
            LabelledTree tree, String orbitLabel) {
        List<LabelledTree> orbitSolutions = new ArrayList<LabelledTree>();
        saturateOrbit(tree, orbitLabel, orbitSolutions);
//        System.out.println("returning solutions " + orbitSolutions);
        return orbitSolutions;
    }
    
    private void saturateOrbit(LabelledTree tree, 
            String orbitLabel, List<LabelledTree> orbitSolutions) {
//        System.out.println("saturating orbit for " + tree + " " + tree.orbitStrings());
        if (tree.isEmptyOrbit(orbitLabel)) {
            orbitSolutions.add(tree);
        } else {
            int nodeIndex;
            if (tree.size() == 0) {
                tree.makeRoot(orbitLabel);
//                System.out.println("registering " + orbitLabel);
                tree.registerLabelInOrbit(orbitLabel, 0);
                nodeIndex = 0;
            } else {
                nodeIndex = tree.getIndexForOrbitLabel(orbitLabel, degreeMap);
            }
            if (nodeIndex == -1) return;
            
            for (LabelledTree child : saturateNode(tree, nodeIndex)) {
                saturateOrbit(child, orbitLabel, orbitSolutions);
            }
        }
        
    }
    
    private List<LabelledTree> saturateNode(LabelledTree tree, int nodeIndex) {
        List<LabelledTree> nodeSolutions = new ArrayList<LabelledTree>();
        saturateNode(tree, nodeIndex, nodeSolutions);
        return nodeSolutions;
    }
    
    private void saturateNode(
           LabelledTree tree, int nodeIndex, List<LabelledTree> nodeSolutions) {
        if (tree.isSaturated(nodeIndex, degreeMap)) {
//            System.out.println("adding " + tree + " to nodeSolutions");
            nodeSolutions.add(tree);
            return;
        } else {
//            System.out.println("saturating " + tree + " at " + nodeIndex + " with " + tree.orbitStrings() + " and " + tree.unusedLabels());
            for (String label : tree.unusedLabels()) {
                LabelledTree child = tree.growByChild(nodeIndex, label);
                saturateNode(child, nodeIndex, nodeSolutions);
            }
        }
    }

}
