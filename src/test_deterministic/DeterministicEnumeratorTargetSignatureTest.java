package test_deterministic;

import java.util.List;

import org.junit.Test;
import org.openscience.cdk.deterministic.DeterministicEnumerator;
import org.openscience.cdk.deterministic.FragmentConverter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.signature.TargetMolecularSignature;

public class DeterministicEnumeratorTargetSignatureTest extends 
                AbstractDeterministicTest {
    
    @Test
    public void degreeThreeTest() {
        IAtomContainer degreeThreeFragment = 
            AbstractDeterministicTest.makeDegreeThreeFragment();
        int count = 8;
        TargetMolecularSignature tms = 
            FragmentConverter.convert(degreeThreeFragment, count);
        String formulaString = "C8H8";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void metheneCyclopropaneTest() {
        IAtomContainer methene = AbstractDeterministicTest.makeCH2();
        int count = 3;
        
        TargetMolecularSignature tms = FragmentConverter.convert(methene, count);
        String formulaString = "C3H6";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void metheneCyclobutaneTest() {
        IAtomContainer methene = AbstractDeterministicTest.makeCH2();
        int count = 4;
        
        TargetMolecularSignature tms = FragmentConverter.convert(methene, count);
        String formulaString = "C4H8";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void methylCycloPropaneTest() {
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add("[C]([C][H][H][H])", 1);
        tms.add("[C]([C][C][H][H])", 2);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[H]([C])", 8);
        String formulaString = "C4H8";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void dimethylCycloButane() {
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add("[C]([C][H][H][H])", 2);
        tms.add("[C]([C][C][H][H])", 2);
        tms.add("[C]([C][C][C][H])", 2);
        tms.add("[H]([C])", 12);
        String formulaString = "C6H12";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    public static void main(String[] args) {
        new DeterministicEnumeratorTargetSignatureTest().dimethylCycloButane();
    }

}
