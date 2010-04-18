package org.openscience.cdk.deterministic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.AtomSignature;
import org.openscience.cdk.signature.MoleculeSignature;
import org.openscience.cdk.signature.Orbit;
import org.openscience.cdk.signature.TargetMolecularSignature;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

/**
 * A structure enumerator that starts from just the elemental formula, and 
 * creates all possible structures.
 *  
 * @author maclean
 *
 */
public class DeterministicEnumerator {
    
    /**
     * Convenience instance of a builder
     */
    private IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    /**
     * The formula that the enumerator was created with
     */
    private IMolecularFormula formula;
    
    /**
     * The initial container, possibly created from the formula
     */
    private IAtomContainer initialContainer;
    
    /**
     * A class that handles the results that are created
     */
    private IEnumeratorResultHandler handler;
    
    /**
     * Listener for debugging/visualization purposes.
     */
    private BondCreationListener bondCreationListener;
    
    /**
     * The target molecular signature that constrains the generation process
     */
    private TargetMolecularSignature hTau;
    
    /**
     * Start from just the formula string.
     * 
     * @param formulaString a formula string like "C4H8"
     */
    public DeterministicEnumerator(String formulaString) {
        this.formula = 
            MolecularFormulaManipulator.getMolecularFormula(
                    formulaString, this.builder);
        
        this.handler = new DefaultEnumeratorResultHandler();
    }
    
    public DeterministicEnumerator(String formulaString, 
            TargetMolecularSignature targetMolecularSignature) {
        this(formulaString);
        hTau = targetMolecularSignature;
    }
    
    public DeterministicEnumerator(IAtomContainer initialContainer) {
        this.initialContainer = initialContainer;
    }
    
    public void setBondCreationListener(BondCreationListener listener) {
        this.bondCreationListener = listener;
    }
    
    /**
     * Set the result handler. 
     * 
     * @param handler
     */
    public void setHandler(IEnumeratorResultHandler handler) {
        this.handler = handler;
    }
    
    private IAtomContainer makeAtomContainerFromFormula() {
        return Util.makeAtomContainerFromFormula(formula, builder);
    }
    
    /**
     * Create the structures, passing each one to the result handler.
     */
    public void generateToHandler() {
        Graph initialGraph;
        if (this.formula != null) {
            initialGraph = new Graph(this.makeAtomContainerFromFormula());
        } else if (this.initialContainer != null) {
            initialGraph = new Graph(this.initialContainer);
        } else {
            return;
        }
        
        if (hTau != null) {
            System.out.println("assigning " + hTau);
            initialGraph.assignAtomsToTarget(hTau);
        }
        this.enumerate(initialGraph);
    }
    
    /**
     * Generate the structures, and return them in a list.
     * 
     * @return a list of atom containers
     */
    public List<IAtomContainer> generate() {
//        final List<IAtomContainer> results = new ArrayList<IAtomContainer>();
        final HashMap<String, IAtomContainer> results = 
            new HashMap<String, IAtomContainer>();
        this.handler = new IEnumeratorResultHandler() {
            public void handle(IAtomContainer result) {
                String signatureString = 
                    new MoleculeSignature(result).toCanonicalString();
//                if (results.containsKey(signatureString)) {
//                    return;
//                } else {
                    results.put(signatureString, result);
//                }
            }
        };
        this.generateToHandler();
        return new ArrayList<IAtomContainer>(results.values());
    }
    
    private void enumerate(Graph g) {
        if (g.isConnected() 
                && g.isFullySaturated() 
//                    && g.isCanonical()
                        && (hTau == null || hTau.matches(g.getAtomContainer()))) {
            System.out.println("solution " + g);
            this.handler.handle(g.getAtomContainer());
        } else {
            Orbit o = g.getUnsaturatedOrbit();
            if (o == null) return;
            
            for (Graph h : saturateOrbit(o, g)) {
                enumerate(h);
            }
        }
    }
    
    private List<Graph> saturateOrbit(Orbit o, Graph g) {
        ArrayList<Graph> orbitSolutions = new ArrayList<Graph>();
        saturateOrbit(o, g, orbitSolutions);
        return orbitSolutions;
    }
    
    private void saturateOrbit(Orbit o, Graph g, ArrayList<Graph> s) {
        System.out.println("Saturating orbit : " + o);
        if (o == null || o.isEmpty()) {
//            System.out.println("orbit empty");
            s.add(g);
        } else {
            int x = o.getFirstAtom();
            o.remove(x);
//            System.out.println("first atom " + x);
            
            for (Graph h : saturateAtom(x, g)) {
                if (h.height1SignatureMatches(x, hTau)) {
                    saturateOrbit(o, h, s);
                }
            }
        }
    }
    
    private List<Graph> saturateAtom(int x, Graph g) {
        Map<String, Graph> atomSolutions = new HashMap<String, Graph>();
        saturateAtom(x, g, atomSolutions);
        return new ArrayList<Graph>(atomSolutions.values());
    }
    
    private void saturateAtom(int x, Graph g, Map<String, Graph> s) {
//        System.out.println("saturating atom " + x + " in " + g);
        if (g.isSaturated(x)) {
//            System.out.println(x + " is already saturated");
            String sig = 
                new AtomSignature(x, g.getAtomContainer()).toCanonicalString();
            if (!s.containsKey(sig)) {
                s.put(sig, g);
            }
            return;
        } else {
            List<Integer> unsaturatedAtoms = g.unsaturatedAtoms(x);
//            System.out.println("trying all of " + unsaturatedAtoms);
            for (int y : unsaturatedAtoms) {
                if (x == y) continue;
                Graph copy = new Graph(g);
                copy.bond(x, y);
                
                if (copy.check(x, y, hTau)) {
//                    System.out.println("passed all tests");
                    if (this.bondCreationListener != null) {
//                        this.bondCreationListener.bondAdded(new BondCreationEvent(g, copy));
                    }
                    saturateAtom(x, copy, s);
                }
            }
        }
    }
}
