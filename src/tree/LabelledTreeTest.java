package tree;

import junit.framework.Assert;

import org.junit.Test;

public class LabelledTreeTest {
    
    public void lex(LabelledTree a, LabelledTree b) {
        String aStr = a.toString();
        String bStr = b.toString();
        int c = aStr.compareTo(bStr); 
        if (c < 0) {
            System.out.println(aStr + " < " + bStr);
        } else if (c > 0) {
            System.out.println(aStr + " > " + bStr);
        } else {
            System.out.println(aStr + " = " + bStr);
        }
    }
    
    @Test
    public void testSecondaryAlcoholIsomers() {
        LabelledTree treeA = LabelledTree.fromString("C(C(O(H)C(HHH)H)HHH)");
        LabelledTree treeB = LabelledTree.fromString("C(C(C(HHH)HO(H))HHH)");
        System.out.println(treeA + " is canonical? " + treeA.isCanonical());
        System.out.println(treeB + " is canonical? " + treeB.isCanonical());
        lex(treeA, treeB);
    }
    
    @Test
    public void testPropaneIsomers() {
        LabelledTree treeA = LabelledTree.fromString("C(C(C(HHH)HH)HHH)");
        LabelledTree treeB = LabelledTree.fromString("C(C(HHH)C(HHH)HH)");
        System.out.println(treeA + " is canonical? " + treeA.isCanonical());
        System.out.println(treeB + " is canonical? " + treeB.isCanonical());
        lex(treeA, treeB);
    }
    
    @Test
    public void testNumbering() {
        LabelledTree tree = LabelledTree.fromString("C(C(CC)C(C))");
        for (LabelledTree.Node node : tree.getNodes()) {
            System.out.println(node.index);
        }
    }
    
    @Test
    public void testRoundtrip() {
        String treeAsString = "C(C(CC)C(C))";
        LabelledTree tree = LabelledTree.fromString(treeAsString);
        Assert.assertEquals(treeAsString, tree.toString());
    }
    
    @Test
    public void testCanonicalRoundtrip() {
        String asPlainString = "C(HC(H)H)";
        String asCanonicalString = "C(C(H)HH)";
        
        LabelledTree plainTree = LabelledTree.fromString(asPlainString);
        Assert.assertFalse(plainTree.isCanonical());
        
        LabelledTree canonicalTree = LabelledTree.fromString(asCanonicalString);
        Assert.assertTrue(canonicalTree.isCanonical());
    }
    
    @Test
    public void testTraversals() {
        LabelledTree tree = new LabelledTree("C");
        tree.makeChild(0, "C");
        tree.makeChild(0, "C");
        tree.makeChild(1, "C");
        tree.makeChild(3, "C");
        for (int i = 0; i < tree.size(); i++) {
            System.out.println(
                    String.format("%3d %s", i, tree.getCanonicalString(i)));
        }
    }
    
    @Test
    public void traverseUpLinearChain() {
        LabelledTree tree = new LabelledTree("C");
        tree.makeChild(0, "C");
        tree.makeChild(1, "C");
        System.out.println(tree.getCanonicalString(2));
    }
    
    @Test
    public void testCanonical() {
        LabelledTree treeA = new LabelledTree("C");
        treeA.makeChild(0, "C");
        treeA.makeChild(1, "C");
        System.out.println(treeA + " is canonical? " + treeA.isCanonical());
        
        LabelledTree treeB = new LabelledTree("C");
        treeB.makeChild(0, "C");
        treeB.makeChild(0, "C");
        System.out.println(treeB + " is canonical? " + treeB.isCanonical());

        lex(treeA, treeB);
    }
    
    @Test
    public void testNeighbours() {
        LabelledTree tree = new LabelledTree("C");
        tree.makeChild(0, "H");
        for (int i = 0; i < tree.size(); i++) {
            LabelledTree.Node node = tree.getNode(i);
            System.out.println(String.format("%3d %s %s", i, node, node.numberOfNeighbours()));
        }
    }

}
