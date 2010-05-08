package test_deterministic;

import java.util.List;

import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;

public class AbstractDeterministicTest {
    
    public static SmilesGenerator smilesGenerator = new SmilesGenerator();
    
    public static IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public static String toSmiles(IAtomContainer container) {
        if (ConnectivityChecker.isConnected(container)) {
            return AbstractDeterministicTest.smilesGenerator.createSMILES(
                    AbstractDeterministicTest.builder.newInstance(
                            IMolecule.class, container));
        } else {
            return "disconnected";
        }
    }
    
    public static void printResults(List<IAtomContainer> results) {
        for (IAtomContainer result : results) {
            System.out.println(AbstractDeterministicTest.toSmiles(result));
        }
    }
    
    public static IAtomContainer makeDegreeThreeFragment() {
        // make H(C(CCC))
        IAtomContainer fragment = builder.newInstance(IAtomContainer.class);
        fragment.addAtom(builder.newInstance(IAtom.class, "C"));
        fragment.addAtom(builder.newInstance(IAtom.class, "C"));
        fragment.addAtom(builder.newInstance(IAtom.class, "C"));
        fragment.addAtom(builder.newInstance(IAtom.class, "C"));
        fragment.addAtom(builder.newInstance(IAtom.class, "H"));
        fragment.addBond(0, 1, IBond.Order.SINGLE);
        fragment.addBond(0, 2, IBond.Order.SINGLE);
        fragment.addBond(0, 3, IBond.Order.SINGLE);
        fragment.addBond(0, 4, IBond.Order.SINGLE);
        return fragment;
    }
    
    public static IAtomContainer makeCH2() {
        IAtomContainer fragment = builder.newInstance(IAtomContainer.class);
        fragment.addAtom(builder.newInstance(IAtom.class, "C"));
        fragment.addAtom(builder.newInstance(IAtom.class, "C"));
        fragment.addAtom(builder.newInstance(IAtom.class, "C"));
        fragment.addAtom(builder.newInstance(IAtom.class, "H"));
        fragment.addAtom(builder.newInstance(IAtom.class, "H"));
        fragment.addBond(0, 1, IBond.Order.SINGLE);
        fragment.addBond(0, 2, IBond.Order.SINGLE);
        fragment.addBond(0, 3, IBond.Order.SINGLE);
        fragment.addBond(0, 4, IBond.Order.SINGLE);
        return fragment;
    }

}
