package cages;

import java.util.Iterator;

import org.openscience.cdk.group.Permutation;


public class PermutationGenerator 
    extends Permutor implements Iterator<Permutation> {

    public PermutationGenerator(int size) {
        super(size);
    }

    public Permutation next() {
        return new Permutation(super.getNextPermutation());
    }

    public void remove() {
        super.setRank(super.getRank() + 1);
    }

}
