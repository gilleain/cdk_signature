package org.openscience.cdk.signature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;

/**
 * A collection of {@link TargetAtomicSignature}s and counts of same. This is
 * used to represent a structure or set of structures. Note that it is possible 
 * to make a target molecular signature that does not correspond to any 
 * structures.
 * 
 * As a container of atomic signatures, methods are provided by this class to
 * determine the compatibilities of these signatures. To this end, it can
 * calculate an all-v-all table of compatibility counts. This is computed 
 * lazily on the first call to <code>compatibleTargetBonds(i, j)</code>.  
 * 
 * @author maclean
 *
 */
public class TargetMolecularSignature {
    
    private List<TargetAtomicSignature> signatures;
    
    private int height;
    
    private int[][] lookupTable;
    
    /**
     * Make a target molecular signature from only a formula.
     * 
     * @param formula the elements and counts to use
     */
    public TargetMolecularSignature(IMolecularFormula formula) {
        
        this.signatures = new ArrayList<TargetAtomicSignature>();
        
        ArrayList<String> symbols = new ArrayList<String>(); 
        HashMap<String, Integer> countMap = new HashMap<String, Integer>();
        for (IIsotope isotope : formula.isotopes()) {
            String symbol = isotope.getSymbol();
            symbols.add(symbol);
            countMap.put(symbol, formula.getIsotopeCount(isotope));
        }
        Collections.sort(symbols);
        for (String symbol : symbols) {
            String signatureString = "[" + symbol + "]";
            int count = countMap.get(symbol);
            this.signatures.add(new TargetAtomicSignature(signatureString, count));
        }
        this.lookupTable = createLookupTable();
        this.height = 0;
    }

    /**
     * Make a target molecular signature from a set of strings and a list of
     * counts of those signature strings.
     * 
     * @param signatureStrings the target atomic strings
     * @param counts the counts of the target atomic strings
     * @param height the height of the signature
     */
    public TargetMolecularSignature(ArrayList<String> signatureStrings, 
                                    ArrayList<Integer> counts,
                                    int height) {
        this.signatures = new ArrayList<TargetAtomicSignature>();
        for (int i = 0; i < signatures.size(); i++) {
            String signatureString = signatureStrings.get(i);
            int count = counts.get(i);
            this.signatures.add(new TargetAtomicSignature(signatureString, count));
        }
        this.lookupTable = createLookupTable();
        this.height = height;
    }
    
    /**
     * A target molecular signature with no signatures - add them with the add
     * method.
     * 
     * @param height the height of the signature
     */
    public TargetMolecularSignature(int height) {
        this.signatures = new ArrayList<TargetAtomicSignature>();
        this.height = height;
    }
    
    public boolean matches(IAtomContainer molecule) {
        // record of the best (highest) match for each atom
        int[] maxMatchHeights = new int[molecule.getAtomCount()];
        Arrays.fill(maxMatchHeights, -1);
        
        for (TargetAtomicSignature tas : signatures) {
            int height = tas.getHeight();
            boolean matchedAtLeastOne = false;
            for (int i = 0; i < molecule.getAtomCount(); i++) {
                if (matches(tas, i, molecule)) {
                    matchedAtLeastOne = true;
                    if (height > maxMatchHeights[i]) {
                        maxMatchHeights[i] = height;
                    }
                }
            }
            if (!matchedAtLeastOne) {
                return false;
            }
        }
        System.out.println(java.util.Arrays.toString(maxMatchHeights));
        for (int i = 0; i < maxMatchHeights.length; i++) {
            if (maxMatchHeights[i] == -1) {
                return false;
            }
        }
        return true;
    }
    
    public boolean matches(TargetAtomicSignature tas, int i, IAtomContainer mol) {
        int height = tas.getHeight();
        String atomSignature = 
            new AtomSignature(i, height, mol).toCanonicalString();
        String targetSignature = tas.toCanonicalSignatureString();
        return atomSignature.equals(targetSignature);
    }
    
    public int size() {
        return this.signatures.size();
    }
    
    public List<TargetAtomicSignature> getSignatures() {
        return signatures;
    }
    
    public void sortSignatures() {
        Collections.sort(signatures);
    }
    
    public int[][] getLookupTable() {
        if (lookupTable == null) {
            lookupTable = createLookupTable();
        }
        return lookupTable;
    }
    
