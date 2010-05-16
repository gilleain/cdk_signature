package test_group;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.graph.AtomContainerAtomPermutor;
//import org.openscience.cdk.graph.Permutor;
import org.openscience.cdk.group.CDKDiscretePartitionRefiner;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.structgen.deterministic.CanonicalChecker;


public class CDKDiscretePartitionRefinerTest {
    
    private IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
    
    public boolean isCanonical(IAtomContainer container) {
        return new CDKDiscretePartitionRefiner().isCanonical(container);
    }
    
    public boolean isCanonicalWithDisconnectedAtoms(IAtomContainer container) {
        return new CDKDiscretePartitionRefiner(true).isCanonical(container);
    }
    
    @Test
    public void testAutGroupForCamphor() {
        IAtomContainer camphor = builder.newInstance(IAtomContainer.class);
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"C"));
        camphor.addAtom(builder.newInstance(IAtom.class,"O"));
        
        camphor.addBond(0, 2, IBond.Order.SINGLE);
        camphor.addBond(1, 2, IBond.Order.SINGLE);
        camphor.addBond(2, 3, IBond.Order.SINGLE);
        camphor.addBond(2, 4, IBond.Order.SINGLE);
        camphor.addBond(3, 5, IBond.Order.SINGLE);
        camphor.addBond(3, 6, IBond.Order.SINGLE);
        camphor.addBond(3, 7, IBond.Order.SINGLE);
        camphor.addBond(4, 8, IBond.Order.SINGLE);
        camphor.addBond(4, 9, IBond.Order.SINGLE);
        camphor.addBond(6, 8, IBond.Order.SINGLE);
        camphor.addBond(7, 9, IBond.Order.SINGLE);
        camphor.addBond(7, 10, IBond.Order.DOUBLE);
        
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
        SSPermutationGroup aut = refiner.getAutomorphismGroup(camphor);
        Assert.assertEquals(2, aut.order());
    }
    
    @Test
    public void testBridgedCycloButane() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 0
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 1
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 2
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 3
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 4
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 5
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 6
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 7
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 8
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 9
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 10
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 11
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 12
        
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 3, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        atomContainer.addBond(0, 5, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 2, IBond.Order.SINGLE);
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
        atomContainer.addBond(1, 4, IBond.Order.SINGLE);
        atomContainer.addBond(1, 6, IBond.Order.SINGLE);
        
        atomContainer.addBond(2, 7, IBond.Order.SINGLE);
        atomContainer.addBond(2, 8, IBond.Order.SINGLE);
        
        atomContainer.addBond(3, 9, IBond.Order.SINGLE);
        atomContainer.addBond(3, 10, IBond.Order.SINGLE);

        atomContainer.addBond(4, 11, IBond.Order.SINGLE);
        atomContainer.addBond(4, 12, IBond.Order.SINGLE);
        
//        boolean isCanon = isCanonical(atomContainer);
        boolean isCanon = CanonicalChecker.isCanonicalWithSignaturePartition(atomContainer);
        System.out.println(isCanon);
    }
    
    @Test
    public void testPartialC4H4() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addBond(0, 1, IBond.Order.DOUBLE);
        atomContainer.addBond(0, 3, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
        atomContainer.addBond(1, 5, IBond.Order.SINGLE);
        atomContainer.addBond(2, 3, IBond.Order.DOUBLE);
        atomContainer.addBond(2, 6, IBond.Order.SINGLE);
        atomContainer.addBond(2, 7, IBond.Order.SINGLE);
//        CanonicalChecker.isCanonicalTest(atomContainer);
        boolean b=CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
//        boolean b=CanonicalChecker.isCanonical(atomContainer);
        System.out.println(b);
//        CanonicalChecker.signaturePartition(atomContainer);
        CanonicalChecker.searchForSignaturePartition(atomContainer);
    }
    
    @Test
    public void testC4H4Isomer() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 3, IBond.Order.DOUBLE);
        atomContainer.addBond(1, 2, IBond.Order.DOUBLE);
        atomContainer.addBond(1, 4, IBond.Order.SINGLE);
        atomContainer.addBond(2, 5, IBond.Order.SINGLE);
        atomContainer.addBond(3, 6, IBond.Order.SINGLE);
        atomContainer.addBond(3, 7, IBond.Order.SINGLE);
        boolean b=CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
