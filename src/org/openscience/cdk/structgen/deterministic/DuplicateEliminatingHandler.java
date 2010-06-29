package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.signature.MoleculeSignature;

public class DuplicateEliminatingHandler implements IEnumeratorResultHandler {
    
    private Map<String, IAtomContainer> results;
    
    public DuplicateEliminatingHandler() {
        results = new HashMap<String, IAtomContainer>();
    }

    public void handle(IAtomContainer result) {
        String signatureString = 
            new MoleculeSignature(result).toCanonicalString();
        results.put(signatureString, result);
    }

    public List<IAtomContainer> getResults() {
        return new ArrayList<IAtomContainer>(results.values());
    }

}
