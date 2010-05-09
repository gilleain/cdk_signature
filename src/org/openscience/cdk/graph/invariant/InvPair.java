package org.openscience.cdk.graph.invariant;

import org.openscience.cdk.math.Primes;

/**
 * Re-write of the invariant pair class.
 * 
 * @author Oliver Horlacher <oliver.horlacher@therastrat.com>
 * @author maclean
 *
 */
public class InvPair  {
    
    private int last = 0;

    private int current = 0;

    private int atomNumber;

    private int prime;

    public InvPair(int current, int atomNumber) {
        this.current = current;
        this.atomNumber = atomNumber;
    }

    public int getAtomNumber() {
        return this.atomNumber;
    }

    /**
     * Get the current prime number.
     * 
     * @return The current prime number
     * @see #setPrime()
     */
    public int getPrime() {
        return prime;
    }

    public int getLast() {
        return last;
    }

    public void setLast(int newLast) {
        last = newLast;
    }

    /**
     * Set the value of the seed, with the side-effect of setting the prime.
     * 
     * @see #getCurrent()
     */
    public void setCurrentAndPrime(int newCurr) {
        this.current = newCurr;
        prime = Primes.getPrimeAt(current - 1);
    }
    
    /**
     * Set the value of the seed.
     * 
     * @see #getCurrent()
     */
    public void setCurrentAndLast(int newCurr) {
        this.last = this.current;
        this.current = newCurr;
    }

    /**
     * Get the current seed.
     * 
     * @return The seed
     * @see #setCurrent(long)
     * @see #setPrime()
     * @see #getPrime()
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Check whether this instance equals another instance.
     * 
     * @param e
     *            An instance of InvPair
     * @return true if they are equal, false otherwise
     */
    public boolean equals(Object e) {
        if (e instanceof InvPair) {
            InvPair o = (InvPair) e;
            return (last == o.getLast() && current == o.getCurrent());
        } else {
            return false;
        }
    }

    /**
     * String representation.
     * 
     * @return The string representation of the class.
     */
    public String toString() {
        return current + "";
    }
}