    /**
     * Do an all-v-all comparison of the signatures that make up this molecular
     * signature so that they are not re-computed each time.
     * 
     * @return a square N*N array of bond compatibility counts
     */
    private int[][] createLookupTable() {
        // allocate space
        int n = this.signatures.size();
        int[][] table = new int[n][];
        for (int i = 0; i < n; i++) { 
            table[i] = new int[n]; 
        }

        // first, generate and store the reconstructed fragments
        ArrayList<IMolecule> molecules = new ArrayList<IMolecule>();
        for (TargetAtomicSignature signature : this.signatures) {
            molecules.add(signature.toMolecule());
        }
        
        // now, use these to do an all-v-all comparison, and store the results
        for (int i = 0; i < n; i++) {
            TargetAtomicSignature signatureA = this.signatures.get(i);
            IMolecule moleculeA = molecules.get(i);
            for (int j = i; j < n; j++) {
                TargetAtomicSignature signatureB = this.signatures.get(j);
                IMolecule moleculeB = molecules.get(j);
//                System.out.println("A, B: (i, j) : (" + i + " " + j + ")");
//                System.out.println(signatureA.toString());
//                System.out.println(signatureB.toString());
                
                int cAB = compatibleCount(moleculeA, signatureB);
//                System.out.println("A-B " + i + " " + j + " " + signatureA.getHeight() + " " + cAB);
                int cBA = compatibleCount(moleculeB, signatureA);
//                System.out.println("B-A " + i + " " + j + " " + signatureB.getHeight() + " " + cBA);
                table[i][j] = cAB;
                table[j][i] = cBA;
            }
        }
        return table;
    }
    
    private int compatibleCount(IMolecule molecule, TargetAtomicSignature target) {
        MoleculeSignature sigFromMolecule = new MoleculeSignature(molecule);
        int height = target.getHeight() - 1;
        if (height < 0) {
            return 1;
        }
        String a = sigFromMolecule.signatureStringForVertex(0, height);
        int count = 0;
        for (String b : target.getSignatureStringsFromRootChildren(height)) {
//            System.out.println("a " + a);
//            System.out.println("b " + b);
//            System.out.println("---------------------------------");
            if (a.equals(b)) count++;
        }
//        System.out.println("count = " + count);
        return count;
    }
    
    /**
     * Lookup the number of compatible bonds between target atomic signatures.
     * 
     * @param i the index of one signature
     * @param j the index of the other signature
     * @return the count of the number of bonds that can be made
     */
    public int compatibleTargetBonds(int i, int j) {
        if (this.lookupTable == null) {
            this.lookupTable = this.createLookupTable();
        }
        return this.lookupTable[i][j];
    }
    
    /**
     * Count of the number of compatible bonds between target atomic signatures. 
     * 
     * @param targetX the target atomic signature index of atom x
     * @param h the height of the signature
     * @param hMinusOneTauY the h-1 signature to match against
     * @return the count of the number of bonds that can be made
     */
    public int compatibleTargetBonds(int targetX, int h, String hMinusOneTauY) {
        
        // count the number of (h - 1) target signatures of atoms bonded to x 
        // compatible with the (h - 1) signature of y 
        int n12 = 0;
        for (String subSignature : this.getBondedSignatures(targetX, h - 1)) {
            System.out.println("Compatible? " + subSignature 
                    + "\t" + hMinusOneTauY 
                    + "\t" + subSignature.equals(hMinusOneTauY));
            if (hMinusOneTauY.equals(subSignature)) {
                n12++;
            }
        }
        return n12;
    }
    
    /**
     * Get the count for the signature <code>i</code> in the tms.
     * 
     * @param i
     * @return
     */
    public int getCount(int i) {
        return signatures.get(i).getCount();
    }
    
    public void add(String signatureString) {
        this.add(signatureString, 1);
    }
    
    public void add(String signatureString, int count) {
        this.signatures.add(new TargetAtomicSignature(signatureString, count));
    }
    
    public void add(String signatureString, int count, String name) {
        this.signatures.add(new TargetAtomicSignature(signatureString, name, count));
    }

    /**
     * Get the signatures strings of height <code>height</code> 
     * of the atoms bonded to atom <code>x</code>.
     * 
     * @param g
     *            the underlying graph
     * @param x
     *            the atom to use
     * @param height
     *            the height of the signatures to return
     * @return a list of signatures of attached atoms
     */
    public ArrayList<String> getBondedSignatures(int x, int height) {
        return this.signatures.get(x).getSignatureStringsFromRootChildren(height);
    }

    public int getHeight() {
        return this.height;
    }
   
    public TargetAtomicSignature getTargetAtomicSignature(int i) {
        return this.signatures.get(i);
    }
    
    public String getTargetAtomicSignature(int i, int h) {
        return this.signatures.get(i).getSubSignature(h);
    }

    public String getTargetAtomicSubSignature(int x, int h) {
        return this.signatures.get(x).getSignatureString(0, h);
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        int l = signatures.size();
        for (int i = 0; i < l - 1; i++) {
            TargetAtomicSignature signature = this.signatures.get(i);
            int count = signature.getCount();
            if (count > 1) {
                buffer.append(String.valueOf(count) + signature.toString());
            } else {
                buffer.append(signature.toString());
            }
            buffer.append(" + ");
        }
        if (signatures.size() > 0) {
            TargetAtomicSignature signature = this.signatures.get(l - 1);
            int count = signature.getCount();
            if (count > 1) {
                buffer.append(String.valueOf(count) + signature.toString());
            } else {
                buffer.append(signature.toString());
            }
        }
        return buffer.toString();
    }
}
