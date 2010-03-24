package test_signature;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.signature.CDKAtomSignature;
import org.openscience.cdk.signature.CDKMoleculeSignature;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;

public class CDKMoleculeSignatureTest {
    
    private SmilesParser parser;
    
    private IChemObjectBuilder builder; 
    
    public CDKMoleculeSignatureTest() {
        this.parser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        this.builder = DefaultChemObjectBuilder.getInstance();
    }
    
    public String canonicalStringFromSmiles(String smiles)
            throws InvalidSmilesException {
        IMolecule mol = parser.parseSmiles(smiles);
        CDKMoleculeSignature signature = new CDKMoleculeSignature(mol);
        return signature.toCanonicalString();
    }
    
    public String canonicalStringFromMolecule(IMolecule molecule) {
        CDKMoleculeSignature signature = new CDKMoleculeSignature(molecule);
//        return signature.toCanonicalString();
        return signature.getGraphSignature();
    }
    
    public String fullStringFromMolecule(IMolecule molecule) {
        CDKMoleculeSignature molSig = new CDKMoleculeSignature(molecule);
        return molSig.toFullString();
    }
    
    public List<String> getAtomicSignatures(IMolecule molecule) {
        CDKMoleculeSignature signature = new CDKMoleculeSignature(molecule);
        return signature.getVertexSignatureStrings();
    }
    
    public void addHydrogens(IMolecule mol, IAtom atom, int n) {
        for (int i = 0; i < n; i++) {
            IAtom h = builder.newAtom("H");
            mol.addAtom(h);
            mol.addBond(builder.newBond(atom, h));
        }
    }
    
    @Test
    public void testEmpty() throws Exception {
       IMolecule mol = DefaultChemObjectBuilder.getInstance().newMolecule(); 
       CDKMoleculeSignature signature = new CDKMoleculeSignature(mol);
       String signatureString = signature.toCanonicalString();
       String expected = "";
       Assert.assertEquals(expected, signatureString);
    }

    
    @Test
    public void testSingleNode() throws Exception {
       String singleChild = "C"; 
       String signatureString = this.canonicalStringFromSmiles(singleChild);
       String expected = "[C]";
       Assert.assertEquals(expected, signatureString);
    }

    
    @Test
    public void testSingleChild() throws Exception {
       String singleChild = "CC"; 
       String signatureString = this.canonicalStringFromSmiles(singleChild);
       String expected = "[C]([C])";
       Assert.assertEquals(expected, signatureString);
    }

    @Test
    public void testMultipleChildren() throws Exception {
       String multipleChildren = "C(C)C"; 
        String signatureString = 
            this.canonicalStringFromSmiles(multipleChildren);
       String expected = "[C]([C]([C]))";
       Assert.assertEquals(expected, signatureString);
    }

    @Test
    public void testThreeCycle() throws Exception {
       String fourCycle = "C1CC1"; 
       String signatureString = this.canonicalStringFromSmiles(fourCycle);
       String expected = "[C]([C,2]([C,1])[C,1])";
       Assert.assertEquals(expected, signatureString);
    }
    
    @Test
    public void testFourCycle() throws Exception {
       String fourCycle = "C1CCC1"; 
       String signatureString = this.canonicalStringFromSmiles(fourCycle);
       String expected = "[C]([C]([C,1])[C]([C,1]))";
       Assert.assertEquals(expected, signatureString);
    }
    
    @Test
    public void testMultipleFourCycles() throws Exception {
       String bridgedRing = "C1C(C2)CC12"; 
       String signatureString = this.canonicalStringFromSmiles(bridgedRing);
       String expected = "[C]([C]([C,1])[C]([C,1])[C]([C,1]))";
       Assert.assertEquals(expected, signatureString);
    }
    
    @Test
    public void testFiveCycle() throws Exception {
       String fiveCycle = "C1CCCC1"; 
       String signatureString = this.canonicalStringFromSmiles(fiveCycle);
       String expected = "[C]([C]([C,2]([C,1]))[C]([C,1]))";
       Assert.assertEquals(expected, signatureString);
    }

    @Test
    public void testMultipleFiveCycles() throws Exception {
       String multipleFiveCycle = "C1C(CC2)CCC12"; 
       String signatureString = 
            this.canonicalStringFromSmiles(multipleFiveCycle);
       String expected = "[C]([C]([C,2]([C,3]))[C]([C,3]([C,1]))[C]([C,1]))";
       Assert.assertEquals(expected, signatureString);
    }
    
    @Test
    public void testCubane() {
        String expected = "[C]([C]([C,4]([C,2])[C,1]([C,2]))" +
        		          "[C]([C,4][C,3]([C,2]))[C]([C,3][C,1]))";
        IMolecule mol = AbstractSignatureTest.makeCubane();
        Assert.assertEquals(expected, this.canonicalStringFromMolecule(mol));
    }
    
