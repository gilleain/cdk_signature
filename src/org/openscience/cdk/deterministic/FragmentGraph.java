package org.openscience.cdk.deterministic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;

/**
 * Molecule graph used by the DeterministicEnumerator. Keeps track of the
 * elements or molecular fragments that are being used to construct the set of
 * fragment graphs.
 * 
 * @author maclean
 *
 */
public class FragmentGraph {
    
    /**
     * The growing molecule, composed of fragments that have already been added
     */
    private IAtomContainer atomContainer;
    
    /**
     * Each atom that has been added will have a label
     */
    private List<String> labels;
    
    /**
     * Unused labels
     */
    private Map<String, Fragment> fragments;
    
    /**
     * Makes an initial fragment graph from the list of strings and counts.
     * 
     * @param fragmentCounts
     */
    public FragmentGraph(
            Map<String, Integer> fragmentCounts, 
            IChemObjectBuilder builder,
            String initialLabel) {
        this.atomContainer = builder.newAtomContainer();
        this.labels = new ArrayList<String>();
        
        this.fragments = new HashMap<String, Fragment>();
        boolean firstLabel = true;
        for (String label : fragmentCounts.keySet()) {
            int count = fragmentCounts.get(label);
            
            // TODO : allow more than just element fragments...
            if (firstLabel && label.equals(initialLabel)) {
                this.fragments.put(label, new ElementFragment(count - 1, label));
                this.atomContainer.addAtom(builder.newAtom(label));
                this.labels.add(label);
                firstLabel = false;
            } else {
                this.fragments.put(label, new ElementFragment(count, label));
            }
        }
    }
    
