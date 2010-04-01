package org.openscience.cdk.deterministic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.graph.PathTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.CDKMoleculeSignature;
import org.openscience.cdk.signature.Orbit;
import org.openscience.cdk.signature.TargetMolecularSignature;

/**
 * The graph maintains its underlying atom container as well as a list of the
 * atoms that can be saturated and a mapping of atoms to target atomic 
 * signatures.
 * 
 * @author maclean
 *
 */
public class Graph {
    
    /**
     * The actual atom and bond data - may be disconnected fragments.
     */
    private IAtomContainer atomContainer;
    
    /**
     * These are the indices of the TargetAtomicSignatures that have
     * been assigned to each atom.
     */
    private ArrayList<Integer> targets;
    
    /**
     * The 'orbits' are lists of atoms that are equivalent because they have
     * the same target signature and the same signature in the atom container.
     */
    private List<Orbit> orbits;
    
    private ArrayList<Integer> unsaturatedAtoms;
    
    private ArrayList<Boolean> orbitUnsaturatedFlags;
    
    /**
     * Wrap an atom container in a graph, to manage the fragments
     * 
     * @param atomContainer the underlying atom container
     */
    public Graph(IAtomContainer atomContainer) {
        this.atomContainer = atomContainer;
        this.targets = new ArrayList<Integer>();
        this.orbits = new ArrayList<Orbit>();
        this.unsaturatedAtoms = new ArrayList<Integer>();
        this.orbitUnsaturatedFlags = new ArrayList<Boolean>();
        this.determineUnsaturated();
        this.determineOrbitUnsaturated();
    }
    
    /**
     * Copy constructor
     * 
     * @param g the graph to copy
     */
    public Graph(Graph g) {
        // For now, clone the whole atom container, to make sure.
        // In theory, it might be possible to just copy over atom references
        // and clone the bonds
        try {
            this.atomContainer = (IAtomContainer) g.atomContainer.clone();
            this.targets = (ArrayList<Integer>) g.targets.clone();
            this.orbits = new ArrayList<Orbit>();
            for (Orbit o : g.orbits) {
                this.orbits.add((Orbit)o.clone());
            }
            this.unsaturatedAtoms = (ArrayList<Integer>) g.unsaturatedAtoms.clone();
            this.orbitUnsaturatedFlags = 
                (ArrayList<Boolean>) g.orbitUnsaturatedFlags.clone();
        } catch (CloneNotSupportedException c) {
            
        }
    }
    
    /**
     * Check two atoms to see if a bond can be formed between them, according
     * to the target signatures.
     * 
     * @param x the index of an atom
     * @param y the index of another atom
     * @param hTau the target molecular signature to use
     * @return true if a bond can be formed
     */
    public boolean compatibleBond(int x, int y, TargetMolecularSignature hTau) {
        int h = hTau.getHeight();
        int targetX = targets.get(x);
        int targetY = targets.get(y);
        String hMinusOneTauY = hTau.getTargetAtomicSignature(targetY, h - 1);
        
//        int n12 = hTau.compatibleTargetBonds(targetX, h, hMinusOneTauY);
        int n12 = hTau.compatibleTargetBonds(targetX, targetY);
        if (n12 == 0) return false;
        int m12 = countExistingBondsOfType(y, h, hMinusOneTauY);
       
        return n12 - m12 >= 0;
    }
    
    /**
     * Count of the existing bonds of a particular type.
     * 
     * @param x the index of an atom
     * @param h the height of the signature
     * @param hMinusOneTauY the h-1 signature to match against
     * @return
     */
    public int countExistingBondsOfType(int x, int h, String hMinusOneTauY) {
        // count the number of bonds already used between x and y
        int m12 = 0;
        for (String hMinusOneTauY1 : getSignaturesOfBondedAtoms(x, h - 1)) {
            if (hMinusOneTauY.equals(hMinusOneTauY1)) {
                m12++;
            }
        }
        return m12;
    }
    
    /**
     * The signatures of the atoms in the graph bonded to the atom at <code>x
     * </code> up to height <code>h</code>.
     * 
     * @param x the atom to get the neighbour-signatures of
     * @param h the height of those signatures
     * @return a list of signature strings
     */
    public List<String> getSignaturesOfBondedAtoms(int x, int h) {
        IAtom atom = atomContainer.getAtom(x);
        List<String> signatures = new ArrayList<String>();
        for (IAtom connected : atomContainer.getConnectedAtomsList(atom)) {
            int atomNumber = atomContainer.getAtomNumber(connected);
            CDKMoleculeSignature signature = 
                new CDKMoleculeSignature(this.atomContainer);
            signatures.add(signature.signatureStringForVertex(atomNumber, h));
        }
        return signatures;
    }

    public IAtomContainer getAtomContainer() {
        return this.atomContainer;
    }
    
    /**
     * Given a target molecular signature composed of target atomic signatures,
     * assign an atomic signature to each atom of the atom container.
     * 
     * @param signature the target molecular signature
     */
    public void assignAtomsToTarget(TargetMolecularSignature signature) {
        int currentTarget = 0;
        int currentCount = signature.getCount(0);
        for (int i = 0; i < this.atomContainer.getAtomCount(); i++) {
            if (currentCount > 0) {
                currentCount -= 1;
                this.targets.add(currentTarget);
            } else {
                currentTarget += 1;
                currentCount = signature.getCount(currentTarget) - 1;
                this.targets.add(currentTarget);
            }
        }
    }
    
    /**
     * Divide the atoms into partitions using both the calculated signatures
     * based on connectivity, and the target signatures. So, two atoms are in
     * the same partition (orbit) if and only if they have both signatures
     * equal. 
     */
    public void partition() {
        CDKMoleculeSignature signature = 
            new CDKMoleculeSignature(this.atomContainer);
        this.orbits = signature.calculateOrbits();
        
        // XXX : fix this
        Collections.reverse(orbits);
    }

