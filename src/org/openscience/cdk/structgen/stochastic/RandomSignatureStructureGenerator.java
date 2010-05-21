package org.openscience.cdk.structgen.stochastic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.structgen.deterministic.Graph;
import org.openscience.cdk.structgen.deterministic.TargetMolecularSignature;
import org.openscience.cdk.structgen.deterministic.Util;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

public class RandomSignatureStructureGenerator {
    
    private IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    private IMolecularFormula formula;
    
    private TargetMolecularSignature tms;
    
    private Random random;
    
    public RandomSignatureStructureGenerator(
            String formulaString, TargetMolecularSignature tms) {
        this.formula = 
            MolecularFormulaManipulator.getMolecularFormula(
                formulaString, this.builder);
        this.tms = tms;
        this.random = new Random();
    }
    
    public IAtomContainer generate() {
        IAtomContainer initial = 
            Util.makeAtomContainerFromFormula(formula, builder);
        Graph graph = new Graph(initial);
        graph.assignAtomsToTarget(tms);
        boolean notFullyConnected = true;
        boolean notFullySaturated = true;
        List<Integer> previousUnsaturated = new ArrayList<Integer>();
        int stuckLimit = 10;
        int stuckCount = 0;
        while (notFullyConnected || notFullySaturated) {
            int x = randomIndex(graph);
            int y = randomIndex(graph);

            graph = makeRandomBond(x, y, graph);
            notFullyConnected = !Util.isConnected(graph.getAtomContainer());
            List<Integer> currentUnsaturated = graph.allUnsaturatedAtoms();
            notFullySaturated = currentUnsaturated.size() > 0;
            boolean eq = listsEqual(previousUnsaturated, currentUnsaturated);
            if (eq) {
                stuckCount++;
            } else {
                stuckCount = 0;
                previousUnsaturated = currentUnsaturated;
            }
            if (stuckCount >= stuckLimit) {
                removeRandomBond(graph);
                stuckCount = 0;
            }
        }
        return graph.getAtomContainer();
    }
    
    private void removeRandomBond(Graph graph) {
        boolean bondNotRemoved = true;
        int n = graph.getAtomContainer().getAtomCount();
        while (bondNotRemoved) {
            int x = random.nextInt(n);
            int y = random.nextInt(n);
            bondNotRemoved = !graph.removeBond(x, y); 
        }
    }
    
    private boolean listsEqual(List<Integer> a, List<Integer> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) == b.get(i)) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
    
    private Graph makeRandomBond(int x, int y, Graph graph) {
        if (x != y) {
            Graph copy = new Graph(graph);
            copy.bond(x, y);
            if (copy.check(x, y, tms) && 
                    !Util.saturatedSubgraph(x, graph.getAtomContainer())) {
                return copy;
            }
        }
        
        return graph;
    }
    
    private int randomIndex(Graph graph) {
        List<Integer> unsaturated = graph.allUnsaturatedAtoms();
        System.out.println(unsaturated + " " + graph);
        return unsaturated.get(random.nextInt(unsaturated.size()));
    }

}
