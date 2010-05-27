package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.signature.MoleculeSignature;

import signature.AbstractVertexSignature;

public class AlternateCanonicalChecker {
    
    public static boolean isCanonicalByVisitor(IAtomContainer atomContainer) {
        return listInOrder(getLabels(atomContainer));
    }
    
    public static List<AbstractVertexSignature> getSignatureList(
            IAtomContainer container) {
        List<AbstractVertexSignature> signatureList = 
            new ArrayList<AbstractVertexSignature>();
        MoleculeSignature signature = new MoleculeSignature(container);
        int n = container.getAtomCount();
        List<List<Integer>> visitedLists = new ArrayList<List<Integer>>();
        for (int i = 0; i < n; i++) {
            if (inVisitedLists(i, visitedLists) 
                || container.getConnectedAtomsCount(
                        container.getAtom(i)) == 0) {
                continue;
            } else {
                AbstractVertexSignature avs = signature.signatureForVertex(i);
                visitedLists.add(getVisited(n, avs));
                signatureList.add(avs);
            }
            System.out.println("i = " + i + " visited = " + visitedLists);
        }
        return signatureList;
    }
    
    public static boolean listsInOrder(List<List<Integer>> lists) {
        // a single list is always in order
        if (lists.size() <= 1) return true;
        
        // 
        for (int i = 0; i < lists.size(); i++) {
            
        }
        return true;
    }
    
    public static boolean inVisitedLists(int i, List<List<Integer>> lists) {
        for (List<Integer> visitedList : lists) {
            if (visitedList.contains(i)) {
                return true;
            }
        }
        return false;
    }
    
    // TODO : convert this to an AbstractVertexSignature method
    public static List<Integer> getVisited(int n, AbstractVertexSignature avs) {
        List<Integer> visited = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            int originalIndex = avs.getOriginalVertexIndex(i); 
            if (originalIndex != -1) {
                visited.add(originalIndex);
            }
        }
        return visited;
    }

    public static AbstractVertexSignature getCanonicalSignature(
            IAtomContainer graph) {
        MoleculeSignature signature = new MoleculeSignature(graph);
        AbstractVertexSignature canonicalSignature = null;
        String canonicalString = null;
        for (int i = 0; i < signature.getVertexCount(); i++) {
            if (graph.getConnectedAtomsCount(graph.getAtom(i)) == 0) continue;
            AbstractVertexSignature avs = signature.signatureForVertex(i);
            String sigString = avs.toCanonicalString();
            if (canonicalString == null || 
                    sigString.compareTo(canonicalString) < 0) {
                canonicalSignature = avs;
                canonicalString = sigString;
//                System.out.println(
//                       "setting canonical sig " + i + " to " + canonicalString);
            }
        }
        return canonicalSignature;
    }
    
    public static int[] getLabels(IAtomContainer graph) {
        AbstractVertexSignature canon = getCanonicalSignature(graph);
        int n = canon.getVertexCount();
        CanonicalDAGVisitor cdv = new CanonicalDAGVisitor(n);
        canon.accept(cdv);
        int[] labels = new int[n];
        Arrays.fill(labels, -1);
        int internalIndex = 0;
        for (int atomIndex = 0; atomIndex < n; atomIndex++) {
            if (graph.getConnectedAtomsCount(graph.getAtom(atomIndex)) == 0) {
                continue;
            }
            int externalIndex = canon.getOriginalVertexIndex(internalIndex); 
            System.out.print(internalIndex + "->" 
                    + cdv.labels[internalIndex] + "->" + externalIndex + " ");
            labels[externalIndex] = cdv.labels[internalIndex];
            internalIndex++;
        }
//        System.out.println();
        System.out.println(Arrays.toString(cdv.labels) 
                + " " + Arrays.toString(labels));
        return labels;
    }
    
    public static boolean listInOrder(int[] intList) {
        if (intList.length < 2) return true;
        int prev = intList[0];
        for (int i = 1; i < intList.length; i++) {
            if (intList[i] == -1) continue;
            if (intList[i] < prev) return false;
            prev = intList[i];
        }
        return true;
    }
}
