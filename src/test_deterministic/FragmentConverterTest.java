package test_deterministic;

import java.util.Arrays;

import org.junit.Test;
import org.openscience.cdk.deterministic.FragmentConverter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.TargetMolecularSignature;

public class FragmentConverterTest extends AbstractDeterministicTest {
    
    public static IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    @Test
    public void threeRegularGraphTest() {
        // make H(C(CCC))
        IAtomContainer fragment = AbstractDeterministicTest.makeDegreeThreeFragment();
        int count = 8;
        
        TargetMolecularSignature tms = 
            FragmentConverter.convert(fragment, count);
        System.out.println(tms + "\n" 
                + Arrays.deepToString(tms.getLookupTable())
                + "\t" + tms.getSignatures());
    }

}
