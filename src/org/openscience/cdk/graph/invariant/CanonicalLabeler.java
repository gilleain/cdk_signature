package org.openscience.cdk.graph.invariant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 * Canonically labels an atom container implementing the algorithm published in
 * David Weininger et.al. {@cdk.cite WEI89}. The Collections.sort() method uses
 * a merge sort which is stable and runs in n log(n).
 * 
 * @cdk.module standard
 * @cdk.githash
 * 
 * @author Oliver Horlacher <oliver.horlacher@therastrat.com>
 * @cdk.created 2002-02-26
 * 
 * @cdk.keyword canonicalization
 */
// @TestClass("org.openscience.cdk.graph.invariant.CanonicalLabelerTest")
public class CanonicalLabeler {
    
    public static final Comparator<InvPair> currentComparator = 
        new Comparator<InvPair>() {
            public int compare(InvPair o1, InvPair o2) {
                return o1.getCurrent() -  o2.getCurrent();
            }
        };
        
    public static final Comparator<InvPair> lastComparator = 
        new Comparator<InvPair>() {
            public int compare(InvPair o1, InvPair o2) {
                return o1.getLast() - o2.getLast();
            }
        };

    public static final Comparator<InvPair> currentLastComparator = 
        new Comparator<InvPair>() {
           public int compare(InvPair o1, InvPair o2) {
                int cDiff = o1.getCurrent() - o2.getCurrent();
                if (cDiff == 0) {
                    return o1.getLast() - o2.getLast();
                } else {
                    return cDiff;
                }
           }
        };
    
    public void canonLabel(IAtomContainer atomContainer) {
        int[] labels = canonicallyLabel(atomContainer);
        for (int i = 0; i < labels.length; i++) {
            atomContainer.getAtom(i).setProperty(
                 org.openscience.cdk.smiles.InvPair.CANONICAL_LABEL, (long)labels[i]);
        }
    }
        
//    public int[] canonLabel(IAtomContainer atomContainer) {
//           return CanonicalLabeler.canonicallyLabel(atomContainer); 
//    }
        
    /**
     * Canonically label the fragment. The labels are set as atom property
     * InvPair.CANONICAL_LABEL of type Integer, indicating the canonical order.
     * This is an implementation of the algorithm published in David Weininger
     * et.al. {@cdk.cite WEI89}.
     * 
     * <p>
     * The Collections.sort() method uses a merge sort which is stable and runs
     * in n log(n).
     * 
     * <p>
     * It is assumed that a chemically valid AtomContainer is provided: this
     * method does not check the correctness of the AtomContainer. Negative H
     * counts will cause a NumberFormatException to be thrown.
     * 
     * @param atomContainer The molecule to label
     */
    public static int[] canonicallyLabel(IAtomContainer atomContainer) {
        if (atomContainer.getAtomCount() == 0) {
            return new int[] {};
        } else if (atomContainer.getAtomCount() == 1) {
            return new int[] {1};
        } else {
            int[] labels = new int[atomContainer.getAtomCount()];
            ArrayList<InvPair> invariants = createInvariants(atomContainer);
            sortAndRankInvariants(invariants, atomContainer, labels);
            return labels;
        }
    }

    /**
     * @param invariants the invariance pair vector
     * @param atoms the atom container
     */
    private static void rescale(
            ArrayList<InvPair> invariants, IAtomContainer atoms, int[] labels) {
        primeProduct(invariants, atoms);
        sortAndRankInvariants(invariants, atoms, labels);
    }

    /**
     * @param invariants the invariance pair vector
     * @param atoms the atom container
     */
    private static void sortAndRankInvariants(
            ArrayList<InvPair> invariants, IAtomContainer atoms, int[] labels) {
        sortInvariants(invariants);
        rankInvariants(invariants);
        
        if (!isInvariantPartition(invariants)) {
            rescale(invariants, atoms, labels);
        } else {
            // On first pass save, partitioning as symmetry classes.
            InvPair last = invariants.get(invariants.size() - 1); 
            if (last.getCurrent() < invariants.size()) {
                breakTies(invariants);
                rescale(invariants, atoms, labels);
            }
            // now apply the ranking
            for (InvPair invariant : invariants) {
                labels[invariant.getAtomNumber()] = invariant.getCurrent();
            }
        }
    }

