package engine;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.config.AtomTypeFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.BondManipulator;

public class SaturationCalculator {
    
    private AtomTypeFactory atf;
    
    public SaturationCalculator() {
        atf = AtomTypeFactory.getInstance(
                NoNotificationChemObjectBuilder.getInstance());
    }
    
    
    public int calculateUnsaturation(IAtom atom, IAtomContainer container) {
        IAtomType[] atomTypes = atf.getAtomTypes(atom.getSymbol());
        double bondOrderSum = container.getBondOrderSum(atom);
        IBond.Order maxBondOrder = container.getMaximumBondOrder(atom);
        Integer hcount = 
            atom.getHydrogenCount() == CDKConstants.UNSET? 
                    0 : atom.getHydrogenCount();
        Integer charge = 
            atom.getFormalCharge() == CDKConstants.UNSET? 
                    0 : atom.getFormalCharge();
        for (IAtomType atomType : atomTypes) {
            boolean higherOrder = BondManipulator.isHigherOrder(
                    maxBondOrder, atomType.getMaxBondOrder());
            if (bondOrderSum - charge + hcount == atomType.getBondOrderSum()
                    && !higherOrder) {
                return 0;
            }
        }

        return 1;
    }

}
