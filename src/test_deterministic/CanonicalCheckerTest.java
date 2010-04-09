package test_deterministic;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.deterministic.CanonicalChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class CanonicalCheckerTest {
    
    private IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    @Test
    public void trivialTest() {
        IAtomContainer ac = builder.newAtomContainer();
        ac.addAtom(builder.newAtom("C"));
        ac.addAtom(builder.newAtom("C"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        Assert.assertEquals(true, CanonicalChecker.isCanonical(ac));
    }
    
    @Test
    public void disconnectedBondsTest() {
        IAtomContainer ac = builder.newAtomContainer();
        ac.addAtom(builder.newAtom("C"));
        ac.addAtom(builder.newAtom("C"));
        ac.addAtom(builder.newAtom("C"));
        ac.addAtom(builder.newAtom("C"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        Assert.assertEquals(true, CanonicalChecker.isCanonical(ac));
    }
    
    @Test
    public void testFourCycle() {
        IAtomContainer ac = builder.newAtomContainer();
        ac.addAtom(builder.newAtom("C"));
        ac.addAtom(builder.newAtom("C"));
        ac.addAtom(builder.newAtom("C"));
        ac.addAtom(builder.newAtom("C"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(1, 3, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        Assert.assertEquals(true, CanonicalChecker.isCanonical(ac));
    }
    
    @Test
    public void cycloButeneMethyl1ene() {
        IAtomContainer ac = builder.newAtomContainer();
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newAtom("C"));
        }
        
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newAtom("H"));
        }
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(0, 3, IBond.Order.DOUBLE);
        ac.addBond(1, 2, IBond.Order.DOUBLE);
        ac.addBond(1, 4, IBond.Order.SINGLE);
        ac.addBond(2, 5, IBond.Order.SINGLE);
        ac.addBond(3, 6, IBond.Order.SINGLE);
        ac.addBond(3, 7, IBond.Order.SINGLE);
        Assert.assertEquals(true, 
                CanonicalChecker.isCanonicalWithColorPartition(ac));
    }

}
