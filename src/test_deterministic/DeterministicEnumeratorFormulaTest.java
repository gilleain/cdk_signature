package test_deterministic;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.deterministic.DeterministicEnumerator;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class DeterministicEnumeratorFormulaTest extends 
        AbstractDeterministicTest {
    
    public static IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public static void testFormula(String formulaString, int expected) {
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString);
        List<IAtomContainer> results = enumerator.generate();
        int actual = results.size();
        System.out.println(actual + " results");
        AbstractDeterministicTest.printResults(results);
//        Assert.assertEquals(expected, actual);
    }
    
    public void addAtoms(IAtomContainer ac, String symbol, int count) {
        for (int i = 0; i < count; i++) { 
            ac.addAtom(builder.newInstance(IAtom.class,symbol)); 
        }
    }
    
    @Test
    public void testPartialEthane() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
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
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
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
        DeterministicEnumeratorFormulaTest.testFormula("CH4", 1);
    }
    
    @Test
    public void testCNH6() {
        DeterministicEnumeratorFormulaTest.testFormula("CH6N", 1);
    }
    
    @Test
    public void testC2H2() {
        DeterministicEnumeratorFormulaTest.testFormula("C2H2", 1);
    }
    
    @Test
    public void testC2H4() {
        DeterministicEnumeratorFormulaTest.testFormula("C2H4", 1);
    }
    
    @Test
    public void testC2H6() {
        DeterministicEnumeratorFormulaTest.testFormula("C2H6", 1);
    }
    
    @Test
    public void testC3H4O() {
        DeterministicEnumeratorFormulaTest.testFormula("C3H4O", 1);
    }
    
    @Test
    public void testC3H6() {
        DeterministicEnumeratorFormulaTest.testFormula("C3H6", 1);
    }
    
    @Test
    public void testC3H8() {
        DeterministicEnumeratorFormulaTest.testFormula("C3H8", 1);
    }
    
    @Test
    public void testC3H6O() {
        DeterministicEnumeratorFormulaTest.testFormula("C3H6O", 1);
    }
    
    @Test
    public void testC3H6N() {
        DeterministicEnumeratorFormulaTest.testFormula("C3H5N", 1);
    }
    
    @Test
    public void testC4H4() {
        DeterministicEnumeratorFormulaTest.testFormula("C4H4", 1);
    }
    
    @Test
    public void testC4H4O() {
        DeterministicEnumeratorFormulaTest.testFormula("C4H4O", 1);
    }
    
    @Test
    public void testC4H6() {
        DeterministicEnumeratorFormulaTest.testFormula("C4H6", 1);
    }
    
    @Test
    public void testC4H8() {
        DeterministicEnumeratorFormulaTest.testFormula("C4H8", 1);
    }
    
    @Test
    public void testC4H10() {
        DeterministicEnumeratorFormulaTest.testFormula("C4H10", 1);
    }
    
    @Test
    public void testC5H8() {
        DeterministicEnumeratorFormulaTest.testFormula("C5H8", 1);
    }
    
    @Test
    public void testC5H12() {
        DeterministicEnumeratorFormulaTest.testFormula("C5H12", 1);
    }
    
    @Test
    public void testC6H12() {
        DeterministicEnumeratorFormulaTest.testFormula("C6H12", 1);
    }
    
    @Test
    public void testC6H14() {
        DeterministicEnumeratorFormulaTest.testFormula("C6H14", 1);
    }
    
    @Test
    public void testC7H12() {
        DeterministicEnumeratorFormulaTest.testFormula("C7H12", 1);
    }
    
    public static void main(String[] args) {
        new DeterministicEnumeratorFormulaTest().testC4H4();
    }

}
