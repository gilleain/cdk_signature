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
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.AtomSignature;
import org.openscience.cdk.signature.MoleculeSignature;
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
    
    public boolean height1SignatureMatches(int x, TargetMolecularSignature tms) {
        if (targets.size() == 0) return true;   // TODO
        String target = tms.getTargetAtomicSignature(targets.get(x), 1);
        String atomSignature = 
            new AtomSignature(x, 1, atomContainer).toCanonicalString();
        boolean equal = target.equals(atomSignature);
        if (equal) {
            System.out.println(
                    "EQ  " + target + " and " 
                    + atomSignature + "\tat " + x + " in " + this);
        } else {
            System.out.println(
                    "NEQ " + target + " and " 
                    + atomSignature + "\tat " + x + " in " + this);
            
        }
        return equal;
    }

    public boolean check(int x, int y, TargetMolecularSignature hTau) {
        boolean sSubgraphs = Util.saturatedSubgraph(x, atomContainer);
        if (sSubgraphs) {
            System.out.println("saturated subgraphs");
            return false;
        }
        
//        boolean isCanonical = CanonicalChecker.isCanonicalWithColorPartition(atomContainer);
        boolean isCanonical = true;
//            CanonicalChecker.isCanonicalWithSignaturePartition(atomContainer);
        
        boolean isCompatible = true;
        if (hTau != null) {
            isCompatible = compatibleBond(x, y, hTau) 
                        && compatibleBond(y, x, hTau);
            System.out.println("compatible " + isCompatible + " canonical " 
                    + isCanonical + "\t" + this);
        } else {
//            System.out.println("canonical " + isCanonical + "\t" + this);
        }
        return isCanonical && isCompatible;
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
        System.out.println(
                "Checking compatibility of " + x + " and " + y
                + " targetX " + targetX + " targetY " + targetY);
        String hMinusOneTauY = hTau.getTargetAtomicSignature(targetY, h - 1);
//        String hMinusOneTauY = hTau.getTargetAtomicSignature(targetY, h);
        
//        int n12 = hTau.compatibleTargetBonds(targetX, h, hMinusOneTauY);
        int n12 = hTau.compatibleTargetBonds(targetX, targetY);
        
        if (n12 == 0) {
            System.out.println("n12 == 0 NO " + targetX + " " + targetY);
            return false;
        } else {
            System.out.println("n12 != 0 " + targetX + " " + targetY);
        }
        int m12 = countExistingBondsOfType(y, h, hMinusOneTauY);
       
        boolean lessThanOrEqual = n12 - m12 >= 0;
        if (lessThanOrEqual) {
            System.out.println("m12 " + m12 + " n12 " + n12);
        } else {
            System.out.println("m12 " + m12 + " n12 " + n12 + " NO");
        }
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
            System.out.println(
                  "Counting existing bonds "
                    + hMinusOneTauY + " " + hMinusOneTauY1 
                    + " h= " + h + " x = " + x);
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
            MoleculeSignature signature = 
                new MoleculeSignature(this.atomContainer);
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
        signature.sortSignatures();
        int currentTarget = 0;
        int currentCount = signature.getCount(0);
        for (int i = 0; i < this.atomContainer.getAtomCount(); i++) {
            if (currentCount > 0) {
                currentCount -= 1;
                System.out.println("assigning " + 
                        signature.getTargetAtomicSignature(currentTarget)
                        + " to "  + i);
                this.targets.add(currentTarget);
            } else {
                currentTarget += 1;
                currentCount = signature.getCount(currentTarget) - 1;
                System.out.println("assigning " + 
                        signature.getTargetAtomicSignature(currentTarget)
                        + " to "  + i);
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
        MoleculeSignature signature = 
            new MoleculeSignature(this.atomContainer);
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
    public List<Integer> unsaturatedAtoms(int indexOfSaturatingAtom) {
//      public List<Integer> unsaturatedAtoms(int indexOfSaturatingAtom) {
        MoleculeSignature signature = 
            new MoleculeSignature(this.atomContainer);
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
    
    private boolean isSaturated(Orbit o) {
        return this.isSaturated(o.getFirstAtom());
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
        MoleculeSignature signature = 
            new MoleculeSignature(this.atomContainer);
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
            int o = bond.getOrder().ordinal() + 1;
            sb.append("(").append(o).append(") ");
        }
        sb.append("] ");
        for (Orbit o : orbits) {
            sb.append(o.toString()).append(",");
        }
        return sb.toString();
    }
    
}
