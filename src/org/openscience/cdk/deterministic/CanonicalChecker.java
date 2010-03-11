package org.openscience.cdk.deterministic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openscience.cdk.group.CDKDiscretePartitionRefiner;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

public class CanonicalChecker {
    
    private static CDKDiscretePartitionRefiner discreteRefiner = 
        new CDKDiscretePartitionRefiner(false);
    
    public static boolean isCanonicalBroken(IAtomContainer atomContainer) {
//        // clear any previous labels
//        for (IAtom a : atomContainer.atoms()) {
//            a.setProperty(InvPair.CANONICAL_LABEL, null);
//        }
//        
//        int[] labels = null;
////        labels = CanonicalLabeler.canonicallyLabel(atomContainer);
////        new CanonicalLabeler().canonLabel(atomContainer);
//        
//        int l = 0;
//        for (int i = 1; i < atomContainer.getAtomCount(); i++) {
//            if (labels[l] != i) {
//                System.out.println("not canon " + Arrays.toString(labels));
//                return false;
//            }
//            l++;
//        }
//        
        return true;
    }
    
    public static String edgeString(IAtomContainer container, Permutation p) {
        List<Bond> bonds = new ArrayList<Bond>();
        for (IBond bond : container.bonds()) {
            IAtom a1 = bond.getAtom(0);
            IAtom a2 = bond.getAtom(1);
            int a1N = p.get(container.getAtomNumber(a1));
            int a2N = p.get(container.getAtomNumber(a2));
            String s1 = a1.getSymbol();
            String s2 = a2.getSymbol();
            bonds.add(new Bond(a1N, a2N, s1, s2, bond.getOrder().ordinal()));
            
        }
        Collections.sort(bonds);
        StringBuffer sb = new StringBuffer();
        for (Bond b : bonds) {
            sb.append(b).append(',');
        }
        return sb.toString();
    }
    
    public static String getBest(IAtomContainer atomContainer) {
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
        refiner.getAutomorphismGroup(atomContainer);
        return edgeString(atomContainer, refiner.getBest());
    }
    
    public static boolean isCanonicalTest(final IAtomContainer atomContainer) {
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
        final int n = atomContainer.getAtomCount();
        System.out.println("N = " + n);
        if (n < 2) return true;
        final SSPermutationGroup autG = refiner.getAutomorphismGroup(atomContainer);
        System.out.println("autG size " + autG.order());
        SSPermutationGroup symN = SSPermutationGroup.makeSymN(n);
        System.out.println("symN size " + symN.order());
        final String original = edgeString(atomContainer, new Permutation(n));
        final int m = symN.order() / autG.order();
        final List<Permutation> results = new ArrayList<Permutation>();
        System.out.println("original " + original);
        
        symN.apply(new SSPermutationGroup.Backtracker() {
            int x = 0;
            int r = 1000;
            boolean isFinished = false;
            String best = original;
            
            public void applyTo(Permutation p) {
                if (x % r == 0) { System.out.print("."); }
                String permuted = edgeString(atomContainer, p);
                int c = best.compareTo(permuted); 
                if (c > 0) {
                    System.out.println("smaller " + permuted + " " + c + " " + p);
                    best = permuted;
                    isFinished = true;
                }
                x++;
                
                for (Permutation f : results) {
                    Permutation h = f.invert().multiply(p);
                    if (autG.test(h) == n) {
                        return;
                    }
                }
                results.add(p);
                if (results.size() >= m) {
                    this.isFinished = true;
                }
            }
            
            public boolean finished() {
                return isFinished;
            }
        });
        return true;

    }
    
    public static boolean isCanonicalByRefinement(FragmentGraph graph) {
        if (graph.getAtomContainer().getAtomCount() < 3) return true;
        return CanonicalChecker.discreteRefiner.isCanonical(graph.getAtomContainer());
    }
    
    public static boolean isCanonical(FragmentGraph graph) {
//        return CanonicalChecker.discreteRefiner.isCanonical(graph.getAtomContainer());
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
//        return refiner.isCanonical(graph.getAtomContainer());
        IAtomContainer atomContainer = graph.getAtomContainer();
        int n = atomContainer.getAtomCount();
        if (n < 2) return true;
        SSPermutationGroup autG = refiner.getAutomorphismGroup(atomContainer);
        SSPermutationGroup symN = SSPermutationGroup.makeSymN(n);
        String original = edgeString(atomContainer, new Permutation(n));
        for (Permutation p : symN.transversal(autG)) {
            String permuted = edgeString(atomContainer, p);
            if (original.compareTo(permuted) > 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean edgesInOrder(IAtomContainer atomContainer) {
        int prevA = -1;
        int prevB = -1;
        for (IBond bond : atomContainer.bonds()) {
            int aN = atomContainer.getAtomNumber(bond.getAtom(0));
            int bN = atomContainer.getAtomNumber(bond.getAtom(1));
            if (prevA == -1 && prevB == -1) {
                if (aN < bN) {
                    prevA = aN;
                    prevB = bN;
                } else {
                    prevA = bN;
                    prevB = aN;
                }
            } else {
                if (aN < bN) {
                    if (prevA < aN || (prevA == aN && prevB < bN)) {
                        prevA = aN;
                        prevB = bN;
                    } else {
                        return false;
                    }
                } else {
                    if (prevA < bN || (prevA == bN && prevB < aN)) {
                        prevA = bN;
                        prevB = aN;
                    } else {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean isCanonical(IAtomContainer atomContainer) {
        return new CDKDiscretePartitionRefiner(false).isCanonical(atomContainer);
    }
    
    public static boolean isCanonicalWithGaps(IAtomContainer atomContainer) {
        return new CDKDiscretePartitionRefiner(true).isCanonical(atomContainer);
    }

}