//        CanonicalChecker.isCanonicalTest(atomContainer);
        System.out.println(b);
        CanonicalChecker.signaturePartition(atomContainer);
    }
    
    @Test
    public void testMinimalCanonicalExample() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        Assert.assertTrue(isCanonical(atomContainer));
    }
    
    @Test
    public void testMinimalCanonicalExampleWithDisconnectedAtoms() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        Assert.assertTrue(isCanonicalWithDisconnectedAtoms(atomContainer));
    }
    
    @Test
    public void testPartialButaneA() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        boolean isCanonical = 
            CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
        System.out.println("Is canonical " + isCanonical);
    }
    
    @Test
    public void testPartialButane() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        atomContainer.addBond(0, 5, IBond.Order.SINGLE);
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
//        CanonicalChecker.isMinimalTest(atomContainer);
//        CanonicalChecker.isCanonicalTest(atomContainer);
//        Assert.assertTrue(isCanonical(atomContainer));
        boolean isCanonical = 
            CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
        System.out.println("Is canonical " + isCanonical);
    }
    
    @Test
    public void testButene() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 0
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 1
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 2
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 3
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 4
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 5
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 6
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 7
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 8
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 9
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 10
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 11
        
        atomContainer.addBond(0, 1, IBond.Order.DOUBLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 5, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
        atomContainer.addBond(1, 4, IBond.Order.SINGLE);
        
        atomContainer.addBond(2, 6, IBond.Order.SINGLE);
        atomContainer.addBond(2, 7, IBond.Order.SINGLE);
        atomContainer.addBond(2, 8, IBond.Order.SINGLE);
        
        atomContainer.addBond(3, 9, IBond.Order.SINGLE);
        atomContainer.addBond(3, 10, IBond.Order.SINGLE);
        atomContainer.addBond(3, 11, IBond.Order.SINGLE);
//        CanonicalChecker.isCanonicalTest(atomContainer);
//        System.out.println(CanonicalChecker.getBest(atomContainer));
        Assert.assertTrue(isCanonical(atomContainer));
    }
    
    @Test
    public void testButane() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 0
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 1
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 2
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 3
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 4
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 5
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 6
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 7
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 8
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 9
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 10
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 11
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 12
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 13
        
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        atomContainer.addBond(0, 5, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
        atomContainer.addBond(1, 6, IBond.Order.SINGLE);
        atomContainer.addBond(1, 7, IBond.Order.SINGLE);
        
        atomContainer.addBond(2, 8, IBond.Order.SINGLE);
        atomContainer.addBond(2, 9, IBond.Order.SINGLE);
        atomContainer.addBond(2, 10, IBond.Order.SINGLE);
        
        atomContainer.addBond(3, 11, IBond.Order.SINGLE);
        atomContainer.addBond(3, 12, IBond.Order.SINGLE);
        atomContainer.addBond(3, 13, IBond.Order.SINGLE);
//        System.out.println(CanonicalChecker.getBest(atomContainer));
//        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
//        refiner.isCanonical(atomContainer);
//        refiner.compareRowwise(refiner.getBest());
        Assert.assertTrue(isCanonical(atomContainer));
    }
    
    @Test
    public void testIsoButane() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 0
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 1
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 2
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 3
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 4
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 5
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 6
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 7
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 8
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 9
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 10
        
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 11
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 12
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 13
        
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 3, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 5, IBond.Order.SINGLE);
        atomContainer.addBond(1, 6, IBond.Order.SINGLE);
        atomContainer.addBond(1, 7, IBond.Order.SINGLE);
        
        atomContainer.addBond(2, 8, IBond.Order.SINGLE);
        atomContainer.addBond(2, 9, IBond.Order.SINGLE);
        atomContainer.addBond(2, 10, IBond.Order.SINGLE);
        
        atomContainer.addBond(3, 11, IBond.Order.SINGLE);
        atomContainer.addBond(3, 12, IBond.Order.SINGLE);
        atomContainer.addBond(3, 13, IBond.Order.SINGLE);
        Assert.assertTrue(isCanonical(atomContainer));
    }
    
    @Test
    public void testPropene() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 0
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 1
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 2
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 3
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 4
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 5
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 6
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 7
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 8
        
        atomContainer.addBond(0, 1, IBond.Order.DOUBLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 3, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 4, IBond.Order.SINGLE);
        atomContainer.addBond(1, 5, IBond.Order.SINGLE);
        
        atomContainer.addBond(2, 6, IBond.Order.SINGLE);
        atomContainer.addBond(2, 7, IBond.Order.SINGLE);
        atomContainer.addBond(2, 8, IBond.Order.SINGLE);
        
        Assert.assertTrue(isCanonical(atomContainer));
