package org.openscience.cdk.signature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * A list of atom indices, and the label of the orbit.
 * 
 * @author maclean
 * @cdk.module signature
 * 
 */
public class Orbit implements Iterable<Integer>, Cloneable {
    
    /**
     * The atom indices in this orbit
     */
    private List<Integer> atomIndices;
    
    /**
     * The label that all the atoms in the orbit share
     */
    private String label;
    
    /**
     * The maximum height of the signature string
     */
    private int height;
    
    /**
     * @param label
     * @param height
     */
    public Orbit(String label, int height) {
        this.label = label;
        this.atomIndices = new ArrayList<Integer>();
        this.height = height;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<Integer> iterator() {
        return this.atomIndices.iterator();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        Orbit o = new Orbit(this.label, this.height);
        for (Integer i : this.atomIndices) {
            o.atomIndices.add(new Integer(i));
        }
        return o;
    }
    
    /**
     * Sorts the atom indices in this orbit
     */
    public void sort() {
        // TODO : change the list to a sorted set?
        Collections.sort(this.atomIndices);
    }
    
    /**
     * Gets the height of the signature label.
     * 
     * @return the height of the signature of this orbit
     */
    public int getHeight() {
        return this.height;
    }
    
    /**
     * Gets all the atom indices as a list.
     * 
     * @return the atom indices
     */
    public List<Integer> getAtomIndices() {
        return this.atomIndices;
    }
    
    /**
     * Adds an atom index to the orbit.
     * 
     * @param atomIndex the atom index
     */
    public void addAtom(int atomIndex) {
        this.atomIndices.add(atomIndex);
    }
    
    /**
     * Checks to see if the orbit has this string as a label.
     * 
     * @param otherLabel the label to compare with
     * @return true if it has this label
     */
    public boolean hasLabel(String otherLabel) {
        return this.label.equals(otherLabel);
    }
    
    /**
     * Checks to see if the orbit is empty.
     * 
     * @return true if there are no atom indices in the orbit
     */
    public boolean isEmpty() {
        return this.atomIndices.isEmpty();
    }

    /**
     * Gets the first atom index of the orbit.
     * 
     * @return the first atom index
     */
    public int getFirstAtom() {
        return this.atomIndices.get(0);
    }
    
    /**
     * Removes an atom index from the orbit.
     * 
     * @param atomIndex the atom index to remove
     */
    public void remove(int atomIndex) {
        this.atomIndices.remove(this.atomIndices.indexOf(atomIndex));
    }
    

    /**
     * Gets the label of the orbit.
     * 
     * @return the orbit's string label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Checks to see if the orbit contains this atom index.
     * 
     * @param atomIndex the atom index to look for
     * @return true if the orbit contains this atom index
     */
    public boolean contains(int atomIndex) {
        return this.atomIndices.contains(atomIndex);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return label + " " +
        Arrays.deepToString(atomIndices.toArray()); 
    }

}