    /**
     * Create initial invariant labeling (corresponds to step 1).
     * 
     * @return ArrayList containing the invariant pairs
     */
    private static ArrayList<InvPair> createInvariants(IAtomContainer atomContainer) {
        ArrayList<InvPair> vect = new ArrayList();
        int i = 0;
        for (IAtom a : atomContainer.atoms()) {
            StringBuffer inv = new StringBuffer();

            // total number of bonds
            int size = atomContainer.getConnectedAtomsList(a).size();
            int numberOfHydrogens = 
                a.getHydrogenCount() == CDKConstants.UNSET ?
                        0 : a.getHydrogenCount();
            inv.append(size + numberOfHydrogens);

            // number of non H bonds
            inv.append(size);

            // Atomic number
            int atomicNumber = 
                a.getAtomicNumber() == CDKConstants.UNSET ?
                        0: a.getAtomicNumber();
            inv.append(atomicNumber);

            Double charge = 
                a.getCharge() == CDKConstants.UNSET? 0.0 : a.getCharge();
            
            // Sign of charge
            if (charge < 0) { 
                inv.append(1);
            } else {
                inv.append(0); 
            }
            
            // Absolute charge
            int absCharge = 
                (int) Math.abs(
                        (a.getFormalCharge() == CDKConstants.UNSET ? 
                                0.0 : a.getFormalCharge()));
            inv.append(absCharge);
            
            // number of hydrogens
            inv.append(numberOfHydrogens);
            
            vect.add(new InvPair(Integer.parseInt(inv.toString()), i));
            i++;
        }
        return vect;
    }

    /**
     * Calculates the product of the neighboring primes.
     * 
     * @param invariants the list of invariant pairs
     * @param atomContainer the atom container
     */
    private static void primeProduct(
            ArrayList<InvPair> invariants, IAtomContainer atomContainer) {
        int sum;
        for (InvPair inv : invariants) {
            sum = 1;
            IAtom atom = atomContainer.getAtom(inv.getAtomNumber());
            for (IAtom a : atomContainer.getConnectedAtomsList(atom)) {
                int atomNumber = atomContainer.getAtomNumber(a);
                InvPair pair = findPair(invariants, atomNumber);
                sum *= pair.getPrime();
            }
            inv.setCurrentAndLast(sum);
        }
    }
    
    private static InvPair findPair(ArrayList<InvPair> invariants, int atomNumber) {
        for (InvPair pair : invariants) {
            if (pair.getAtomNumber() == atomNumber) {
                return pair;
            }
        }
        
        return null;
    }

    /**
     * Sorts the vector according to the current invariants (corresponds to step
     * 3).
     * 
     * @param invariants the list of invariant pairs
     * @cdk.todo can this be done in one loop?
     */
    private static void sortInvariants(ArrayList<InvPair> invariants) {
//        Collections.sort(invariants, CanonicalLabeler.currentComparator);
//        Collections.sort(invariants, CanonicalLabeler.lastComparator);
        Collections.sort(invariants, CanonicalLabeler.currentLastComparator);
    }

    /**
     * Rank atomic vector, corresponds to step 4.
     * 
     * @param invariants the invariant pairs
     */
    private static void rankInvariants(ArrayList<InvPair> invariants) {
        int num = 1;
        int[] temp = new int[invariants.size()];
        InvPair last = invariants.get(0);

        int x = 0;
        for (InvPair current : invariants) {
            if (!last.equals(current)) {
                num++;
            }
            temp[x] = num;
            last = current;
            x++;
        }

        x = 0;
        for (InvPair curr : invariants) {
            curr.setCurrentAndPrime(temp[x]);
            x++;
        }
    }

    /**
     * Checks to see if the vector is invariantly partitioned.
     * 
     * @param v
     *            the invariance pair vector
     * @return true if the vector is invariantly partitioned, false otherwise
     */
    private static boolean isInvariantPartition(ArrayList<InvPair> invariants) {
        InvPair largest = invariants.get(invariants.size() - 1); 
        if (largest.getCurrent() == invariants.size())
            return true;

        for (InvPair current : invariants) {
            if (current.getCurrent() != current.getLast())
                return false;
        }
        return true;
    }

    /**
     * Break ties. Corresponds to step 7
     * 
     * @param invariants the list of invariant pairs
     */
    private static void breakTies(ArrayList<InvPair> invariants) {
        InvPair last = null;
        int tie = 0;
        boolean found = false;
        int x = 0;
        for (InvPair current : invariants) {
            current.setCurrentAndPrime(current.getCurrent() * 2);
            if (x != 0 && !found && current.getCurrent() == last.getCurrent()) {
                tie = x - 1;
                found = true;
            }
            last = current;
            x++;
        }

        InvPair curr = invariants.get(tie);
        curr.setCurrentAndPrime(curr.getCurrent() - 1);
    }
}
