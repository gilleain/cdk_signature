package cages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;

/**
 * Pretty broken implementation or port of the code in CAGES Alg 6.15/6.16.
 * 
 * @author maclean
 *
 */
public class RepresentativeLister {
    
    private SSPermutationGroup group;
    
    private int n;
    
    public RepresentativeLister(int n, SSPermutationGroup group) {
        this.group = group;
        this.n = n;
    }
    
    public List<SortedSet<Integer>> orbReps(List<SortedSet<Integer>> R) {
        List<SortedSet<Integer>> S = new ArrayList<SortedSet<Integer>>();
        Permutation beta = new Permutation(n);
        for (SortedSet<Integer> A : R) {
            SortedSet<Integer> tmp = new TreeSet<Integer>();
            for (int i = 0; i < n; i++) {
                tmp.add(i);
            }
            tmp.removeAll(A);

            // this convoluted way of doing things is so that elements can be 
            // removed while iterating over the collection.
            // No synchronized blocks and such didn't work
            List<Integer> c = new ArrayList<Integer>();
            c.addAll(tmp);
            
            int index = 0;
            while (index < c.size()) {
                int x = c.get(index);
                SortedSet<Integer> b = new TreeSet<Integer>();
                b.addAll(A);
                b.add(x);
                SortedSet<Integer> a = minRep(b);
                if (!S.contains(a)) {
                    S.add(a);
                }
                int k = 0;
                for (int y : A) {
                    beta.set(k, y);
                    k++;
                }
                beta.set(k, x);
                int j = k + 1;
                for (int y = 0; y < n; y++) {
                    if (y != x && !A.contains(y)) {
                        beta.set(j, y);
                    }
                }
                this.group.changeBase(beta);
                for (int y = 0; y < n; y++) {
                    if (this.group.get(k, y) != null && c.contains(y)) {
                        c.remove(new Integer(y));
                    }
                }
                index++;
            }
        }
        return S;
    }
    
    public SortedSet minRep(SortedSet set) {
        
        // copy the contents of the set into the orbit options, 
        // k will be the size of the set 
        int k = 0;
        Orbit o = new Orbit(this.n);
        for (int i = 0; i < this.n; i++) {
            if (set.contains(i)) {
                o.addOption(i);
                k++;
            }
        }
        System.out.println("backtracking over " + set);
        
        // backtrack over the permutations
        minRepBacktrack(0, new SetList(set), o);
        
        // now, copy the representatives back into a new set
        SortedSet representatives = new TreeSet();
        representatives.addAll(o.getOptions());
        return representatives;
    }
    
    private void minRepBacktrack(int l, SetList c, Orbit o) {
        int start;
        int k = o.getNumberOfOptions();
        if (l == 0) {
            start = 0;
        } else {
            start = o.X[l - 1];
        }
        
        int m = this.n;
        for (int x = start; x < n; x++) {
            if (c.contains(x, l)) {
                int r = 0;
                while ((r < l) && o.X[r] == o.getOption(r)) r++;
                if (r < l && o.X[r] >= o.getOption(r)) return;
                o.X[l] = x;
                setPermutation(l + 1, o.X, o.beta);
                this.group.changeBase(o.beta);
                
                // find the permutation in Ul : g[x] is smallest
                Permutation g = null;
                for (int j = 0; j <= x; j++) {
                    g = this.group.get(l, j);
                    if (g != null) break;
                }
                
                if (g == null) return;  // :(
                
                if (g.get(x) <= m) {
                    m = g.get(x);
                    o.X[l] = m;
                    
                    if (k == l + 1) {
                        int i;
                        for (i = r; i < k && o.X[i] == o.getOption(i); i++);
                        if (i != k && o.X[i] < o.getOption(i)) {
                            for (i = 0; i< k; i++) o.setOption(i, o.X[i]);
                        }
                    } else {
                        SortedSet<Integer> blockL = c.get(l);
                        SortedSet<Integer> blockL1 = g.apply(blockL);
                        blockL1.remove(m);
                        c.add(blockL1);
                        
                        minRepBacktrack(l + 1, c, o);
                    }
                }
            }
        }
    }
    
    private void setPermutation(int k, int[] A, Permutation g) {
        System.out.print("setting " + g  
                        + " to " + Arrays.toString(A) 
                        + " up to " + k);
        Set<Integer> s = new HashSet<Integer>();
        int i;  // this is used in the next loop
        for (i = 0; i < k; i++) {
            s.add(A[i]);
            g.set(i, A[i]);
        }
        System.out.print(" " + s);
        for (int j = 0; j < n; j++) {
            if (!s.contains(j)) {
                System.out.print(" (" + i + " " + j + ") ");
                if (i < n) {    // XXX
                    g.set(i, j);
                }
                i++;
            }
        }
        System.out.print("\n");
    }

}
