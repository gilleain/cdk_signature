package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.openscience.cdk.graph.AtomContainerAtomPermutor;
import org.openscience.cdk.group.CDKDiscretePartitionRefiner;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.MoleculeFromSignatureBuilder;
import org.openscience.cdk.signature.MoleculeSignature;
import org.openscience.cdk.signature.Orbit;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import signature.AbstractVertexSignature;

public class CanonicalChecker {
    
    private static CDKDiscretePartitionRefiner discreteRefiner = 
        new CDKDiscretePartitionRefiner(true);
    
    public static boolean isCanonicalByMagic(IAtomContainer ac) {
        // make a partition of connected atoms, but not compact
        Partition sigPart = new Partition();
        Map<String, Integer> sigBlockMap = new HashMap<String, Integer>();
        MoleculeSignature molSig = new MoleculeSignature(ac);
        for (int i = 0; i < ac.getAtomCount(); i++) {
            IAtom atom = ac.getAtom(i);
            if (ac.getConnectedAtomsCount(atom) > 0) {
                String sig = molSig.signatureStringForVertex(i);
                if (sigBlockMap.containsKey(sig)) {
                    int block = sigBlockMap.get(sig);
                    sigPart.addToCell(block, i);
                } else {
                    sigPart.addSingletonCell(i);
                }
            }
        }
        
        // make the initial edge string, and permute
        int atomCount = ac.getAtomCount();
        Permutation identity = new Permutation(atomCount);
        String initialEdgeString = edgeString(ac, identity);
        for (int i = 0; i < sigPart.size(); i++) {
            SortedSet<Integer> cell = sigPart.getCell(i);
            int cellSize = cell.size();
            if (cellSize == 1) continue;
            
            // could cache these
            SSPermutationGroup symN = SSPermutationGroup.makeSymN(cellSize);
            
            for (Permutation permutation : symN.all()) {
                
                // could have a better name, and be more efficiently made
                Permutation fullPermutation = new Permutation(atomCount);
                int cellIndex = 0;
                for (int j : cell) {
                    fullPermutation.set(j, permutation.get(cellIndex));
                    cellIndex++;
                }
                
                // check to see if this permutation makes a more minimal string
                String permutedString = edgeString(ac, fullPermutation);
                if (permutedString.compareTo(initialEdgeString) < 0) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static AbstractVertexSignature getCanonicalSignature(
            IAtomContainer graph) {
        MoleculeSignature signature = new MoleculeSignature(graph);
        AbstractVertexSignature canonicalSignature = null;
        String canonicalString = null;
        for (int i = 0; i < graph.getAtomCount(); i++) {
            if (graph.getConnectedAtomsCount(graph.getAtom(i)) == 0) continue;
            AbstractVertexSignature avs = signature.signatureForVertex(i);
            String sigString = avs.toCanonicalString();
            if (canonicalString == null || 
                    sigString.compareTo(canonicalString) < 0) {
                canonicalSignature = avs;
                canonicalString = sigString;
                System.out.println(
                       "setting canonical sig " + i + " to " + canonicalString);
            }
        }
        return canonicalSignature;
    }
    
    public static int[] getLabels(IAtomContainer graph) {
        AbstractVertexSignature canon = getCanonicalSignature(graph);
        int n = graph.getAtomCount();
        CanonicalDAGVisitor cdv = new CanonicalDAGVisitor(n);
        canon.accept(cdv);
        int[] labels = new int[n];
        Arrays.fill(labels, -1);
        int x = 0;
        for (int i = 0; i < n; i++) {
            if (graph.getConnectedAtomsCount(graph.getAtom(i)) == 0) continue;
            int j = canon.getOriginalVertexIndex(cdv.labels[x]); 
            System.out.print(x + "->" + cdv.labels[x] + "->" + j + " ");
            labels[j] = x;
            x++;
        }
        System.out.println();
        System.out.println(Arrays.toString(cdv.labels) + " " + Arrays.toString(labels));
        return labels;
    }
    
    public static boolean permutationInOrder(int[] permutation) {
        if (permutation.length < 1) return true;
        int prev = permutation[0];
        for (int i = 1; i < permutation.length; i++) {
            if (permutation[i] == -1) continue;
            if (permutation[i] < prev) return false;
            prev = permutation[i];
        }
        return true;
    }
    
    public static boolean isCanonicalByVisitor(IAtomContainer atomContainer) {
        return permutationInOrder(getLabels(atomContainer));
    }
    
    public static boolean isCanonicalByReconstruction(IAtomContainer atomContainer) {
        MoleculeSignature moleculeSignature = new MoleculeSignature(atomContainer);
        MoleculeFromSignatureBuilder builder = 
            new MoleculeFromSignatureBuilder(
                    NoNotificationChemObjectBuilder.getInstance());
        moleculeSignature.reconstructCanonicalGraph(
                moleculeSignature.signatureForVertex(0), builder);
        IAtomContainer reconstruction = builder.getAtomContainer();
        return CanonicalChecker.identical(atomContainer, reconstruction);
    }
    
    public static boolean identical(IAtomContainer a, IAtomContainer b) {
        // assume that atom and bond counts are equal...
        for (IBond bondA : a.bonds()) {
            int atomZero = a.getAtomNumber(bondA.getAtom(0));
            int atomOne = a.getAtomNumber(bondA.getAtom(1));
            if (CanonicalChecker.containedIn(atomZero, atomOne, b)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    public static boolean containedIn(
            int atomZero, int atomOne, IAtomContainer atomContainer) {
        for (IBond bond : atomContainer.bonds()) {
            int atomZeroPrime = atomContainer.getAtomNumber(bond.getAtom(0));
            int atomOnePrime = atomContainer.getAtomNumber(bond.getAtom(1));
            if (atomZero == atomZeroPrime && atomOne == atomOnePrime 
                    ||atomOne == atomZeroPrime && atomZero == atomOnePrime) {
                return true;
            }
        }
        return false;
    }
    
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
    
    public static void isCanonicalTest(IAtomContainer atomContainer) {
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
        AtomContainerAtomPermutor permutor = new AtomContainerAtomPermutor(atomContainer);
        while (permutor.hasNext()) {
            IAtomContainer permuted = permutor.next();
            boolean canonical = refiner.isCanonical(permuted);
            if (canonical) {
//                String p = Arrays.toString(permutor.getCurrentPermutation());
////                System.out.println(p + " is canonical? " + canonical);
//                System.out.println(p + " " + edgeString(permuted, new Permutation(n)));
            }
        }
    }
    
    public static boolean isMinimalTest(final IAtomContainer atomContainer) {
        final CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner(); 
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
    
    public static boolean degreeOrdered(IAtomContainer atomContainer) {
        if (atomContainer.getAtomCount() < 2) return true;
        IAtom first = atomContainer.getAtom(0);
        double previousDegree = 
            AtomContainerManipulator.getBondOrderSum(atomContainer, first);
//            atomContainer.getConnectedAtomsCount(atomContainer.getAtom(0));
        for (int i = 1; i < atomContainer.getAtomCount(); i++) {
            IAtom atom = atomContainer.getAtom(0);
            double degree = 
                AtomContainerManipulator.getBondOrderSum(atomContainer, atom);
//                atomContainer.getConnectedAtomsCount(atomContainer.getAtom(i));
            if (degree <= previousDegree) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isCanonical(IAtomContainer atomContainer) {
        return new CDKDiscretePartitionRefiner(false, true).isCanonical(atomContainer);
    }
    
    public static boolean isCanonicalWithGaps(IAtomContainer atomContainer) {
        return new CDKDiscretePartitionRefiner(true, true).isCanonical(atomContainer);
    }
    
    public static void searchForSignaturePartition(IAtomContainer atomContainer) {
        AtomContainerAtomPermutor permutor = new AtomContainerAtomPermutor(atomContainer);
        while (permutor.hasNext()) {
            IAtomContainer permuted = permutor.next();
            Partition partition = CanonicalChecker.signaturePartition(permuted);
            if (partition.inOrder()) {
//                String pstr = Arrays.toString(permutor.getCurrentPermutation());
//                System.out.println(pstr + " " + partition);
            }
        }
    }
    
    public static Partition signaturePartition(IAtomContainer atomContainer) {
        MoleculeSignature signature = new MoleculeSignature(atomContainer);
        List<Orbit> orbits = signature.calculateOrbits();
        Map<String, Orbit> orbitMap = new HashMap<String, Orbit>();
        for (Orbit o : orbits) {
            orbitMap.put(o.getLabel(), o);
        }
        List<String> keys = new ArrayList<String>();
        keys.addAll(orbitMap.keySet());
        Collections.sort(keys);
        Partition partition = new Partition();
        for (String key : keys) {
            Orbit o = orbitMap.get(key);
//            System.out.println(o.getAtomIndices() + "\t" + key);
            partition.addCell(o.getAtomIndices());
        }
//        System.out.println(partition);
        return partition;
    }
    
    public static Partition compactSignaturePartition(IAtomContainer atomContainer) {
      MoleculeSignature signature = new MoleculeSignature(atomContainer);
      int compactIndex = 0;
  
      Map<String, Integer> signatureBlockMap = new HashMap<String, Integer>();
      int maxBlock = 0;
      Partition partition = new Partition();
      for (int i = 0; i < atomContainer.getAtomCount(); i++) {
          IAtom atom = atomContainer.getAtom(i);
          if (atomContainer.getConnectedAtomsCount(atom) > 0) {
              String signatureStringForAtom = 
                  signature.signatureStringForVertex(i);
              if (signatureBlockMap.containsKey(signatureStringForAtom)) {
                  int blockIndex = signatureBlockMap.get(signatureStringForAtom);
                  partition.addToCell(blockIndex, compactIndex);
              } else {
                  SortedSet<Integer> block = new TreeSet<Integer>();
                  block.add(compactIndex);
                  partition.addCell(block);
                  signatureBlockMap.put(signatureStringForAtom, maxBlock);
                  maxBlock++;
              }
              compactIndex++;
          }
      }
//      System.out.println("signature partition " + partition + " " + signatureBlockMap.keySet());
//      System.out.println("signature partition " + partition);
      return partition;
    }
    
    public static boolean isCanonicalWithSignaturePartition(
            IAtomContainer atomContainer) {
        Partition initial = CanonicalChecker.signaturePartition(atomContainer);
        if (initial.size() < 2) return true;
        System.out.println(initial);
        CDKDiscretePartitionRefiner refiner = 
            new CDKDiscretePartitionRefiner(true);
        refiner.refine(initial, atomContainer);
        return refiner.firstIsIdentity();
    }
    
    public static boolean isCanonicalWithCompactSignaturePartition(
            IAtomContainer atomContainer) {
        Partition initial = 
            CanonicalChecker.compactSignaturePartition(atomContainer);
        
        CDKDiscretePartitionRefiner refiner = 
            new CDKDiscretePartitionRefiner(true);
        Partition initialClone = new Partition(initial);
        refiner.refine(initial, atomContainer);
        boolean canon = refiner.firstIsIdentity();
        System.out.println(canon + " " + initialClone);
        
//        return canon && signaturesOrdered(atomContainer);
        return canon;
//        return true;
    }
    
    public static boolean signaturesOrdered(IAtomContainer atomContainer) {
        String prev = null;
        MoleculeSignature molSig = new MoleculeSignature(atomContainer);
        for (int i = 0; i < atomContainer.getAtomCount(); i++) {
            int c = atomContainer.getConnectedAtomsCount(atomContainer.getAtom(i)); 
            if (c > 0) {
                String sig = molSig.signatureStringForVertex(i);
                if (prev == null) {
                    System.out.println("prev " + prev + " curr " + sig);
                } else {
                    boolean ordered = ordered(prev, sig);
                    System.out.println("prev " + prev + " curr " + sig + " " + ordered);
                }
                if (prev == null || ordered(prev, sig)) {
                    prev = sig;
                } else {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean ordered(String a, String b) {
        return a.compareTo(b) >= 0;
    }

    public static boolean isCanonicalWithColorPartition(
            IAtomContainer atomContainer) {
        Map<Integer, SortedSet<Integer>> colorBlocks = 
            new HashMap<Integer, SortedSet<Integer>>();
        Map<String, Integer> stringColorMap = new HashMap<String, Integer>();
        int maxColor = 0;
        int compactIndex = 0;
        for (int i = 0; i < atomContainer.getAtomCount(); i++) {
            IAtom atom = atomContainer.getAtom(i);
            if (atomContainer.getConnectedAtomsCount(atom) == 0) continue;
            String symbol = atom.getSymbol();
            if (stringColorMap.containsKey(symbol)) {
                int color = stringColorMap.get(symbol);
                colorBlocks.get(color).add(compactIndex);
            } else {
                int color = maxColor;
                stringColorMap.put(symbol, color);
                SortedSet<Integer> block = new TreeSet<Integer>();
                block.add(compactIndex);
                colorBlocks.put(color, block);
                maxColor++;
            }
            compactIndex++;
        }
        Partition initial = new Partition();
        for (int color : colorBlocks.keySet()) {
            initial.addCell(colorBlocks.get(color));
        }
        if (initial.size() == 0) return true;
        
        CDKDiscretePartitionRefiner refiner = 
//            new CDKDiscretePartitionRefiner(true, false);   // XXX turning off bond orders!
//            new CDKDiscretePartitionRefiner(false, true);
            new CDKDiscretePartitionRefiner(true, true);
        refiner.refine(initial, atomContainer);
        return refiner.firstIsIdentity();
    }

}
