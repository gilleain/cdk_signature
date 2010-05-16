package test_deterministic;

import junit.framework.Assert;

import org.junit.Test;

import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.structgen.deterministic.TargetMolecularSignature;

import test_signature.AbstractSignatureTest;

public class TargetMolecularSignatureTest extends AbstractSignatureTest {
    
    /**
     * Make a simple signature compatible with hexane.
     * 
     * @return a sample molecular signature
     */
    public static TargetMolecularSignature makeHexaneSignature() {
        TargetMolecularSignature sig = new TargetMolecularSignature(5);
        sig.add("[C]([C]([C]([C]([C]([C])))))", 2);
        sig.add("[C]([C][C]([C]([C]([C]))))", 2);
        sig.add("[C]([C]([C]([C]))[C]([C]))", 2);
        return sig;
    }
    
    /**
     * Make a height-2 signature compatible with (at least) adenine.
     * 
     * @return a height-2 signature compatible with adenine
     */
    public static TargetMolecularSignature makeAdenineSignature() {
        TargetMolecularSignature sig = new TargetMolecularSignature(2);
        sig.add("[C]([N][N])",  3);
        sig.add("[C]([N][N][C])", 2);
        sig.add("[N]([C][C])",  3);
        sig.add("[N]([C][C][C])", 1);
        sig.add("[N]([C])",   1);
        return sig;
    }
    
    public static TargetMolecularSignature makeCuneaneSignature() {
        TargetMolecularSignature sig = new TargetMolecularSignature(3);
        sig.add("[C]([C]([C,2]([C,3])[C,3]([C,1]))" +
                "[C]([C,2][C,4]([C,1]))[C]([C,1][C,4]))", 2, "A");
        sig.add("[C]([C]([C,1][C]([C,2][C,3]))[C]([C,4]" +
                "([C,3])[C,2]([C,3]))[C,1]([C,4]))", 4, "B");
        sig.add("[C]([C]([C]([C,1][C,2])[C,2]([C,3]))" +
                "[C]([C,4][C,1]([C,3]))[C,4]([C,3]))", 2, "C");
        return sig;
    }
    
    /**
     * Make the example molecular signature given in the signature enumeration
     * paper.
     * 
     * @return a sample molecular signature
     */
    public static TargetMolecularSignature makePaperExampleMolecularSignature() {
        TargetMolecularSignature sig = new TargetMolecularSignature(2);
        sig.add("[H]([C]([H][H][C]))", 9, "h3");
        sig.add("[H]([C]([H][C][C]))", 12, "h2");
        sig.add("[H]([C]([C][C][C]))", 1, "h1");
        sig.add("[C]([H][H][H][C]([H][C][C]))", 1, "c31");
        sig.add("[C]([H][H][H][C]([H][H][C]))", 2, "c32");
        sig.add("[C]([H][H][C]([H][H][H])[C]([H][H][C]))", 2, "c232");
        sig.add("[C]([H][H][C]([H][H][C])[C]([H][H][C]))", 2, "c222");
        sig.add("[C]([H][H][C]([H][H][C])[C]([H]CC))", 2, "c221");
        
        // NOTE : why is this called '1322' in the paper, when all the
        // others follow the naming pattern of number of hydrogens? 
        sig.add("[C]([H][C]([H][H][H])[C]([H][H][C])[C]([H][C][C]))", 1, "c1322");

        return sig;
    }
    
