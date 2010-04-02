package org.openscience.cdk.deterministic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.signature.CDKMoleculeSignature;
import org.openscience.cdk.signature.Orbit;

public class SimpleGraph {


    /**
     * The fragments (single atoms or groups) that have not yet been connected
     */
//    private Map<String, Integer> fragmentCounts;
    
    
//    private Map<Orbit, IAtomContainer> orbitFragmentMap;
    
    /**
     * The growing molecule, composed of fragments that have already been added
     */
    private IAtomContainer atomContainer;
    
    public SimpleGraph(Map<String, Integer> fragmentCounts) {
//        this.fragmentCounts = fragmentCounts;
    }

    /**
     * Wrap an atom container in a graph, to manage the fragments
     * 
     * @param atomContainer the underlying atom container
     */
    public SimpleGraph(IAtomContainer atomContainer) { // TODO : remove
        this.atomContainer = atomContainer;
    }

    /**
     * Copy constructor
     * 
     * @param g the graph to copy
     */
    public SimpleGraph(SimpleGraph g) {
        // For now, clone the whole atom container, to make sure.
        // In theory, it might be possible to just copy over atom references
        // and clone the bonds
        try {
            this.atomContainer = (IAtomContainer) g.atomContainer.clone();
//            this.fragmentCounts = new HashMap<String, Integer>();
//            for (String fragmentString : g.fragmentCounts.keySet()) {
//                int count = g.fragmentCounts.get(fragmentString);
//                this.fragmentCounts.put(fragmentString, count);
//            }
        } catch (CloneNotSupportedException c) {

        }
    }
    
    /**
     * Add a bond between these two atoms.
     * 
     * @param x the first atom to be bonded
     * @param y the second atom to be bonded
     */
    public void bond(int x, int y) {
        IAtom a = atomContainer.getAtom(x);
        IAtom b = atomContainer.getAtom(y);
        System.out.println(
                String.format("bonding %d and %d (%s-%s)",
                        x, y, a.getSymbol(),b.getSymbol()));
        IBond existingBond = this.atomContainer.getBond(a, b);
        if (existingBond != null) {
            IBond.Order o = existingBond.getOrder(); 
            if (o == IBond.Order.SINGLE) {
                existingBond.setOrder(IBond.Order.DOUBLE);
            } else if (o == IBond.Order.DOUBLE) {
                existingBond.setOrder(IBond.Order.TRIPLE);
            }
        } else {
            atomContainer.addBond(x, y, IBond.Order.SINGLE);
        }
    }

    public IAtomContainer getAtomContainer() {
        return this.atomContainer;
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
        int numberOfAtoms = atomContainer.getAtomCount();
        int numberOfBonds = atomContainer.getBondCount();
    
        // n atoms connected into a simple chain have (n - 1) bonds
        return numberOfBonds >= (numberOfAtoms - 1) 
        && ConnectivityChecker.isConnected(atomContainer);
    }

    public boolean check(int x, int y) {
        boolean sSubgraphs = Util.saturatedSubgraph(x, atomContainer);
        if (sSubgraphs) {
            System.out.println("saturated subgraphs");
            return false;
        }
        
//        boolean isCanonical = CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
        boolean isCanonical = CanonicalChecker.isCanonicalWithSignaturePartition(atomContainer);
        System.out.println(isCanonical + "\t" + this);
        return isCanonical;
//        return true;
    }
    
    public boolean isCanonical() {
//        boolean isCanonical = CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
        boolean isCanonical = CanonicalChecker.isCanonicalWithSignaturePartition(atomContainer);
        System.out.println(isCanonical + "\t" + this);
        return isCanonical;
    }
    

    public Orbit getUnsaturatedOrbit() {
        CDKMoleculeSignature signature = 
            new CDKMoleculeSignature(this.atomContainer);
        List<Orbit> orbits = signature.calculateOrbits();
        Collections.reverse(orbits);
        sort(orbits);
        for (Orbit o : orbits) {
            if (isSaturated(o)) {
                continue;
            } else {
                return o;
            }
        }
        return null;
    }
    
    private void sort(List<Orbit> orbits) {
        for (Orbit o : orbits) {
            o.sort();
        }
        Collections.sort(orbits, new Comparator<Orbit>() {

            public int compare(Orbit o1, Orbit o2) {
                return new Integer(o1.getFirstAtom()).compareTo(
                        new Integer(o2.getFirstAtom()));
            }
            
        });
    }

    /**
     * Get the list of atoms to be saturated.
     * 
     * @return a list of atom indices
     */
    public List<Integer> unsaturatedAtoms(int indexOfSaturatingAtom) {
        CDKMoleculeSignature signature = 
            new CDKMoleculeSignature(this.atomContainer);
        List<Orbit> orbits = signature.calculateOrbits();

        // XXX : fix this
//        Collections.reverse(orbits);
        sort(orbits);
        
//        System.out.println("Orbits : " + orbits);
        List<Integer> unsaturated = new ArrayList<Integer>();
        for (Orbit o : orbits) {
            if (o.isEmpty() || isSaturated(o)) continue;
            List<Integer> atomIndices = o.getAtomIndices();
            int indexIndex = 0;
            int atomIndex = 0;
            while (indexIndex < atomIndices.size()) {
                atomIndex = atomIndices.get(indexIndex); 
                if (atomIndex == indexOfSaturatingAtom ||
                        maximumAttachment(indexOfSaturatingAtom, atomIndex)) {
//                    continue;
                    indexIndex++;
                } else {
                    unsaturated.add(atomIndex);
                    break;
                }
//                indexIndex++;
            }
        }
        return unsaturated;
    }
    
    public boolean maximumAttachment(int atomIndexA, int atomIndexB) {
        IAtom a = atomContainer.getAtom(atomIndexA);
        IAtom b = atomContainer.getAtom(atomIndexB);
        IBond bond = atomContainer.getBond(a, b);
        if (bond == null) {
            return false;
        } else {
            return bond.getOrder() == IBond.Order.TRIPLE;
        }
    }
    
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

    private boolean isSaturated(Orbit o) {
        return this.isSaturated(o.getFirstAtom());
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
    //        for (Orbit o : orbits) {
    //            sb.append(o.toString()).append(",");
    //        }
            return sb.toString();
        }

}
