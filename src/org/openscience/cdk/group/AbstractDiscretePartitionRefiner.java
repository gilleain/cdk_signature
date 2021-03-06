package org.openscience.cdk.group;

import java.util.Set;

/**
 * Refines vertex partitions until they are discrete, and therefore equivalent
 * to permutations. These permutations are automorphisms of the graph that was
 * used during the refinement to guide the splitting of partition blocks. 
 * 
 * @author maclean
 * @cdk.module group
 */
public abstract class AbstractDiscretePartitionRefiner {
    
    public enum Result { WORSE, EQUAL, BETTER };
    
    private boolean bestExist;
    
    private Permutation best;
    
    private Permutation first;
    
    private IEquitablePartitionRefiner refiner;
    
    private SSPermutationGroup group;
    
    private boolean checkVertexColors;
    
    private boolean checkEdgeColors;
    
    public AbstractDiscretePartitionRefiner() {
        this(false);
    }
    
    public AbstractDiscretePartitionRefiner(boolean checkVetexColors) {
      this(checkVetexColors, false);
    }
    
    public AbstractDiscretePartitionRefiner(
            boolean checkVetexColors, boolean checkEdgeColors) {
        this.bestExist = false;
        this.best = null;
        this.refiner = null;
        this.checkVertexColors = checkVetexColors;
        this.checkEdgeColors = checkEdgeColors;
    }
    
    public abstract int getVertexCount();
    
    public abstract boolean isConnected(int i, int j);
    
    public abstract boolean sameVertexColor(int i, int j);
    
    public abstract boolean sameEdgeColor(int iOld, int jOld, int iNew, int jNew);
    
    public void setup(SSPermutationGroup group, IEquitablePartitionRefiner refiner) {
        this.bestExist = false;
        this.best = null;
        this.group = group;
        this.refiner = refiner;
    }
    
    public boolean firstIsIdentity() {
        return this.first.isIdentity();
    }
    
    public int getCertificate() {
        return calculateCertificate(this.getBest());
    }
    
    public int calculateCertificate(Permutation p) {
        int k = 0;
        int certificate = 0;
        int n = getVertexCount();
        for (int i = n - 1; i > 0; i--) {
            for (int j = i - 1; j > -1; j--) {
                if (isConnected(p.get(i), p.get(j))) {
                    certificate = (int)Math.pow(2, k);
                }
                k++;
            }
        }
        return certificate;
    }
    
    public String getHalfMatrixString(Permutation p) {
        String hms = "";
        int n = p.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected(p.get(i), p.get(j))) {
                    hms += "1";
                } else {
                    hms += "0";
                }
            }
        }
        return hms;
    }
    
    public String getBestHalfMatrixString() {
       return getHalfMatrixString(best);
    }
    
    public String getFirstHalfMatrixString() {
        return getHalfMatrixString(first);
     }
    
    public String getHalfMatrixString() {
        return getHalfMatrixString(new Permutation(getVertexCount()));
    }
    
    public SSPermutationGroup getGroup() {
        return this.group;
    }
    
    public Permutation getBest() {
        return this.best;
    }
    
    public Permutation getFirst() {
        return this.first;
    }
    
    /**
     * Check for a canonical graph, without generating the whole 
     * automorphism group.
     * 
     * @return true if the graph is canonical
     */
    public boolean isCanonical() {
        return isCanonical(Partition.unit(getVertexCount()));
    }
    
    public boolean isCanonical(Partition partition) {
        int n = getVertexCount();
        if (partition.size() == n) {
            return partition.toPermutation().isIdentity();
        } else {
            int l = partition.getIndexOfFirstNonDiscreteCell();
            int first = partition.getFirstInCell(l);
            Partition finerPartition = 
                refiner.refine(partition.splitBefore(l, first));
            return isCanonical(finerPartition);
        }
    }
    
    public void refine(Partition p) {
        refine(this.group, p);
    }
    
    public void refine(SSPermutationGroup group, Partition p) {
        int n = getVertexCount();
        
        Partition q = this.refiner.refine(p);
        
        int l = q.getIndexOfFirstNonDiscreteCell();
        if (l == -1) {
            l = n;
        }
        
        Permutation pi1 = new Permutation(l);
        Permutation pi2 = new Permutation(n);
        
        Result result = Result.BETTER;
        if (bestExist) {
            q.setAsPermutation(pi1, l);
            result = compareRowwise(pi1);
        }
        
        if (q.size() == n) {    // partition is discrete
            if (!bestExist) {
                best = q.toPermutation();
                first = q.toPermutation();
                bestExist = true;
            } else {
                if (result == Result.BETTER) {
                    best = new Permutation(pi1);
                } else if (result == Result.EQUAL) {
                    pi2 = pi1.multiply(best.invert());
                    if ((!checkVertexColors || vertexColorsAutomorphic(pi2))
                        && (!checkEdgeColors || edgeColorsAutomorphic(pi2))) {
                        group.enter(pi2);
                    }
                }
            }
        } else {
            if (result != Result.WORSE) {
                Set c = q.copyBlock(l);
                for (int u = 0; u < n; u++) {
                    if (c.contains(u)) {
                        Partition r = q.splitBefore(l, u);
                        
                        this.refine(group, r);
                        
                        Permutation f = new Permutation(n);
                        Permutation invF = new Permutation(n);
                        
                        for (int j = 0; j <= l; j++) {
                            int x = r.getFirstInCell(j);
                            int i = invF.get(x);
                            int h = f.get(j);
                            f.set(j, x);
                            f.set(i, h);
                            invF.set(h, i);
                            invF.set(x, j);
                        }
                        group.changeBase(f);
                        for (int j = 0; j < n; j++) {
                            Permutation g = group.get(l, j);
                            if (g != null) c.remove(g.get(u));
                        }
                    }
                }
            }
        }
    }
    
    private boolean vertexColorsAutomorphic(Permutation p) {
        for (int i = 0; i < p.size(); i++) {
            if (sameVertexColor(i, p.get(i))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    private boolean edgeColorsAutomorphic(Permutation p) {
        for (int i = 0; i < p.size(); i++) {
            if (sameVertexColor(i, p.get(i))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Check a permutation to see if it is better, equal, or worse than the 
     * current best.
     * 
     * @param perm the permutation to check
     * @return BETTER, EQUAL, or WORSE
     */
    public Result compareColumnwise(Permutation perm) {
        int m = perm.size();
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < i; j++) {
                int x = isAdjacent(best.get(i), best.get(j));
                int y = isAdjacent(perm.get(i), perm.get(j));
                if (x > y) return Result.WORSE;
                if (x < y) return Result.BETTER;
            }
        }
        return Result.EQUAL;
    }
    
    public Result compareRowwise(Permutation perm) {
        int m = perm.size();
        for (int i = 0; i < m - 1; i++) {
            for (int j = i + 1; j < m; j++) {
                int x = isAdjacent(best.get(i), best.get(j));
                int y = isAdjacent(perm.get(i), perm.get(j));
                if (x > y) return Result.WORSE;
                if (x < y) return Result.BETTER;
            }
        }
        return Result.EQUAL;
    }

    private int isAdjacent(int i, int j) {
        if (isConnected(i, j)) { 
            return 1; 
        } else {
            return 0;
        }
    }

}
