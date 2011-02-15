package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.signature.MoleculeSignature;

public class DuplicateEliminatingHandler implements IEnumeratorResultHandler {
    
    private Map<String, IAtomContainer> results;
    
    private boolean hasDuplicates = false;
    
    public DuplicateEliminatingHandler() {
        results = new HashMap<String, IAtomContainer>();
    }

    public void handle(IAtomContainer result) {
        String signatureString = 
            new MoleculeSignature(result).toCanonicalString();
        if (results.containsKey(signatureString)) {
            hasDuplicates = true;
        }
        results.put(signatureString, result);
    }

    public List<IAtomContainer> getResults() {
        if (hasDuplicates) { System.out.println("DUPS"); }
        return new ArrayList<IAtomContainer>(results.values());
    }

}
