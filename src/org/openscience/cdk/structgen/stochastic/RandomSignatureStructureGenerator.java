package org.openscience.cdk.structgen.stochastic;

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
        while (notFullyConnected && notFullySaturated) {
            int x = randomIndex(graph);
            int y = randomIndex(graph);

            graph = makeRandomBond(x, y, graph);
            notFullyConnected = !Util.isConnected(graph.getAtomContainer());
            notFullySaturated = graph.allUnsaturatedAtoms().size() > 0;
        }
        return graph.getAtomContainer();
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
