package app;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.renderer.selection.IChemObjectSelection;

public class BondFlagSelection implements IChemObjectSelection {
    
    private IAtom centralAtom;
    
    private List<IBond> bonds;
    
    private IAtomContainer fragment;
    
    public BondFlagSelection(int atomIndex, IMolecule molecule) {
        if (molecule == null) return;
        centralAtom = molecule.getAtom(atomIndex);
        bonds = new ArrayList<IBond>();
        for (IBond bond : molecule.bonds()) {
            if (bond.contains(centralAtom)) {
                bonds.add(bond);
            }
        }
        fragment = molecule.getBuilder().newInstance(IAtomContainer.class);
        fragment.setAtoms(new IAtom[] {centralAtom});
        fragment.setBonds(bonds.toArray(new IBond[bonds.size()]));
    }

    public boolean contains(IChemObject chemObject) {
        if (centralAtom == null) return false;
        if (chemObject instanceof IAtom) {
            if ((IAtom)chemObject == centralAtom) {
                return true;
            } 
        } else if (chemObject instanceof IBond) {
            if (bonds.contains((IBond) chemObject)) {
                return true;
            }
        }
        return false;
    }

    public <E extends IChemObject> Collection<E> elements(Class<E> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    public IAtomContainer getConnectedAtomContainer() {
        if (fragment != null) {
            return fragment;
        } else {
            return 
            NoNotificationChemObjectBuilder.getInstance().newInstance(
                    IAtomContainer.class);
        }
    }

    public boolean isFilled() {
        return true;
    }

    public void select(IChemModel chemModel) {
        // do nothing
    }

}