    @Test
    public void testCage() {
//        String expectedA =  "[C]([C]([C,2]([C]([C,3][C,4]))[C]([C,5]" +
//                            "[C,3]([C,6]([C,1]))))[C]([C]([C,7][C]" +
//                            "([C,1][C,8]))[C,5]([C,8]([C,6])))[C]([C,2]" +
//                            "[C,7]([C,4]([C,1]))))";
//        String expectedB =  "[C]([C]([C]([C,2][C]([C,1][C,3]))[C]" +
//                            "([C,1]([C,4])[C,5]))[C]([C,2]([C,6]" +
//                            "([C,3]))[C]([C,7][C,6]))[C]([C,5]([C,4]" +
//                            "([C,8]))[C,7]([C,8]([C,3]))))";
        IMolecule mol = AbstractSignatureTest.makeCage();
//        String signature = this.canonicalStringFromMolecule(mol);
//        Assert.assertEquals(expectedB, signature);
//        Assert.assertFalse(expectedA.equals(signature));
        String signature = fullStringFromMolecule(mol);
        System.out.println(signature);
    }
    
    @Test
    public void testPropellane() {
        String expectedA = "[C]([C]([C,1])[C]([C,1])[C]([C,1])[C,1])";
        String expectedB = "[C]([C]([C,1][C,2][C,3])[C,2]([C,1][C,3]))";
        IMolecule mol = AbstractSignatureTest.makePropellane();
        String signature = this.canonicalStringFromMolecule(mol);
        Assert.assertEquals(expectedA, signature);
        Assert.assertFalse(expectedB.equals(signature));
    }
    
    @Test
    public void testBridgedCycloButane() {
        String expected = "[C]([C]([C,1])[C]([C,1])[C,1])";
        IMolecule mol = AbstractSignatureTest.makeBridgedCyclobutane();
        String signature = this.canonicalStringFromMolecule(mol);
        for (String atomicSignature : this.getAtomicSignatures(mol)) {
            System.out.println(atomicSignature);
        }
        Assert.assertEquals(expected, signature);
    }
    
    @Test
    public void tmp() {
        IMolecule mol = AbstractSignatureTest.makeBridgedCyclobutane();
        MDLWriter writer = new MDLWriter(System.out);
        try {
            writer.writeMolecule(mol);
        } catch (Exception e) {
            
        }
    }
    
    @Test
    public void testCageAtVariousHeights() {
        IMolecule cage = AbstractSignatureTest.makeCage();
        CDKMoleculeSignature molSig;
        molSig = new CDKMoleculeSignature(cage, 2);
        System.out.println(molSig.signatureStringForVertex(0, 2));
        molSig = new CDKMoleculeSignature(cage, 3);
        System.out.println(molSig.signatureStringForVertex(0, 3));
    }

    @Test
    public void testCyclohexaneWithHydrogens() {
        IMolecule cyclohexane = MoleculeFactory.makeCyclohexane();
        for (int i = 0; i < 6; i++) {
            addHydrogens(cyclohexane, cyclohexane.getAtom(i), 2);
        }
        String expected = "[C]([C]([C]([C,1]([H][H])[H][H])[H][H])" +
        		          "[C]([C]([C,1][H][H])[H][H])[H][H])";
        
        String actual = this.canonicalStringFromMolecule(cyclohexane);
        Assert.assertEquals(expected, actual);
    }
    
    public void testSmiles(String smiles) {
        try {
            IMolecule molecule = this.parser.parseSmiles(smiles);
            CDKMoleculeSignature sig = new CDKMoleculeSignature(molecule);
            System.out.println(sig.toFullString());
            System.out.println(sig.toCanonicalString());
        } catch (Exception e) {
            
        }
    }
    
    @Test
    public void testCuneane() {
        String cuneaneSmiles = "C1C2C3CC4C1C4C23";
        testSmiles(cuneaneSmiles);
    }
    
    @Test
    public void testPolyPhenylMolecule() {
        // NOTE : currently doesn't work due (possibly) to CDK atom typing
        String smiles = "C1=CC=C(C=C1)P(C2=CC=CC=C2)(C3=CC=CC=C3)[RhH]" +
        		"(P(C4=CC=CC=C4)(C5=CC=CC=C5)C6=CC=CC=C6)(P(C7=CC=CC=C7)" +
        		"(C8=CC=CC=C8)C9=CC=CC=C9)P(C%10=CC=CC=C%10)" +
        		"(C%11=CC=CC=C%11)C%12=CC=CC=C%12";
        testSmiles(smiles);
    }
    
    @Test
    public void cuneaneCubaneHeightTest() {
        IMolecule cuneane = AbstractSignatureTest.makeCuneane();
        IMolecule cubane = AbstractSignatureTest.makeCubane();
        CDKAtomSignature cuneaneSignature = new CDKAtomSignature(0, 2, cuneane);
        CDKAtomSignature cubaneSignature = new CDKAtomSignature(0, 2, cubane);
        String cuneaneSigString = cuneaneSignature.toCanonicalString();
        String cubaneSigString = cubaneSignature.toCanonicalString();
        System.out.println(cuneaneSigString);
        System.out.println(cubaneSigString);
        Assert.assertEquals(cuneaneSigString, cubaneSigString);
    }
}
