package test_deterministic;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.deterministic.DeterministicEnumerator;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;

public class DeterministicEnumeratorTest {
    
    public static IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public static SmilesGenerator smilesGenerator = new SmilesGenerator();
    
    public static String toSmiles(IAtomContainer container) {
        if (ConnectivityChecker.isConnected(container)) {
            return DeterministicEnumeratorTest.smilesGenerator.createSMILES(
                    DeterministicEnumeratorTest.builder.newMolecule(container));
        } else {
            return "disconnected";
        }
    }
    
    public static void printResults(List<IAtomContainer> results) {
        for (IAtomContainer result : results) {
            System.out.println(DeterministicEnumeratorTest.toSmiles(result));
        }
    }
    
    public static void testFormula(String formulaString, int expected) {
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString);
        List<IAtomContainer> results = enumerator.generate();
        int actual = results.size();
        DeterministicEnumeratorTest.printResults(results);
//        Assert.assertEquals(expected, actual);
    }
    
    public void addAtoms(IAtomContainer ac, String symbol, int count) {
        for (int i = 0; i < count; i++) { 
            ac.addAtom(builder.newAtom(symbol)); 
        }
    }
    
    @Test
    public void testPartialEthane() {
        IAtomContainer ac = builder.newAtomContainer();
        addAtoms(ac, "C", 3);
        addAtoms(ac, "H", 8);
        ac.addBond(0, 3, IBond.Order.SINGLE);
        ac.addBond(0, 4, IBond.Order.SINGLE);
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(ac);
        enumerator.generate();
    }
    
    @Test
    public void testCNCN() {
        IAtomContainer ac = builder.newAtomContainer();
        addAtoms(ac, "C", 2);
        addAtoms(ac, "N", 2);
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(ac);
        for (IAtomContainer container : enumerator.generate()) {
            System.out.println(toSmiles(container));
        }
    }
    
    @Test
    public void testCH4() {
        DeterministicEnumeratorTest.testFormula("CH4", 1);
    }
    
    @Test
    public void testC2H2() {
        DeterministicEnumeratorTest.testFormula("C2H2", 1);
    }
    
    @Test
    public void testC2H4() {
        DeterministicEnumeratorTest.testFormula("C2H4", 1);
    }
    
    @Test
    public void testC2H6() {
        DeterministicEnumeratorTest.testFormula("C2H6", 1);
    }
    
    @Test
    public void testC3H4O() {
        DeterministicEnumeratorTest.testFormula("C3H4O", 1);
    }
    
    @Test
    public void testC3H6() {
        DeterministicEnumeratorTest.testFormula("C3H6", 1);
    }
    
    @Test
    public void testC3H8() {
        DeterministicEnumeratorTest.testFormula("C3H8", 1);
    }
    
    @Test
    public void testC4H4() {
        DeterministicEnumeratorTest.testFormula("C4H4", 1);
    }
    
    @Test
    public void testC4H4O() {
        DeterministicEnumeratorTest.testFormula("C4H4O", 1);
    }
    
    @Test
    public void testC4H6() {
        DeterministicEnumeratorTest.testFormula("C4H6", 1);
    }
    
    @Test
    public void testC4H8() {
        DeterministicEnumeratorTest.testFormula("C4H8", 1);
    }
    
    @Test
    public void testC4H10() {
        DeterministicEnumeratorTest.testFormula("C4H10", 1);
    }
    
    @Test
    public void testC5H8() {
        DeterministicEnumeratorTest.testFormula("C5H8", 1);
    }
    
    @Test
    public void testC5H12() {
        DeterministicEnumeratorTest.testFormula("C5H12", 1);
    }
    
    @Test
    public void testC6H14() {
        DeterministicEnumeratorTest.testFormula("C6H14", 1);
    }
    
    public static void main(String[] args) {
        new DeterministicEnumeratorTest().testC4H4();
    }

}
