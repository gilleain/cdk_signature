package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.signature.AtomSignature;
import org.openscience.cdk.signature.MoleculeSignature;

public class FragmentConverter {
    
    public static TargetMolecularSignature convert(
            IAtomContainer fragment, int count) {
        List<IAtomContainer> fragments = new ArrayList<IAtomContainer>();
        fragments.add(fragment);
        List<Integer> counts = new ArrayList<Integer>();
        counts.add(count);
        return FragmentConverter.convert(fragments, counts);
    }
    
    public static TargetMolecularSignature convert(
            List<IAtomContainer> fragments, List<Integer> counts) {
        TargetMolecularSignature targetMolecularSignature = 
            new TargetMolecularSignature(1);    // FIXME - height

        // temporary store for signature strings - TODO convert TMS to allow
        // for adding new signatures and counting duplicates
        Map<String, Integer> tmp = new HashMap<String, Integer>();
        
        int fragmentIndex = 0;
        for (IAtomContainer fragment : fragments) {
            MoleculeSignature fragmentSignature = 
                new MoleculeSignature(fragment);
            int fragmentCount = counts.get(fragmentIndex);
            for (int i = 0; i < fragment.getAtomCount(); i++) {
                if (FragmentConverter.isSaturated(i, fragment)) {
                    AtomSignature signature = 
                        (AtomSignature) 
                            fragmentSignature.signatureForVertex(i);
                    String signatureString = signature.toString();
                    if (tmp.containsKey(signatureString)) {
                        tmp.put(signatureString, 
                                tmp.get(signatureString) + fragmentCount);
                    } else {
                        tmp.put(signatureString, fragmentCount);
                    }
                }
            }
            fragmentIndex++;
        }
        for (String signatureString : tmp.keySet()) {
            int count = tmp.get(signatureString);
            targetMolecularSignature.add(signatureString, count);
        }
        
        return targetMolecularSignature;
    }
    
    public static boolean isSaturated(int i, IAtomContainer container) {
        return Util.isSaturated(container.getAtom(i), container);
    }

}
