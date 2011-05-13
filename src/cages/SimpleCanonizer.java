package cages;

import java.util.Iterator;
import java.util.Set;

import org.openscience.cdk.group.IEquitablePartitionRefiner;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;

import engine.Graph;

import test_group.TestEquitablePartitionRefiner;


/**
 * The canonizer (or discrete partition refiner) corresponding to algorithm 7.7
 * from the CAGES book. It does not prune by automorphisms.
 * 
 * @author maclean
 *
 */
public class SimpleCanonizer {
    
    public enum Result { WORSE, EQUAL, BETTER };
    
    private boolean bestExist;
    
    private Permutation best;
    
    private IEquitablePartitionRefiner refiner;
    
    private Graph graph;
    
    public SimpleCanonizer(int n) {
        this.bestExist = false;
        this.best = null;
        this.refiner = null;
    }
    
    public Permutation getBest() {
        if (best == null) {
            return new Permutation(this.graph.getVertexCount());
        } else {
            return this.best;
        }
    }
    
    public boolean canon(Graph graph) {
        int n = graph.getVertexCount();
        this.refiner = new TestEquitablePartitionRefiner(graph);
        this.graph = graph;
        this.bestExist = false;
        Partition unit = Partition.unit(n);
        canon(unit);
        return bestExist;
    }
    
    public boolean canon(Graph graph, Partition initialPartition) {
        this.refiner = new TestEquitablePartitionRefiner(graph);
        this.graph = graph;
        this.bestExist = false;
        canon(initialPartition);
        return bestExist;
    }
    
    public int getCertificate() {
        return calculateCertificate(this.getBest());
    }
    
    public int calculateCertificate(Permutation p) {
        int k = 0;
        int certificate = 0;
        int n = this.graph.getVertexCount();
        for (int i = n - 1; i > 0; i--) {
            for (int j = i - 1; j > -1; j--) {
                if (graph.isConnected(p.get(i), p.get(j))) {
                    certificate = (int)Math.pow(2, k); 
                }
                k++;
            }
        }
        return certificate;
    }
    
    public void canon(Partition p) {
        int n = graph.getVertexCount();
        
        Partition q = this.refiner.refine(p);
        int l = q.getIndexOfFirstNonDiscreteCell();
        if (l == -1) {
//            System.out.println(q.toPermutation());
//            return;
            l = n;
        }
        
        Result result = Result.BETTER;
        Permutation pi1 = new Permutation(l);
        if (this.bestExist) {
            q.setAsPermutation(pi1, l);
            result = compare(best, pi1, l);
            System.out.println("Compare(" + best + ", " + pi1 + ") = " + result);
        }
        
        if (q.size() == n) {
            if (!this.bestExist) {
                this.best = q.toPermutation();
                this.bestExist = true;
                System.out.println("Setting best(first time) to " + this.best);
            } else {
                if (result == Result.BETTER) {
                    this.best = new Permutation(pi1);
                    System.out.println("Setting best to " + this.best);
                } else if (result == Result.EQUAL) {
                    System.out.println("EQUAL " + best + " " + q.toPermutation());
                } else {
                    System.out.println("WORSE " + best + " " + q.toPermutation());
                }
            }
        } else {
            if (result != Result.WORSE) {
                Set<Integer> c = q.copyBlock(l);
                Iterator<Integer> itr = c.iterator(); 
                while (itr.hasNext()) {
                    canon(q.splitBefore(l, itr.next()));
                }
            }
        }
        
    }
    
    public Result compare(Permutation a, Permutation b, int m) {
        // m == perm.size?
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < i; j++) {
                int x = isAdjacent(a.get(i), a.get(j));
                int y = isAdjacent(b.get(i), b.get(j));
                if (x > y) return Result.BETTER;
                if (x < y) return Result.WORSE;
            }
        }
        return Result.EQUAL;
    }

    private int isAdjacent(int i, int j) {
        if (this.graph.isConnected(i, j)) { 
            return 1; 
        } else {
            return 0;
        }
    }

}
