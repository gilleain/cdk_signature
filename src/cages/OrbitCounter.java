package cages;

import java.util.Arrays;

import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;

/**
 * A broken implementation/port of the procedure to count the number of 
 * orbits of k-element subsets for k =0, 1, 2, ...,|X| for a set X.
 * 
 * @author maclean
 *
 */
public class OrbitCounter implements SSPermutationGroup.Backtracker {
    
    private int[] V1;
    
    private int[] V2;
    
    private int[] V3;
    
    private SSPermutationGroup group;
    
    private int n;
    
    public OrbitCounter(int n, SSPermutationGroup group) {
        this.V1 = new int[n + 1];
        this.V2 = new int[n + 1];
        this.V3 = new int[n + 1];
        this.group = group;
        this.n = n;
    }
    
    public int[] getV3() {
        return this.V3;
    }
    
    public int[] count() {
        Arrays.fill(this.V3, 0);
        int[] counts = new int[n];
        this.group.apply(this);
        int gSize = this.group.order();
        for (int k = 0; k < n; k++) {
            counts[k] = V3[k] / gSize;
        }
        return counts;
    }
    
    private void recPartition(int k, int m, int b, int x) {
        if (m == 0) {
            int[] c = new int[this.n + 1];
            for (int i = 1; i < x; i++) {
                c[this.V2[i]]++;
            }
            int prod = 1;
            for (int i = 1; i <= this.n; i++) {
                prod *= choose(V1[i], c[i]);
            }
            this.V3[k] += prod;
        } else {
            for (int i = 1; i <= Math.min(b, m); i++) {
                this.V2[x + 1] = i;
                this.recPartition(k, m - i, i, x + 1);
            }
        }
    }
    
    /**
     * Compute the binomial coefficient N choose K.
     * 
     * @param N
     * @param K
     * @return
     */
    private int choose(int N, int K) {
        if (N < K) {
            return 0;
        }
        int k = (N - K < K) ? N - K : K;
        int ans = 1;
        for (int i = 1; i <= k; i++) {
            ans = (ans * (N + 1 - i)) / i;
        }
        return ans;
    }

    public void applyTo(Permutation p) {
        this.V1 = p.getType();
        for (int k = 0; k <= n; k++) {
            this.V2[1] = k;
            this.recPartition(k, k, k, 0);
        }
    }

    public boolean finished() {
        return false;
    }

}
