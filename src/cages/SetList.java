package cages;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.openscience.cdk.group.Permutation;

public class SetList {
    
    private List<SortedSet<Integer>> sets;
    
    public SetList(SortedSet initialSet) {
        this.sets = new ArrayList<SortedSet<Integer>>();
        this.sets.add(initialSet);
    }

    public boolean contains(int value, int setIndex) {
        for (SortedSet<Integer> set : this.sets) {
            if (set.contains(value)) {
                return true;
            }
        }
        return false;
    }
    
    public SortedSet<Integer> get(int i) {
        return this.sets.get(i);
    }
    
    public void add(SortedSet<Integer> element) {
        this.sets.add(element);
    }
    
    public void apply(Permutation g, int l) {
        this.sets.set(l + 1, g.apply(this.sets.get(l)));
    }

}
