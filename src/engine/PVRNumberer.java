package engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;

/**
 * Canonically number the atoms of a molecule according to the scheme
 * outlined in the paper
 * 
 * "On Canonical Numbering of Carbon Atoms in Fullerenes: 
 * C60 Buckminsterfullerene" CCACAA 78 (4) 493-502 (2005) 
 *  
 * @author maclean
 *
 */
public class PVRNumberer {
    
    private int[] bestPermutation;
    
    public static final Comparator<int[]> lexicographicOrder = 
        new Comparator<int[]>() {

            public int compare(int[] a, int[] b) {
                for (int i = 0; i < a.length; i++) {
                    if (a[i] < b[i]) {
                        return -1;
                    } else if (a[i] > b[i]) {
                        return 1;
                    } else {
                        continue;
                    }
                }
                return 0;
            }
            
        };
    
    public PVRNumberer() {
        this.bestPermutation = null;
    }
    
    /**
     * Get the numbering, without altering the molecule.
     * 
     * @param molecule a molecule to determine the numbering for
     * @return
     */
    public int[] getNumbering(IMolecule molecule) {
        this.bestPermutation = null;
        this.renumber(molecule, 0, new int[molecule.getAtomCount()]);
        return this.bestPermutation;
    }
    
    private void renumber(IMolecule molecule, int k, int[] permutation) {
        if (k == molecule.getAtomCount()) {
            if (this.bestPermutation == null) {
                this.bestPermutation = permutation;
            } else {
                if (this.better(this.bestPermutation, permutation)) {
                    this.bestPermutation = permutation;
                }
            }
        } else {
            // try all possible next candidates
            for (int c : getCandidates(permutation, molecule, k)) {
                int[] nextPermutation = new int[molecule.getAtomCount()];
                System.arraycopy(permutation, 0, nextPermutation, 0, k);
                nextPermutation[k + 1] = c;
                renumber(molecule, k + 1, nextPermutation);
            }
            
        }
    }
    
    private boolean better(int[] oldPermutation, int[] newPermutation) {
        return true;
    }
    
    private ArrayList<Integer> getCandidates(
            int[] permutation, IMolecule molecule, int k) {
        
        ArrayList<Integer> candidates = new ArrayList<Integer>();
        ArrayList<int[]> matrix = new ArrayList<int[]>();
        for (int i = k; i < molecule.getAtomCount(); i++) {
            IAtom v = molecule.getAtom(i);
            int[] row = new int[k];
            for (int j = 0; j < k; j++) {
                IAtom q = molecule.getAtom(permutation[j]);
                if (adjacent(v, q, molecule)) {
                    row[j] = 1;
                } else{
                    row[j] = 0;
                }
            }
        }
        Collections.sort(matrix, PVRNumberer.lexicographicOrder);
        ArrayList<ArrayList<Integer>> ranks = 
            new ArrayList<ArrayList<Integer>>();
        int[] previous = null;
        ArrayList<Integer> rankList = new ArrayList<Integer>();
        ranks.add(rankList);
        
        for (int i = 0; i < matrix.size(); i++) {
            int[] row = matrix.get(i);
            if (previous == null || row == previous) {
                rankList.add(i);
            } else {
                rankList = new ArrayList<Integer>();
                rankList.add(i);
            }
            previous = row;
        }
//        int maximumRank = ranks.size();
//        for (int i : ranks.get(0)) {
            
//        }
        return candidates;
    }
    
    private boolean adjacent(IAtom a, IAtom b, IMolecule m) {
        for (IAtom c : m.getConnectedAtomsList(a)) {
            if (b == c) {
                return true;
            }
        }
        return false;
    }

}
