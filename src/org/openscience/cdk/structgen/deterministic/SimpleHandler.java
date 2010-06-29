package org.openscience.cdk.structgen.deterministic;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtomContainer;

public class SimpleHandler implements IEnumeratorResultHandler {
    
    private List<IAtomContainer> results;
    
    public SimpleHandler() {
        this.results = new ArrayList<IAtomContainer>();
    }

    public void handle(IAtomContainer result) {
        results.add(result);
    }
    
    public List<IAtomContainer> getResults() {
        return results;
    }

}
