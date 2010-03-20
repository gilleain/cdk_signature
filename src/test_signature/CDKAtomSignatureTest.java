package test_signature;

import org.junit.Test;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.signature.CDKAtomSignature;

public class CDKAtomSignatureTest extends AbstractSignatureTest {
    
    @Test
    public void heightTest() {
        IMolecule benzene = makeBenzene();
        CDKAtomSignature atomSignature = new CDKAtomSignature(0, 2, benzene);
        System.out.println(atomSignature.toCanonicalString());
    }

}
