package test_signature;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.signature.CDKAtomSignature;

public class CDKAtomSignatureTest extends AbstractSignatureTest {
    
    @Test
    public void heightTest() {
        IMolecule benzene = makeBenzene();
        CDKAtomSignature atomSignature = new CDKAtomSignature(0, 1, benzene);
        System.out.println(atomSignature.toCanonicalString());
    }
    
    @Test
    public void cubaneHeightTest() {
        IMolecule cubane = AbstractSignatureTest.makeCubane();
        moleculeIsCarbon3Regular(cubane);
        int height = 3;
        CDKAtomSignature atomSignature = new CDKAtomSignature(0, height, cubane);
        String canonicalString = atomSignature.toCanonicalString();
        System.out.println(canonicalString);
    }
    
    @Test
    public void cuneaneCubaneHeightTest() {
        IMolecule cuneane = AbstractSignatureTest.makeCuneane();
        IMolecule cubane = AbstractSignatureTest.makeCubane();
        int height = 1;
        CDKAtomSignature cuneaneSignature = new CDKAtomSignature(0, height, cuneane);
        CDKAtomSignature cubaneSignature = new CDKAtomSignature(0, height, cubane);
        String cuneaneSigString = cuneaneSignature.toCanonicalString();
        String cubaneSigString = cubaneSignature.toCanonicalString();
        System.out.println(cuneaneSigString);
        System.out.println(cubaneSigString);
        Assert.assertEquals(cuneaneSigString, cubaneSigString);
    }
    
    public void moleculeIsCarbon3Regular(IMolecule molecule) {
        int i = 0;
        for (IAtom a : molecule.atoms()) {
            int count = 0;
            for (IAtom connected : molecule.getConnectedAtomsList(a)) {
                if (connected.getSymbol().equals("C")) {
                    count++;
                }
            }
            Assert.assertEquals("Failed for atom " + i, 3, count);
            i++;
        }
    }
    
    @Test
    public void dodecahedraneHeightTest() {
        IMolecule dodecahedrane = AbstractSignatureTest.makeDodecahedrane();
        moleculeIsCarbon3Regular(dodecahedrane);
        int diameter = 5;
        for (int height = 0; height <= diameter; height++) {
            allEqualAtHeightTest(dodecahedrane, height);
        }
    }
    
    @Test
    public void allHeightsOfASymmetricGraphAreEqualTest() {
        IMolecule cubane = makeCubane();
        int diameter = 3;
        for (int height = 0; height <= diameter; height++) {
            allEqualAtHeightTest(cubane, height);
        }
    }
    
    public void allEqualAtHeightTest(IMolecule molecule, int height) {
        Map<String, Integer> sigfreq = new HashMap<String, Integer>();
        for (int i = 0; i < molecule.getAtomCount(); i++) {
            CDKAtomSignature atomSignature = new CDKAtomSignature(i, height, molecule);
            String canonicalSignature = atomSignature.toCanonicalString();
            if (sigfreq.containsKey(canonicalSignature)) {
                sigfreq.put(canonicalSignature, sigfreq.get(canonicalSignature) + 1);
            } else {
                sigfreq.put(canonicalSignature, 1);
            }
//            System.out.println(i + " " + canonicalSignature);
        }
//        for (String key : sigfreq.keySet()) { System.out.println(key + " " + sigfreq.get(key));}
        Assert.assertEquals(1, sigfreq.keySet().size());
    }
    
    @Test
    public void testNonZeroRootForSubsignature() {
        IMolecule cubane = makeCubane();
        CDKAtomSignature atomSignature = new CDKAtomSignature(1, 2, cubane);
        String canonicalSignature = atomSignature.toCanonicalString();
        System.out.println(canonicalSignature);
    }

}