//        CanonicalChecker.isCanonicalTest(atomContainer);
    }
    
    @Test
    public void testNeoPentane() {
        IAtomContainer atomContainer = builder.newInstance(IAtomContainer.class);
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 0
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 1
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 2
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 3
        atomContainer.addAtom(builder.newInstance(IAtom.class,"C"));    // 4
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 5
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 6
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 7
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 8
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 9
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 10
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 11
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 12
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 13
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 14
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 15
        atomContainer.addAtom(builder.newInstance(IAtom.class,"H"));    // 16
        
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 3, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 5, IBond.Order.SINGLE);
        atomContainer.addBond(1, 6, IBond.Order.SINGLE);
        atomContainer.addBond(1, 7, IBond.Order.SINGLE);
        
        atomContainer.addBond(2, 8, IBond.Order.SINGLE);
        atomContainer.addBond(2, 9, IBond.Order.SINGLE);
        atomContainer.addBond(2, 10, IBond.Order.SINGLE);
        
        atomContainer.addBond(3, 11, IBond.Order.SINGLE);
        atomContainer.addBond(3, 12, IBond.Order.SINGLE);
        atomContainer.addBond(3, 13, IBond.Order.SINGLE);
        
        atomContainer.addBond(4, 14, IBond.Order.SINGLE);
        atomContainer.addBond(4, 15, IBond.Order.SINGLE);
        atomContainer.addBond(4, 16, IBond.Order.SINGLE);
        
//        isCanonical(atomContainer);
        boolean c=CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
        System.out.println(c);
    }
    
    public void halfMatrix(IAtomContainer container) {
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
        System.out.println(refiner.isCanonical(container) 
                + " " + refiner.getHalfMatrixString()
                + " " + refiner.getFirstHalfMatrixString()
                + " " + refiner.getBestHalfMatrixString());
    }

    @Test
    public void ngonTests() {
        
        IAtomContainer fourgon = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) { fourgon.addAtom(builder.newInstance(IAtom.class,"C")); }
        fourgon.addBond(0, 1, IBond.Order.SINGLE);
        fourgon.addBond(0, 2, IBond.Order.SINGLE);
        fourgon.addBond(1, 3, IBond.Order.SINGLE);
        fourgon.addBond(2, 3, IBond.Order.SINGLE);
        halfMatrix(fourgon);
        
        IAtomContainer fivegon = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 5; i++) { fivegon.addAtom(builder.newInstance(IAtom.class,"C")); }
        fivegon.addBond(0, 1, IBond.Order.SINGLE);
        fivegon.addBond(0, 2, IBond.Order.SINGLE);
        fivegon.addBond(1, 3, IBond.Order.SINGLE);
        fivegon.addBond(2, 4, IBond.Order.SINGLE);
        fivegon.addBond(3, 4, IBond.Order.SINGLE);
        halfMatrix(fivegon);
        
        IAtomContainer sixgon = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 6; i++) { sixgon.addAtom(builder.newInstance(IAtom.class,"C")); }
        sixgon.addBond(0, 1, IBond.Order.SINGLE);
        sixgon.addBond(0, 2, IBond.Order.SINGLE);
        sixgon.addBond(1, 3, IBond.Order.SINGLE);
        sixgon.addBond(2, 4, IBond.Order.SINGLE);
        sixgon.addBond(3, 5, IBond.Order.SINGLE);
        sixgon.addBond(4, 5, IBond.Order.SINGLE);
        halfMatrix(sixgon);

    }
    
    @Test
    public void cnopPermutation() {
        IAtomContainer cnop = builder.newInstance(IAtomContainer.class);
        cnop.addAtom(builder.newInstance(IAtom.class,"C"));
        cnop.addAtom(builder.newInstance(IAtom.class,"N"));
        cnop.addAtom(builder.newInstance(IAtom.class,"O"));
        cnop.addAtom(builder.newInstance(IAtom.class,"P"));
        cnop.addBond(0, 1, IBond.Order.SINGLE);
        cnop.addBond(0, 2, IBond.Order.SINGLE);
        cnop.addBond(1, 3, IBond.Order.SINGLE);
        cnop.addBond(2, 3, IBond.Order.SINGLE);
        permutationTest(cnop);
    }
    
    @Test
    public void fourgonPermutation() {
        IAtomContainer fourgon = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) { fourgon.addAtom(builder.newInstance(IAtom.class,"C")); }
        fourgon.addBond(0, 1, IBond.Order.DOUBLE);
        fourgon.addBond(0, 2, IBond.Order.SINGLE);
        fourgon.addBond(1, 3, IBond.Order.SINGLE);
        fourgon.addBond(2, 3, IBond.Order.DOUBLE);
        permutationTest(fourgon);
    }
    
    @Test
    public void fivegonPermutation() {
        IAtomContainer fivegon = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 5; i++) { fivegon.addAtom(builder.newInstance(IAtom.class,"C")); }
        fivegon.addBond(0, 1, IBond.Order.SINGLE);
        fivegon.addBond(0, 2, IBond.Order.DOUBLE);
        fivegon.addBond(1, 3, IBond.Order.DOUBLE);
        fivegon.addBond(2, 4, IBond.Order.SINGLE);
        fivegon.addBond(3, 4, IBond.Order.SINGLE);
        
        int [] p = new int[] {2, 0, 4, 1, 3};
        
        // this is a hack - fix permutor!