    public static void printTable(TargetMolecularSignature tms) {
        int[][] lookupTable = tms.getLookupTable();
        int n = tms.size();
        System.out.print("  ");
        for (int i = 0; i < n; i++) {
            System.out.print(i + " ");
        }
        System.out.print("\n");
        for (int i = 0; i < n; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < n; j++) {
               System.out.print(lookupTable[i][j]);
               System.out.print(" ");
            }
            System.out.print("\n");
        }
    }
    
    @Test
    public void methyleneCyclopropeneTest() {
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
        
        printTable(tms);
        Assert.assertEquals(1, tms.compatibleTargetBonds(0, 0)); // A->A
        Assert.assertEquals(2, tms.compatibleTargetBonds(0, 1)); // A->B
        Assert.assertEquals(0, tms.compatibleTargetBonds(0, 2)); // A->C
        Assert.assertEquals(0, tms.compatibleTargetBonds(0, 3)); // A->D
        Assert.assertEquals(1, tms.compatibleTargetBonds(0, 4)); // A->E
        
        Assert.assertEquals(1, tms.compatibleTargetBonds(1, 0)); // B->A
        Assert.assertEquals(0, tms.compatibleTargetBonds(1, 1)); // B->B
        Assert.assertEquals(1, tms.compatibleTargetBonds(1, 2)); // B->C
        Assert.assertEquals(0, tms.compatibleTargetBonds(1, 3)); // B->D
        Assert.assertEquals(0, tms.compatibleTargetBonds(1, 4)); // B->E
        
        Assert.assertEquals(0, tms.compatibleTargetBonds(2, 0)); // C->A
        Assert.assertEquals(1, tms.compatibleTargetBonds(2, 1)); // C->B
        Assert.assertEquals(0, tms.compatibleTargetBonds(2, 2)); // C->C
        Assert.assertEquals(1, tms.compatibleTargetBonds(2, 3)); // C->D
        Assert.assertEquals(0, tms.compatibleTargetBonds(2, 4)); // C->E
        
        Assert.assertEquals(1, tms.compatibleTargetBonds(3, 0)); // D->A
        Assert.assertEquals(0, tms.compatibleTargetBonds(3, 1)); // D->B
        Assert.assertEquals(2, tms.compatibleTargetBonds(3, 2)); // D->C
        Assert.assertEquals(0, tms.compatibleTargetBonds(3, 3)); // D->D
        Assert.assertEquals(0, tms.compatibleTargetBonds(3, 4)); // D->E
        
        Assert.assertEquals(1, tms.compatibleTargetBonds(4, 0)); // E->A
        Assert.assertEquals(0, tms.compatibleTargetBonds(4, 1)); // E->B
        Assert.assertEquals(2, tms.compatibleTargetBonds(4, 2)); // E->C
        Assert.assertEquals(0, tms.compatibleTargetBonds(4, 3)); // E->D
        Assert.assertEquals(0, tms.compatibleTargetBonds(4, 4)); // E->E
    }
    
    @Test
    public void targetAtomicSubsignatureTest() {
        TargetMolecularSignature tms = 
            TargetMolecularSignatureTest.makeHexaneSignature();
        for (int h = 0; h < 2; h++) {
            for (int j = 0; j < 3; j++) {
                String subsignature = tms.getTargetAtomicSubSignature(j, h);
                System.out.println("h " + h + " sig " + j + " " + subsignature);
            }
        }
    }
    
    @Test
    public void roundtrip() {
        TargetMolecularSignature tms = makePaperExampleMolecularSignature();
        System.out.println(tms);
    }
    
    @Test
    public void compatibleBondsForHexane() {
        TargetMolecularSignature tms = 
            TargetMolecularSignatureTest.makeHexaneSignature();
        Assert.assertEquals(0, tms.compatibleTargetBonds(0, 0)); // A->A
        Assert.assertEquals(1, tms.compatibleTargetBonds(0, 1)); // A->B
        Assert.assertEquals(0, tms.compatibleTargetBonds(0, 2)); // A->C
        
        Assert.assertEquals(1, tms.compatibleTargetBonds(1, 0)); // B->A
        Assert.assertEquals(0, tms.compatibleTargetBonds(1, 1)); // B->B
        Assert.assertEquals(1, tms.compatibleTargetBonds(1, 2)); // B->C
        
        Assert.assertEquals(0, tms.compatibleTargetBonds(2, 0)); // C->A
        Assert.assertEquals(1, tms.compatibleTargetBonds(2, 1)); // C->B
        Assert.assertEquals(1, tms.compatibleTargetBonds(2, 2)); // C->C
    }
    
    @Test
    public void compatibleBondsForCuneaneExample() {
        TargetMolecularSignature tms = 
            TargetMolecularSignatureTest.makeCuneaneSignature();
        Assert.assertEquals(1, tms.compatibleTargetBonds(0, 0)); // A->A
        Assert.assertEquals(1, tms.compatibleTargetBonds(0, 1)); // A->B
        Assert.assertEquals(0, tms.compatibleTargetBonds(0, 2)); // A->C
        
        Assert.assertEquals(2, tms.compatibleTargetBonds(1, 0)); // B->A
        Assert.assertEquals(1, tms.compatibleTargetBonds(1, 1)); // B->B
        Assert.assertEquals(2, tms.compatibleTargetBonds(1, 2)); // B->C
        
        Assert.assertEquals(0, tms.compatibleTargetBonds(2, 0)); // C->A
        Assert.assertEquals(1, tms.compatibleTargetBonds(2, 1)); // C->B
        Assert.assertEquals(1, tms.compatibleTargetBonds(2, 2)); // C->C
    }
    
    @Test
    public void matchingC7H16IsomersTest() {
        IMolecule c7H16A = AbstractSignatureTest.makeC7H16A();
        IMolecule c7H16B = AbstractSignatureTest.makeC7H16B();
        IMolecule c7H16C = AbstractSignatureTest.makeC7H16C();
        
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add("[C]([C][H][H][H])", 3, "CH3");
        tms.add("[C]([C][C][H][H])", 3, "CH2");
        tms.add("[C]([C][C][C][H])", 1, "CH");
        tms.add("[H]([C])", 16, "H");
        
        System.out.println(tms.matches(c7H16A));
        System.out.println(tms.matches(c7H16B));
        System.out.println(tms.matches(c7H16C));
    }
    
}
