package test_group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.group.CDKDiscretePartitionRefiner;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.SSPermutationGroup;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.signature.MoleculeSignature;
import org.openscience.cdk.signature.Orbit;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;



public class AutomorphismTest {
    
    public int getAutGroupOrder(IAtomContainer atomContainer) {
        MoleculeSignature molSig = new MoleculeSignature(atomContainer);
        Partition signaturePartition = getSignaturePartition(molSig);
        CDKDiscretePartitionRefiner refiner = new CDKDiscretePartitionRefiner();
        SSPermutationGroup perm = 
            refiner.getAutomorphismGroup(atomContainer, signaturePartition);
        return perm.order();
    }
    
    public Partition getSignaturePartition(MoleculeSignature signature) {
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
            partition.addCell(o.getAtomIndices());
        }
        return partition;
    }
    
    public static IAtomContainer makeMethane() throws CDKException{
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        IMolecule mol = builder.newInstance(IMolecule.class);
        mol.addAtom(builder.newInstance(IAtom.class, "C"));//0
        mol.addAtom(builder.newInstance(IAtom.class, "H"));//1
        mol.addAtom(builder.newInstance(IAtom.class, "H"));//2
        mol.addAtom(builder.newInstance(IAtom.class, "H"));//3
        mol.addAtom(builder.newInstance(IAtom.class, "H"));//4

        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 2, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(0, 4, IBond.Order.SINGLE);

        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);

        return mol;
    }
    
    @Test
    public void testBenzene() {
        IAtomContainer benzene = MoleculeFactory.makeBenzene();
      //gives 6, is 12.
        Assert.assertEquals(6, getAutGroupOrder(benzene));
    }
    
    @Test
    public void testCyclohexane() {
        IAtomContainer cyclohexane = MoleculeFactory.makeCyclohexane();
      //gives 12, should be 3 (in chair conformation)
        Assert.assertEquals(12, getAutGroupOrder(cyclohexane));
    }
    
    @Test
    public void testCyclobutane() {
        IAtomContainer cyclobutane = MoleculeFactory.makeCyclobutane();
        //gives 8, is 8
        Assert.assertEquals(8, getAutGroupOrder(cyclobutane));
    }
    
    @Test
    public void testMethane() throws CDKException {
        IAtomContainer methane = makeMethane();
        //gives 24, should be 12 (chiral carbon)
        Assert.assertEquals(12, getAutGroupOrder(methane));
    }

}
