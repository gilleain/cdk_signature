package org.openscience.cdk.deterministic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.graph.PathTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.tools.SaturationChecker;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class Util {
    
    private static SaturationChecker checker;
    
    private static Util instance;
    
    private Util() {
        Util.checker = new SaturationChecker();
    }
    
    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }
    
    /**
     * Check for saturated subgraphs, using only the connected component that
     * contains the atom x. The reasoning is: if the atom x has just been
     * bonded to another atom, it is the only one that can have contributed
     * to a saturated subgraph.
     * 
     * The alternative is that the most recent bond made a complete (connected)
     * and saturated graph - that is, a solution. In that case, the method also
     * returns true, as this is not really a saturated 'sub' graph.
     * 
     * @param x an atom index
     * @param container IAtomContainer
     * @return true if this atom is not part of a saturated subgraph
     */
    public static boolean saturatedSubgraph(
            int atomNumber, IAtomContainer container) {
        IMolecule subGraph = 
            NoNotificationChemObjectBuilder.getInstance().newMolecule();
        List<IAtom> sphere = new ArrayList<IAtom>(); 
        IAtom atomX = container.getAtom(atomNumber);
        sphere.add(atomX);
        atomX.setFlag(CDKConstants.VISITED, true);
        PathTools.breadthFirstSearch(container, sphere, subGraph);
        int saturationCount = 0;
        for (IAtom atom : subGraph.atoms()) {
            atom.setFlag(CDKConstants.VISITED, false);
            if (Util.isSaturated(atom, subGraph)) {
                saturationCount++;
            }
        }
        int atomCount = subGraph.getAtomCount();

        return saturationCount == atomCount && 
                atomCount < container.getAtomCount();
    }
    
    public static boolean isSaturated(IAtom atom, IAtomContainer container) { 
        // TODO (to do properly, that is)
        int totalOrder = 0;
        for (IBond bond : container.getConnectedBondsList(atom)) {
            totalOrder += bond.getOrder().ordinal() + 1;
        }
        if (atom.getSymbol().equals("H") && totalOrder >= 1) {
            return true;
        }
        
        if (atom.getSymbol().equals("C") && totalOrder >= 4) {
            return true;
        }
        
        if (atom.getSymbol().equals("O") && totalOrder >= 2) {
            return true;
        }
        
        if (atom.getSymbol().equals("N") && totalOrder >= 3) {
            return true;
        }
        
        return false;
    }
    
    public static boolean isConnected(IAtomContainer atomContainer) {
        int numberOfAtoms = atomContainer.getAtomCount();
        int numberOfBonds = atomContainer.getBondCount();
    
        // n atoms connected into a simple chain have (n - 1) bonds
        return numberOfBonds >= (numberOfAtoms - 1) 
        && ConnectivityChecker.isConnected(atomContainer);
    }
    
    public static IAtomContainer makeAtomContainerFromFormulaString(String f) {
        IChemObjectBuilder builder = 
            NoNotificationChemObjectBuilder.getInstance();
        IAtomContainer atomContainer = builder.newAtomContainer();
        IMolecularFormula formula = 
            MolecularFormulaManipulator.getMolecularFormula(f, builder);
        ArrayList<IAtom> atoms = new ArrayList<IAtom>();
        for (IIsotope isotope : formula.isotopes()) {
            for (int i = 0; i < formula.getIsotopeCount(isotope); i++) {
                atoms.add(builder.newAtom(isotope));
            }
        }
        
        // sort by symbol lexicographic order
        Collections.sort(atoms, new Comparator<IAtom>() {

            public int compare(IAtom o1, IAtom o2) {
                return o1.getSymbol().compareTo(o2.getSymbol());
            }
            
        });
        atomContainer.setAtoms(atoms.toArray(new IAtom[]{}));
        return atomContainer;
    }
    
    public static SaturationChecker getChecker() {
        return Util.checker;
    }
    
    

}
