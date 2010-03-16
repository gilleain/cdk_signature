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
    public void testMethane() {
        DeterministicEnumeratorTest.testFormula("CH4", 1);
    }
    
    @Test
    public void testEthyne() {
        DeterministicEnumeratorTest.testFormula("C2H2", 1);
    }
    
    @Test
    public void testEthene() {
        DeterministicEnumeratorTest.testFormula("C2H4", 1);
    }
    
    @Test
    public void testEthane() {
        DeterministicEnumeratorTest.testFormula("C2H6", 1);
    }
    
    @Test
    public void testPropene() {
        DeterministicEnumeratorTest.testFormula("C3H6", 1);
    }
    
    @Test
    public void testPropane() {
        DeterministicEnumeratorTest.testFormula("C3H8", 1);
    }
    
    @Test
    public void testButene() {
        DeterministicEnumeratorTest.testFormula("C4H8", 1);
    }
    
    @Test
    public void testButane() {
        DeterministicEnumeratorTest.testFormula("C4H10", 1);
    }
    
    @Test
    public void testPentane() {
        DeterministicEnumeratorTest.testFormula("C5H12", 1);
    }
    
    @Test
    public void testHexane() {
        DeterministicEnumeratorTest.testFormula("C6H14", 1);
    }
    
    public static void main(String[] args) {
        new DeterministicEnumeratorTest().testButane();
    }

}
