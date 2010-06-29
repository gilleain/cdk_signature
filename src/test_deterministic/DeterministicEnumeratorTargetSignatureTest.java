package test_deterministic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.signature.MoleculeSignature;
import org.openscience.cdk.signature.Orbit;
import org.openscience.cdk.structgen.deterministic.DeterministicEnumerator;
import org.openscience.cdk.structgen.deterministic.FragmentConverter;
import org.openscience.cdk.structgen.deterministic.TargetMolecularSignature;

public class DeterministicEnumeratorTargetSignatureTest extends 
                AbstractDeterministicTest {
    
    public void run(TargetMolecularSignature tms, String formulaString) {
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void adenine() {
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
       
        tms.add("[C](=[C][N][N]", 1);
        tms.add("[C]([C]=[C][N])", 1);
        tms.add("[C]([C][N]=[N]", 1);
        tms.add("[C]([H][N]=[N])", 2);
        tms.add("[H]([C])", 2);
        tms.add("[H]([N])", 3);
        tms.add("[N]([C]=[C])", 3);
        tms.add("[N]([C][C][H])", 1);
        tms.add("[N]([C][H][H]", 1);
        
        IMolecule mol = builder.newInstance(IMolecule.class);
        mol.addAtom(builder.newInstance(IAtom.class, "C"));
        mol.addAtom(builder.newInstance(IAtom.class, "N"));
        mol.addAtom(builder.newInstance(IAtom.class, "C"));
        mol.addAtom(builder.newInstance(IAtom.class, "C"));
        mol.addAtom(builder.newInstance(IAtom.class, "N"));
        mol.addAtom(builder.newInstance(IAtom.class, "N"));
        mol.addAtom(builder.newInstance(IAtom.class, "C"));
        mol.addAtom(builder.newInstance(IAtom.class, "N"));
        mol.addAtom(builder.newInstance(IAtom.class, "C"));
        mol.addAtom(builder.newInstance(IAtom.class, "N"));
        mol.addAtom(builder.newInstance(IAtom.class, "H"));
        mol.addAtom(builder.newInstance(IAtom.class, "H"));
        mol.addAtom(builder.newInstance(IAtom.class, "H"));
        mol.addAtom(builder.newInstance(IAtom.class, "H"));
        mol.addAtom(builder.newInstance(IAtom.class, "H"));
        
        mol.addBond(0, 1, IBond.Order.DOUBLE);
        mol.addBond(0, 9, IBond.Order.SINGLE);
        mol.addBond(0, 10, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(2, 8, IBond.Order.DOUBLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(3, 5, IBond.Order.DOUBLE);
        mol.addBond(4, 11, IBond.Order.SINGLE);
        mol.addBond(4, 12, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(6, 7, IBond.Order.DOUBLE);
        mol.addBond(6, 13, IBond.Order.SINGLE);
        mol.addBond(7, 8, IBond.Order.SINGLE);
        mol.addBond(8, 9, IBond.Order.SINGLE);
        mol.addBond(9, 14, IBond.Order.SINGLE);
        
        MoleculeSignature sig = new MoleculeSignature(mol);
        Map<String, Integer> orbs = new HashMap<String, Integer>();
        for (int x = 0; x < mol.getAtomCount(); x++) {
            String ss = sig.signatureStringForVertex(x, 1);
            if (orbs.containsKey(ss)) {
                orbs.put(ss, orbs.get(ss) + 1);
            } else {
                orbs.put(ss, 1);
            }
        }
        List<String> keys = new ArrayList<String>(orbs.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            System.out.println(key + " " + orbs.get(key));
        }
        
        String formulaString = "C5H5N5";
        run(tms, formulaString);
    }
    
    @Test
    public void multipleBondedRingHeight2Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        String a = "[C](=[C]([C][H])[C](=[C][H])[H])"; 
        String b = "[C]([C]([C]=[C])=[C]([C][H])[H])";
        String c = "[C](=[C]([C][C])[C](=[C][H])[C](=[C][H]))";
        String h = "[H]([C]([C]=[C]))";
        
        tms.add(a, 4);
        tms.add(b, 4);
        tms.add(c, 4);
        tms.add(h, 8);
        String formulaString = "C10H8";
//        System.out.println(java.util.Arrays.deepToString(tms.getLookupTable()));
        run(tms, formulaString);
    }
    
    @Test
    public void multipleBondedRingHeight1Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C]=[C])", 4);
        tms.add("[C]([C]=[C][H])", 10);
        tms.add("[H]([C])", 10);
        String formulaString = "C14H10";
        run(tms, formulaString);
    }
    
    @Test
    public void cubaneHeight2Test() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        for (int i = 0; i < 8; i++) { mol.addAtom(builder.newInstance(IAtom.class,"C")); }
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(0, 7, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(1, 6, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE); 
        mol.addBond(2, 5, IBond.Order.SINGLE); 
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(4, 7, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(6, 7, IBond.Order.SINGLE);
        for (int i = 0; i < 8; i++) { 
            mol.addAtom(builder.newInstance(IAtom.class,"H"));
            mol.addBond(i, 8 + i, IBond.Order.SINGLE);
            System.out.println("bonding " + i + " and " + 2 * (i + 1));
        }
//        System.out.println(AbstractDeterministicTest.toSmiles(mol));
        
        MoleculeSignature molSig = new MoleculeSignature(mol);
        String height2CSig = molSig.signatureStringForVertex(0, 2);
        String height2HSig = molSig.signatureStringForVertex(8, 2);
        System.out.println(height2CSig);
        System.out.println(height2HSig);
        
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add(height2CSig, 8);
        tms.add(height2HSig, 8);
        String formulaString = "C8H8";
        DeterministicEnumerator enumerator = 
            new DeterministicEnumerator(formulaString, tms);
        List<IAtomContainer> results = enumerator.generate();
        AbstractDeterministicTest.printResults(results);
    }
    
    @Test
    public void degreeThreeHeight2Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add("[C]([C]([C][C][H])[C]([C][C][H])[C]([C][C][H])[H])", 12);
        tms.add("[H]([C]([C][C][C]))", 12);
        String formulaString = "C12H12";
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
    public void methylCycloButane() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[C]([C][C][H][H])", 3);
        tms.add("[C]([C][H][H][H])", 1);
        tms.add("[H]([C])", 10);
        String formulaString = "C5H10";
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
    public void trimethylCyclopropane() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 3);
        tms.add("[C]([C][H][H][H])", 3);
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
        new DeterministicEnumeratorTargetSignatureTest().methylCycloButane();
//        new DeterministicEnumeratorTargetSignatureTest().dimethylCycloButane();
//        new DeterministicEnumeratorTargetSignatureTest().mixedCarbonC7H16Test();
//        new DeterministicEnumeratorTargetSignatureTest().degreeThreeTest();
    }

}
