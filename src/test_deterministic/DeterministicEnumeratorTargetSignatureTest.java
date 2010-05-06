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
    public void degreeTreeHeight2Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add("[C]([C]([C][C])[C]([C][C])[C]([C][C])[H]", 20);
        tms.add("[H]([C]([C][C][C]))", 20);
        String formulaString = "C20H20";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void degreeThreeDodecahedraneTest() {
        IAtomContainer degreeThreeFragment = 
            AbstractDeterministicTest.makeDegreeThreeFragment();
        int count = 20;
        TargetMolecularSignature tms = 
            FragmentConverter.convert(degreeThreeFragment, count);
        System.out.println(tms);
        String formulaString = "C20H20";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void degreeThreeCubaneCuneaneTest() {
        IAtomContainer degreeThreeFragment = 
            AbstractDeterministicTest.makeDegreeThreeFragment();
        int count = 8;
        TargetMolecularSignature tms = 
            FragmentConverter.convert(degreeThreeFragment, count);
        System.out.println(tms);
        String formulaString = "C8H8";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void degreeThreeTetrahedraneTest() {
        IAtomContainer degreeThreeFragment = 
            AbstractDeterministicTest.makeDegreeThreeFragment();
        int count = 4;
        TargetMolecularSignature tms = 
            FragmentConverter.convert(degreeThreeFragment, count);
        System.out.println(tms);
        String formulaString = "C4H4";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void metheneCyclopropeneWithHeight2Sigs() {
        String signatureA = "[C]([C](=[C][C,1])=[C,1]([H])[H])";
        String signatureB = "[C](=[C]([H][H])[C](=[C,1][H])[C,1]([H]))";
        String signatureC = "[C](=[C]([C][C])[H][H])";
        String signatureD = "[H]([C](=[C][H]))";
        String signatureE = "[H]([C]([C]=[C]))";
        
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add(signatureA, 2);
        tms.add(signatureB, 1);
        tms.add(signatureC, 1);
        tms.add(signatureD, 2);
        tms.add(signatureE, 2);
        
        String formulaString = "C4H4";
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
    public void metheneCyclopentaneTest() {
        IAtomContainer methene = AbstractDeterministicTest.makeCH2();
        int count = 5;
        
        TargetMolecularSignature tms = FragmentConverter.convert(methene, count);
        String formulaString = "C5H10";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void metheneCyclohexaneTest() {
        IAtomContainer methene = AbstractDeterministicTest.makeCH2();
        int count = 6;
        
        TargetMolecularSignature tms = FragmentConverter.convert(methene, count);
        String formulaString = "C6H12";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void metheneCycloheptaneTest() {
        IAtomContainer methene = AbstractDeterministicTest.makeCH2();
        int count = 7;
        
        TargetMolecularSignature tms = FragmentConverter.convert(methene, count);
        String formulaString = "C7H14";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void methylCycloPropaneTest() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[C]([C][C][H][H])", 2);
        tms.add("[C]([C][H][H][H])", 1);
        tms.add("[H]([C])", 8);
        String formulaString = "C4H8";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void cycloButane() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][H][H])", 4);
        tms.add("[H]([C])", 8);
        String formulaString = "C4H8";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void dimethylCycloButane() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 2);
        tms.add("[C]([C][C][H][H])", 2);
        tms.add("[C]([C][H][H][H])", 2);
        tms.add("[H]([C])", 12);
        String formulaString = "C6H12";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void mixedCarbonC6H14Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[C]([C][C][H][H])", 2);
        tms.add("[C]([C][H][H][H])", 3);
        tms.add("[H]([C])", 14);
        String formulaString = "C6H14";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void mixedCarbonC7H16Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[C]([C][C][H][H])", 3);
        tms.add("[C]([C][H][H][H])", 3);
        tms.add("[H]([C])", 16);
        String formulaString = "C7H16";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void mixedCarbonC11H22Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][C])", 1);
        tms.add("[C]([C][C][C][H])", 2);
        tms.add("[C]([C][C][H][H])", 4);
        tms.add("[C]([C][H][H][H])", 4);
        tms.add("[H]([C])", 22);
        String formulaString = "C11H22";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }

    
    @Test
    public void twistaneTest() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 4);
        tms.add("[C]([C][C][H][H])", 6);
        tms.add("[H]([C])", 16);
        String formulaString = "C10H16";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void pineneTest() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][C])", 1);    // four carbons
        tms.add("[C]([C]=[C][H])", 1);      // double bond, carbon and hydrogen
        tms.add("[C]([C][C]=[C])", 1);      // double bond, two carbons
        tms.add("[C]([C][C][C][H])", 2);    // CH
        tms.add("[C]([C][C][H][H])", 2);    // CH2
        tms.add("[C]([C][H][H][H])", 3);    // CH3
        tms.add("[H]([C])", 16);
        String formulaString = "C10H16";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    public static void main(String[] args) {
//        new DeterministicEnumeratorTargetSignatureTest().dimethylCycloButane();
        new DeterministicEnumeratorTargetSignatureTest().mixedCarbonC7H16Test();
//        new DeterministicEnumeratorTargetSignatureTest().degreeThreeTest();
    }

}
