package test_group;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.deterministic.CanonicalChecker;
import org.openscience.cdk.group.CDKDiscretePartitionRefiner;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class CDKDiscretePartitionRefinerTest {
    
    private IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
    
    public boolean isCanonical(IAtomContainer container) {
        return new CDKDiscretePartitionRefiner().isCanonical(container);
    }
    
    public boolean isCanonicalWithDisconnectedAtoms(IAtomContainer container) {
        return new CDKDiscretePartitionRefiner(true).isCanonical(container);
    }
    
    @Test
    public void testMinimalCanonicalExample() {
        IAtomContainer atomContainer = builder.newAtomContainer();
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        Assert.assertTrue(isCanonical(atomContainer));
    }
    
    @Test
    public void testMinimalCanonicalExampleWithDisconnectedAtoms() {
        IAtomContainer atomContainer = builder.newAtomContainer();
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        Assert.assertTrue(isCanonicalWithDisconnectedAtoms(atomContainer));
    }
    
    @Test
    public void testPartialButane() {
        IAtomContainer atomContainer = builder.newAtomContainer();
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addAtom(builder.newAtom("C"));
        atomContainer.addAtom(builder.newAtom("H"));
        atomContainer.addAtom(builder.newAtom("H"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        atomContainer.addBond(0, 5, IBond.Order.SINGLE);
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
        CanonicalChecker.isCanonicalTest(atomContainer);
//        Assert.assertTrue(isCanonical(atomContainer));
    }
    
    @Test
    public void testButene() {
        IAtomContainer atomContainer = builder.newAtomContainer();
        atomContainer.addAtom(builder.newAtom("C"));    // 0
        atomContainer.addAtom(builder.newAtom("C"));    // 1
        atomContainer.addAtom(builder.newAtom("C"));    // 2
        atomContainer.addAtom(builder.newAtom("C"));    // 3
        
        atomContainer.addAtom(builder.newAtom("H"));    // 4
        atomContainer.addAtom(builder.newAtom("H"));    // 5
        
        atomContainer.addAtom(builder.newAtom("H"));    // 6
        atomContainer.addAtom(builder.newAtom("H"));    // 7
        
        atomContainer.addAtom(builder.newAtom("H"));    // 8
        atomContainer.addAtom(builder.newAtom("H"));    // 9
        
        atomContainer.addAtom(builder.newAtom("H"));    // 10
        atomContainer.addAtom(builder.newAtom("H"));    // 11
        
        atomContainer.addAtom(builder.newAtom("H"));    // 12
        atomContainer.addAtom(builder.newAtom("H"));    // 13

        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.DOUBLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
        atomContainer.addBond(1, 5, IBond.Order.SINGLE);
        atomContainer.addBond(1, 6, IBond.Order.SINGLE);
        
        atomContainer.addBond(2, 7, IBond.Order.SINGLE);
        atomContainer.addBond(2, 8, IBond.Order.SINGLE);
        
        atomContainer.addBond(3, 9, IBond.Order.SINGLE);
        atomContainer.addBond(3, 10, IBond.Order.SINGLE);
        atomContainer.addBond(3, 11, IBond.Order.SINGLE);
//        CanonicalChecker.isCanonicalTest(atomContainer);
        System.out.println(CanonicalChecker.getBest(atomContainer));
//        Assert.assertTrue(isCanonical(atomContainer));
    }
    
    @Test
    public void testButaneLabelling() {
        IAtomContainer atomContainer = builder.newAtomContainer();
        atomContainer.addAtom(builder.newAtom("C"));    // 0
        atomContainer.addAtom(builder.newAtom("C"));    // 1
        atomContainer.addAtom(builder.newAtom("C"));    // 2
        atomContainer.addAtom(builder.newAtom("C"));    // 3
        atomContainer.addAtom(builder.newAtom("H"));    // 4
        atomContainer.addAtom(builder.newAtom("H"));    // 5
        atomContainer.addAtom(builder.newAtom("H"));    // 6
        atomContainer.addAtom(builder.newAtom("H"));    // 7
        
        atomContainer.addAtom(builder.newAtom("H"));    // 8
        atomContainer.addAtom(builder.newAtom("H"));    // 9
        atomContainer.addAtom(builder.newAtom("H"));    // 10
        
        atomContainer.addAtom(builder.newAtom("H"));    // 11
        atomContainer.addAtom(builder.newAtom("H"));    // 12
        atomContainer.addAtom(builder.newAtom("H"));    // 13
        
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 6, IBond.Order.SINGLE);
        atomContainer.addBond(0, 7, IBond.Order.SINGLE);
        
        atomContainer.addBond(1, 3, IBond.Order.SINGLE);
        atomContainer.addBond(1, 4, IBond.Order.SINGLE);
        atomContainer.addBond(1, 5, IBond.Order.SINGLE);
        
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
        IAtomContainer atomContainer = builder.newAtomContainer();
        atomContainer.addAtom(builder.newAtom("C"));    // 0
        atomContainer.addAtom(builder.newAtom("C"));    // 1
        atomContainer.addAtom(builder.newAtom("C"));    // 2
        atomContainer.addAtom(builder.newAtom("C"));    // 3
        atomContainer.addAtom(builder.newAtom("H"));    // 4
        atomContainer.addAtom(builder.newAtom("H"));    // 5
        atomContainer.addAtom(builder.newAtom("H"));    // 6
        atomContainer.addAtom(builder.newAtom("H"));    // 7
        
        atomContainer.addAtom(builder.newAtom("H"));    // 8
        atomContainer.addAtom(builder.newAtom("H"));    // 9
        atomContainer.addAtom(builder.newAtom("H"));    // 10
        
        atomContainer.addAtom(builder.newAtom("H"));    // 11
        atomContainer.addAtom(builder.newAtom("H"));    // 12
        atomContainer.addAtom(builder.newAtom("H"));    // 13
        
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
        IAtomContainer atomContainer = builder.newAtomContainer();
        atomContainer.addAtom(builder.newAtom("C"));    // 0
        atomContainer.addAtom(builder.newAtom("C"));    // 1
        atomContainer.addAtom(builder.newAtom("C"));    // 2
        atomContainer.addAtom(builder.newAtom("H"));    // 3
        atomContainer.addAtom(builder.newAtom("H"));    // 4
        atomContainer.addAtom(builder.newAtom("H"));    // 5
        atomContainer.addAtom(builder.newAtom("H"));    // 6
        atomContainer.addAtom(builder.newAtom("H"));    // 7
        atomContainer.addAtom(builder.newAtom("H"));    // 8
        
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
    
    public void halfMatrix(IAtomContainer container) {
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
        System.out.println(refiner.isCanonical(container) 
                + " " + refiner.getHalfMatrixString()
                + " " + refiner.getFirstHalfMatrixString()
                + " " + refiner.getBestHalfMatrixString());
    }

    @Test
    public void ngonTests() {
        
        IAtomContainer fourgon = builder.newAtomContainer();
        for (int i = 0; i < 4; i++) { fourgon.addAtom(builder.newAtom("C")); }
        fourgon.addBond(0, 1, IBond.Order.SINGLE);
        fourgon.addBond(0, 2, IBond.Order.SINGLE);
        fourgon.addBond(1, 3, IBond.Order.SINGLE);
        fourgon.addBond(2, 3, IBond.Order.SINGLE);
        halfMatrix(fourgon);
        
        IAtomContainer fivegon = builder.newAtomContainer();
        for (int i = 0; i < 5; i++) { fivegon.addAtom(builder.newAtom("C")); }
        fivegon.addBond(0, 1, IBond.Order.SINGLE);
        fivegon.addBond(0, 2, IBond.Order.SINGLE);
        fivegon.addBond(1, 3, IBond.Order.SINGLE);
        fivegon.addBond(2, 4, IBond.Order.SINGLE);
        fivegon.addBond(3, 4, IBond.Order.SINGLE);
        halfMatrix(fivegon);
        
        IAtomContainer sixgon = builder.newAtomContainer();
        for (int i = 0; i < 6; i++) { sixgon.addAtom(builder.newAtom("C")); }
        sixgon.addBond(0, 1, IBond.Order.SINGLE);
        sixgon.addBond(0, 2, IBond.Order.SINGLE);
        sixgon.addBond(1, 3, IBond.Order.SINGLE);
        sixgon.addBond(2, 4, IBond.Order.SINGLE);
        sixgon.addBond(3, 5, IBond.Order.SINGLE);
        sixgon.addBond(4, 5, IBond.Order.SINGLE);
        halfMatrix(sixgon);


    }

}
