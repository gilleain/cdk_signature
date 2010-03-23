package test_signature;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
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
    public void allHeightsOfASymmetricGraphAreEqual() {
        IMolecule cubane = makeCubane();
        int h = 1;
        Map<String, Integer> sigfreq = new HashMap<String, Integer>();
        for (int i = 0; i < cubane.getAtomCount(); i++) {
            CDKAtomSignature atomSignature = new CDKAtomSignature(i, h, cubane);
            String canonicalSignature = atomSignature.toCanonicalString();
            if (sigfreq.containsKey(canonicalSignature)) {
                sigfreq.put(canonicalSignature, sigfreq.get(canonicalSignature) + 1);
            } else {
                sigfreq.put(canonicalSignature, 1);
            }
            System.out.println(i + " " + canonicalSignature);
        }
        for (String key : sigfreq.keySet()) {
            System.out.println(key + " " + sigfreq.get(key));
        }
    }
    
    @Test
    public void testNonZeroRootForSubsignature() {
        IMolecule cubane = makeCubane();
        CDKAtomSignature atomSignature = new CDKAtomSignature(1, 2, cubane);
        String canonicalSignature = atomSignature.toCanonicalString();
        System.out.println(canonicalSignature);
    }

}