//        Permutor rawPermutor = new Permutor(5);
//        int rank = 0;
//        while (rawPermutor.hasNext()) {
//            int[] next = rawPermutor.getNextPermutation();
//            if (Arrays.equals(p, next)) {
//                break;
//            }
//            rank++;
//        }
//        
//        AtomContainerAtomPermutor permutor = new AtomContainerAtomPermutor(fivegon);
//        Permutation permutation = new Permutation(p);
////        permutor.setPermutation(p);
//        permutor.setRank(rank);
////        String s1 = CanonicalChecker.edgeString(fivegon, permutation);
////        IAtomContainer permuted = permutor.next();
//        String s2 = CanonicalChecker.edgeString(permuted, new Permutation(5));
//        System.out.println(s1);
//        System.out.println(s2);
//        permutationTest(fivegon);
    }
    
    @Test
    public void sixgonPermutation() {
        IAtomContainer sixgon = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 6; i++) { sixgon.addAtom(builder.newInstance(IAtom.class,"C")); }
        sixgon.addBond(0, 1, IBond.Order.DOUBLE);
        sixgon.addBond(0, 2, IBond.Order.SINGLE);
        sixgon.addBond(1, 3, IBond.Order.SINGLE);
        sixgon.addBond(2, 4, IBond.Order.DOUBLE);
        sixgon.addBond(3, 5, IBond.Order.DOUBLE);
        sixgon.addBond(4, 5, IBond.Order.SINGLE);
        permutationTest(sixgon);
    }
    
    public void permutationTest(IAtomContainer container) {
        AtomContainerAtomPermutor permutor = new AtomContainerAtomPermutor(container);
//        System.out.println("original is canonical? " + isCanonical(container));
        Assert.assertTrue("Initial structure is not canonical", isCanonical(container));
        while (permutor.hasNext()) {
            IAtomContainer permutation = permutor.next();
//            boolean canon = isCanonical(permutation);
            boolean canon = CanonicalChecker.isCanonicalWithSignaturePartition(permutation);
//            if (!canon) continue;
            boolean aut = isAutomorphic(container, permutation);
//            System.out.println("permutation is canonical? " 
//                    +  ((canon)? "CANON" : "")
//                    + " under " + Arrays.toString(permutor.getCurrentPermutation())
//                    + " is aut " + ((aut)? "TRUE" : ""));
        }
    }
    
    public boolean isAutomorphic(IAtomContainer original, IAtomContainer permutation) {
        Permutation identity = new Permutation(original.getAtomCount());
        String originalString = CanonicalChecker.edgeString(original, identity);
        String permutedString = CanonicalChecker.edgeString(permutation, identity);
        System.out.println(originalString + " vs " + permutedString);
        return originalString.equals(permutedString);
    }
}
