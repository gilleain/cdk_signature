package test_deterministic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.openscience.cdk.deterministic.FragmentConverter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.TargetMolecularSignature;

public class FragmentConverterTest {
    
    public static IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    @Test
    public void threeRegularGraphTest() {
        // make H(C(CCC))
        IAtomContainer fragment = builder.newAtomContainer();
        fragment.addAtom(builder.newAtom("C"));
        fragment.addAtom(builder.newAtom("C"));
        fragment.addAtom(builder.newAtom("C"));
        fragment.addAtom(builder.newAtom("C"));
        fragment.addAtom(builder.newAtom("H"));
        fragment.addBond(0, 1, IBond.Order.SINGLE);
        fragment.addBond(0, 2, IBond.Order.SINGLE);
        fragment.addBond(0, 3, IBond.Order.SINGLE);
        fragment.addBond(0, 4, IBond.Order.SINGLE);
        
        List<IAtomContainer> fragments = new ArrayList<IAtomContainer>();
        fragments.add(fragment);
        
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(8);
        
        TargetMolecularSignature tms = 
            FragmentConverter.convert(fragments, counts);
        System.out.println(tms + " " + tms.compatibleTargetBonds(0, 1));
    }

}