    /**
     * Remove the atom index <code>i</code> from the list 
     * of atoms to be saturated.
     * 
     * @param i the atom index (not the index of the atom index!) to remove
     */
    public void removeFromUnsaturatedList(int i) {
        this.unsaturatedAtoms.remove(new Integer(i));
    }
    
    public void removeFromOrbit(int i) {
        for (Orbit o : this.orbits) {
            if (o.contains(i)) {
                o.remove(i);
                return;
            }
        }
    }
    
    /**
     * Determine which atoms are unsaturated.
     */
    public void determineUnsaturated() {
        for (IAtom atom : atomContainer.atoms()) {
            if (Util.isSaturated(atom, atomContainer)) {
                continue;
            } else {
                unsaturatedAtoms.add(atomContainer.getAtomNumber(atom));
            }
        }
    }
    
    /**
     * For each orbit, determine if it is an unsaturated one, or not.
     */
    public void determineOrbitUnsaturated() {
        for (Orbit o : this.orbits) {
            if (o.isEmpty()) continue;
            int i = o.getFirstAtom();
            if (isSaturated(i)) {
                this.orbitUnsaturatedFlags.add(true);
            } else {
                this.orbitUnsaturatedFlags.add(false);
            }
        }
    }
    
    public List<Integer> getAtomTargetMap() {
        return this.targets;
    }

    /**
     * Check this atom for saturation.
     * 
     * @param atomNumber the atom to check
     * @return true if this atom is saturated
     */
    public boolean isSaturated(int atomNumber) {
        IAtom atom = this.atomContainer.getAtom(atomNumber);
//            return Util.getInstance().getChecker().isSaturated(
//                        atom, atomContainer);
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
    
    /**
     * Get the list of atoms to be saturated.
     * 
     * @return a list of atom indices
     */
    public List<Integer> unsaturatedAtoms() {
//        return this.unsaturatedAtoms;
        List<Integer> unsaturated = new ArrayList<Integer>();
        for (Orbit o : this.orbits) {
            if (o.isEmpty()) continue;
            unsaturated.add(o.getFirstAtom());
        }
        return unsaturated;
    }
    
    /**
     * Add a bond between these two atoms.
     * 
     * @param x the first atom to be bonded
     * @param y the second atom to be bonded
     */
    public void bond(int x, int y) {
        System.out.println(
                String.format("bonding %d and %d (%s-%s)",
                x, y, 
                atomContainer.getAtom(x).getSymbol(),
                atomContainer.getAtom(y).getSymbol()));
        this.atomContainer.addBond(x, y, IBond.Order.SINGLE);
    }

    /**
     * Get the list of orbits (the equivalence classes of atoms).
     * 
     * @return the list of orbits
     */
    public List<Orbit> getOrbits() {
        return this.orbits;
    }
    
    /**
     * Calculate the diameter of the graph, which is the longest path between
     * any two of the atoms.
     *   
     * @return the maximum vertex distance
     */
    public int getDiameter() {
        return PathTools.getMolecularGraphDiameter(atomContainer);
    }

    /**
     * Get the first unsaturated orbit
     * 
     * @return the orbit (list of atoms) to try and saturate
     */
    public Orbit getUnsaturatedOrbit() {
        for (Orbit o : this.orbits) {
            if (this.unsaturatedAtoms.contains(o.getFirstAtom())) {
                return o;
            }
        }
        return null;
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
     * @return true if this atom is not part of a saturated subgraph
     */
    public boolean noSaturatedSubgraphs(int x) {
        IMolecule subGraph = 
            NoNotificationChemObjectBuilder.getInstance().newMolecule();
        List<IAtom> sphere = new ArrayList<IAtom>(); 
        IAtom atomX = atomContainer.getAtom(x);
        sphere.add(atomX);
        atomX.setFlag(CDKConstants.VISITED, true);
        PathTools.breadthFirstSearch(atomContainer, sphere, subGraph);
        int saturationCount = 0;
        for (IAtom atom : subGraph.atoms()) {
            atom.setFlag(CDKConstants.VISITED, false);

            if (Util.isSaturated(atom, subGraph)) {
                saturationCount++;
            }
        }
        int atomCount = subGraph.getAtomCount();
        
        // TODO : remove this debugging stuff
//        String atoms = "";
//        for (IAtom a : subGraph.atoms()) { atoms += a.getSymbol(); }
//        System.out.println(atoms + " " + saturationCount + " " + atomCount);
        // TODO : remove this debugging stuff
        
        return saturationCount < atomCount 
            || atomCount == atomContainer.getAtomCount();
    }

    public boolean isCanonical() {
        return CanonicalChecker.isCanonical(atomContainer);
    }
    
    public boolean signatureMatches(TargetMolecularSignature tau) {
        // TODO Auto-generated method stub
        return true;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (IAtom atom : this.atomContainer.atoms()) {
            sb.append(atom.getSymbol()).append(i);
            i++;
        }
        sb.append(" [ ");
        for (IBond bond : this.atomContainer.bonds()) {
            int l = this.atomContainer.getAtomNumber(bond.getAtom(0));
            int r = this.atomContainer.getAtomNumber(bond.getAtom(1));
            if (l < r) {
                sb.append(l).append("-").append(r).append(" ");
            } else {
                sb.append(r).append("-").append(l).append(" ");
            }
        }
        sb.append("] ");
        for (Orbit o : orbits) {
            sb.append(o.toString()).append(",");
        }
        return sb.toString();
    }
    
}