    /**
     * Copy constructor.
     * 
     * @param other the parent fragment graph
     */
    public FragmentGraph(FragmentGraph other) {
        try {
            this.atomContainer = (IAtomContainer) other.atomContainer.clone();
            this.fragments = new HashMap<String, Fragment>();
            for (String label : other.fragments.keySet()) {
                Fragment fragment = other.fragments.get(label);
                this.fragments.put(label, fragment.copy());
            }
            this.labels = new ArrayList<String>();
            for (String label : other.labels) {
                this.labels.add(label);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get the underlying atom container, whether complete or not.
     * 
     * @return a reference to the atom container
     */
    public IAtomContainer getAtomContainer() {
        return this.atomContainer;
    }
    
    /**
     * Make a new single bond, taking one atom from the already created atoms, 
     * and making the other atom by asking a fragment for it.
     * 
     * @param fragmentLabel the label of the fragment that will make the atom
     * @param atomIndex the index of the existing atom to use
     * @param builder the chem object builder that will construct the atom
     */
    public void bond(String fragmentLabel, int atomIndex, IChemObjectBuilder builder) {
        Fragment fragment = this.fragments.get(fragmentLabel);
        IAtom fragmentAtom = fragment.makeAtom(builder);
        IAtom existingAtom = this.atomContainer.getAtom(atomIndex);
        this.atomContainer.addAtom(fragmentAtom);
        this.labels.add(fragmentLabel);
        this.atomContainer.addBond(builder.newBond(existingAtom, fragmentAtom));
    }
    
    /**
     * Make a bond between two existing atoms in the atom container, which must
     * therefore be the close of a cycle.
     * 
     * @param atomIndex1 the index of the first atom in the bond
     * @param atomIndex2 the index of the second atom in the bond
     */
    public void bond(int atomIndex1, int atomIndex2, IChemObjectBuilder builder) {
        IAtom a = this.atomContainer.getAtom(atomIndex1);
        IAtom b = this.atomContainer.getAtom(atomIndex2);
        IBond existingBond = this.atomContainer.getBond(a, b);
        if (existingBond == null) {
            this.atomContainer.addBond(builder.newBond(a, b, IBond.Order.SINGLE));
        } else {
            IBond.Order o = existingBond.getOrder();
            if (o == IBond.Order.SINGLE) {
                existingBond.setOrder(IBond.Order.DOUBLE);
            } else {
                existingBond.setOrder(IBond.Order.TRIPLE);
            }
            // TODO : what if already triple?
        }
    }
    
    public boolean canIncreaseBondOrder(int atomIndex1, int atomIndex2) {
        IAtom a = this.atomContainer.getAtom(atomIndex1);
        IAtom b = this.atomContainer.getAtom(atomIndex2);
        IBond existingBond = this.atomContainer.getBond(a, b);
        if (existingBond == null) {
            return true;    // XXX ? 
        } else {
            return existingBond.getOrder() != IBond.Order.TRIPLE;
        }
    }
    
    /**
     * Searches for the first atom index that has this label.
     * 
     * @param labelToSearchFor the string label to search for
     * @return the index of the first atom with this label, or -1 if none found
     */
    public int getAtomIndexForLabel(String labelToSearchFor) {
        for (int i = 0; i < this.labels.size(); i++) {
            String label = this.labels.get(i);
            if (label.equals(labelToSearchFor) && !isSaturated(i)) {
                return i;
            }
        }
        return -1;
    }
    
    public List<String> getUniqueLabels() {
        // inefficient, and potentially broken - just testing something
        List<String> uniqueLabels = new ArrayList<String>();
        for (int i = 0; i < labels.size(); i++) {
            String label = labels.get(i);
            if (uniqueLabels.contains(label) || isSaturated(i)) {
                continue;
            } else {
                uniqueLabels.add(label);
            }
        }
        return uniqueLabels;
    }

    /**
     * Gets the next label to try to saturate.
     * 
     * @return
     */
    public String getNextUnsaturatedLabel() {
        for (int i = 0; i < labels.size(); i++) {
            if (isSaturated(i)) {
                continue;
            } else {
                return labels.get(i);
            }
        }
        return null;
    }

    /**
     * Get a list of labels that have not yet been used.
     * 
     * @return
     */
    public List<String> unusedLabels() {
        List<String> labels = new ArrayList<String>();
        for (String label : fragments.keySet()) {
            if (fragments.get(label).isEmpty()) {
                continue;
            } else {
                labels.add(label);
            }
        }
        return labels;
    }
    
    /**
     * Get the list of atoms to be saturated.
     * 
     * @return a list of atom indices
     */
    public List<Integer> unsaturatedAtoms() {
        List<Integer> unsaturated = new ArrayList<Integer>();
        for (int i = 0; i < this.atomContainer.getAtomCount(); i++) {
            if (isSaturated(i)) {
                continue;
            } else {
                unsaturated.add(i);
            }
        }
        return unsaturated;
    }
    
    /**
     * Test every atom for saturation to see if the whole container is saturated.
     *  
     * @return true if every atom has the maximum number of bonds
     */
    public boolean isFullySaturated() {
        for (int i = 0; i < this.atomContainer.getAtomCount(); i++) {
            if (isSaturated(i)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check this atom for saturation.
     * 
     * @param atomNumber the atom to check
     * @return true if this atom is saturated
     */
    public boolean isSaturated(int atomNumber) {
        IAtom atom = this.atomContainer.getAtom(atomNumber);
        return Util.isSaturated(atom, atomContainer);
    }
    
    /**
     * Check that the graph is connected.
     * 
     * @return true if there is a path from any atom to any other atom
     */
    public boolean isConnected() {
        // XXX TODO : is this necessary, given the construction procedure?
        return ConnectivityChecker.isConnected(atomContainer);
    }
    
    /**
     * Determines if all the fragments have been used up - that is, their counts
     * are all zero.
     * 
     * @return
     */
    public boolean allUsed() {
        for (String label : fragments.keySet()) {
            if (fragments.get(label).isEmpty()) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
   
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (IAtom atom : this.atomContainer.atoms()) {
            sb.append(atom.getSymbol()).append(i);
            i++;
        }
        sb.append(" { ");
        for (IBond bond : this.atomContainer.bonds()) {
            int l = this.atomContainer.getAtomNumber(bond.getAtom(0));
            int r = this.atomContainer.getAtomNumber(bond.getAtom(1));
            if (l < r) {
                sb.append(l).append("-").append(r);
            } else {
                sb.append(r).append("-").append(l);
            }
            int o = bond.getOrder().ordinal() + 1;
            sb.append("(").append(o).append(") ");
        }
        sb.append("} ");
        for (String label : labels) { sb.append(label); }
        sb.append(" ");
        for (String label : fragments.keySet()) { sb.append(fragments.get(label)); }
        return sb.toString();
    }

}
