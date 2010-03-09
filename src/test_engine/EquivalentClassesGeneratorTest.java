package test_engine;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;

import engine.EquivalentClassesGenerator;

public class EquivalentClassesGeneratorTest {
    
    private IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public void addHydrogens(IAtomContainer ac, IAtom atom, int nH) {
        for (int i = 0; i < nH; i++) {
            IAtom h = builder.newAtom("H");
            ac.addAtom(h);
            ac.addBond(builder.newBond(atom, h, IBond.Order.SINGLE));
        }
    }
    
    public IAtomContainer makeMethyl() {
        IAtomContainer methyl = builder.newAtomContainer();
        IAtom c = builder.newAtom("C");
        methyl.addAtom(c);
        addHydrogens(methyl, c, 3);
        return methyl;
    }
    
    public IAtomContainer makeEthyl() {
        IAtomContainer ethyl = builder.newAtomContainer();
        IAtom c = builder.newAtom("C");
        ethyl.addAtom(c);
        addHydrogens(ethyl, c, 2);
        return ethyl;
    }
    
    public List<IAtomContainer> getFragments() {
        List<IAtomContainer> fragments = new ArrayList<IAtomContainer>();
        fragments.add(makeMethyl());
        fragments.add(makeEthyl());
        fragments.add(makeEthyl());
        fragments.add(makeMethyl());
        return fragments;
    }
    
    public void testBasic() {
        List<IAtomContainer> fragments = getFragments();
        EquivalentClassesGenerator generator = new EquivalentClassesGenerator();
        List<IAtomContainer> results = generator.generate(fragments);
        printResults(results);
    }
    
    public void printResults(List<IAtomContainer> results) {
        SmilesGenerator smilesGenerator = new SmilesGenerator();
        int i = 0;
        for (IAtomContainer result : results) {
            String smiles = smilesGenerator.createSMILES((IMolecule)result);
            System.out.println(i + "\t" + smiles);
            i++;
        }
    }
    
    public static void main(String[] args) {
        new EquivalentClassesGeneratorTest().testBasic();
    }

}
