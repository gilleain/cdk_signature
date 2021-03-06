package test_group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.group.CDKDiscretePartitionRefiner;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.group.SSPermutationGroup;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.signature.MoleculeSignature;
import org.openscience.cdk.signature.Orbit;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;


public class AutomorphismTest {
    
    public int getAutGroupOrder(
            IAtomContainer atomContainer, boolean useSignaturePartition) {
        MoleculeSignature molSig = new MoleculeSignature(atomContainer);
        boolean checkForDisconnected = false;
        boolean useBondOrders = true;
        boolean useElementColors = true;
        boolean useBondColors = true;
        CDKDiscretePartitionRefiner refiner = 
            new CDKDiscretePartitionRefiner(checkForDisconnected, 
                                            useBondOrders, 
                                            useElementColors,
                                            useBondColors);
        SSPermutationGroup perm;
        if (useSignaturePartition) {
            Partition signaturePartition = getSignaturePartition(molSig);
            perm = refiner.getAutomorphismGroup(atomContainer, signaturePartition);
        } else {
            perm = refiner.getAutomorphismGroup(atomContainer);
        }
        for (Permutation p : perm.all()) {
            System.out.println(p + "\t" + p.toCycleString());
        }
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
    public void testSpiroRings() {
        Assert.assertEquals(2, getAutGroupOrder(
                MoleculeFactory.makeSpiroRings(), false));
    }
    
    @Test
    public void testBiphenyl() {
        Assert.assertEquals(2, getAutGroupOrder(
                MoleculeFactory.makeBiphenyl(), false));
    }
    
    @Test
    public void testPyrrole() {
        Assert.assertEquals(2, getAutGroupOrder(
                MoleculeFactory.makePyrrole(), false));
    }
    
    @Test
    public void testBenzene() {
        IAtomContainer benzeneKekule = MoleculeFactory.makeBenzene();
      //gives 6, is 12.
        Assert.assertEquals(6, getAutGroupOrder(benzeneKekule, true));
        IAtomContainer benzeneArom = MoleculeFactory.makeBenzene();
        for (IBond bond : benzeneArom.bonds()) {
            bond.setOrder(IBond.Order.SINGLE);
            bond.setFlag(CDKConstants.ISAROMATIC, true);
        }
        Assert.assertEquals(12, getAutGroupOrder(benzeneArom, true));
    }
    
    @Test
    public void testCyclohexane() {
        IAtomContainer cyclohexane = MoleculeFactory.makeCyclohexane();
      //gives 12, should be 3 (in chair conformation)
        Assert.assertEquals(12, getAutGroupOrder(cyclohexane, true));
    }
    
    @Test
    public void testCyclohexene() throws CDKException {
        IAtomContainer cyclohexene = makeCyclohexene();
        Assert.assertEquals(2, getAutGroupOrder(cyclohexene, true));
    }
    
    @Test
    public void testCyclobutane() throws CDKException {
        IAtomContainer cyclobutane = MoleculeFactory.makeCyclobutane();
//        addHydrogens(cyclobutane);
        //gives 8, is 8
        Assert.assertEquals(8, getAutGroupOrder(cyclobutane, true));
    }
    
    @Test
    public void testDiCyclobutene() {
        IAtomContainer cyclobutadiene = MoleculeFactory.makeCyclobutadiene();
        Assert.assertEquals(4, getAutGroupOrder(cyclobutadiene, true));
    }
    
    @Test
    public void testCyclopentane() throws CDKException {
        IAtomContainer cyclopentane = makeCycloPentane();
        Assert.assertEquals(10, getAutGroupOrder(cyclopentane, false));
    }
    
    @Test
    public void testCyclopentene() throws CDKException {
        IAtomContainer cyclopentene = makeCycloPentene();
        Assert.assertEquals(2, getAutGroupOrder(cyclopentene, true));
    }
    
    @Test
    public void testCyclopropane() throws CDKException {
        IAtomContainer cyclopropane = makeCycloPropane();
        Assert.assertEquals(6, getAutGroupOrder(cyclopropane, true));
    }
    
    @Test
    public void testMethane() throws CDKException {
        IAtomContainer methane = makeMethane();
        //gives 24, should be 12 (chiral carbon)
//        Assert.assertEquals(12, getAutGroupOrder(methane, true));
        Assert.assertEquals(12, getAutGroupOrder(methane, false));
    }
    
	@Test
	public void testIndole() {
		IAtomContainer indole = MoleculeFactory.makeIndole();
		Assert.assertEquals(1, getAutGroupOrder(indole, true));
	}
	
	@Test
	public void testCOCOCycle() {
	    IAtomContainer cocoCycle = makeCOCOCycle();
	    Assert.assertEquals(4, getAutGroupOrder(cocoCycle, false));
	}
	
	public static IAtomContainer makeCOCOCycle() {
	    IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        IAtomContainer mol = builder.newInstance(IAtomContainer.class);
        mol.addAtom(builder.newInstance(IAtom.class,"C"));
        mol.addAtom(builder.newInstance(IAtom.class,"O"));
        mol.addAtom(builder.newInstance(IAtom.class,"C"));
        mol.addAtom(builder.newInstance(IAtom.class,"O"));
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        return mol;
	}
	
	@Test
	public void testMethanol() throws CDKException {
		IAtomContainer methanol = makeMethanol();
		Assert.assertEquals(3, getAutGroupOrder(methanol, true));
	}
	
	@Test
    public void testFluoroChloroBromoIodoMethane() throws CDKException {
        IAtomContainer mol = makeFluoroChloroBromoIodoMethane();
        Assert.assertEquals(1, getAutGroupOrder(mol, false));
    }
	
	public static IAtomContainer makeFluoroChloroBromoIodoMethane() {
	    IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        IAtomContainer mol = builder.newInstance(IAtomContainer.class);
        mol.addAtom(builder.newInstance(IAtom.class,"C"));
        mol.addAtom(builder.newInstance(IAtom.class,"F"));
        mol.addAtom(builder.newInstance(IAtom.class,"Cl"));
        mol.addAtom(builder.newInstance(IAtom.class,"Br"));
        mol.addAtom(builder.newInstance(IAtom.class,"I"));

        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 2, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(0, 4, IBond.Order.SINGLE);
        
        return mol;
	}
	
	public static IAtomContainer makeCycloPentene() throws CDKException {
		IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
		IAtomContainer mol = builder.newInstance(IAtomContainer.class);
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//0
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//1
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//2
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//3
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//4


		mol.addBond(0, 1, IBond.Order.SINGLE);
		mol.addBond(1, 2, IBond.Order.SINGLE);
		mol.addBond(2, 3, IBond.Order.SINGLE);
		mol.addBond(3, 4, IBond.Order.SINGLE);
		mol.addBond(0, 4, IBond.Order.DOUBLE);

		return addHydrogens(mol);
	}
	
	public static IAtomContainer makeCycloPropane() throws CDKException {
		IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
		IAtomContainer mol = builder.newInstance(IAtomContainer.class);
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//0
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//1
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//2

		mol.addBond(0, 1, IBond.Order.SINGLE);
		mol.addBond(1, 2, IBond.Order.SINGLE);
		mol.addBond(2, 0, IBond.Order.SINGLE);
		
		return addHydrogens(mol);
	}
	
	public static IAtomContainer makeMethanol() throws CDKException{
		IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
		IAtomContainer mol = builder.newInstance(IAtomContainer.class);
		mol.addAtom(builder.newInstance(IAtom.class, "C"));//0
		mol.addAtom(builder.newInstance(IAtom.class, "H"));//1
		mol.addAtom(builder.newInstance(IAtom.class, "H"));//2
		mol.addAtom(builder.newInstance(IAtom.class, "H"));//3
		mol.addAtom(builder.newInstance(IAtom.class, "O"));//4
		mol.addAtom(builder.newInstance(IAtom.class, "H"));//5

		mol.addBond(0, 1, IBond.Order.SINGLE);
		mol.addBond(0, 2, IBond.Order.SINGLE);
		mol.addBond(0, 3, IBond.Order.SINGLE);
		mol.addBond(0, 4, IBond.Order.SINGLE);
		mol.addBond(4, 5, IBond.Order.SINGLE);

		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
		return mol;
	}
	
	public static IAtomContainer makeCycloPentane() throws CDKException {
		IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
		IAtomContainer mol = builder.newInstance(IAtomContainer.class);
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//0
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//1
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//2
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//3
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//4

		mol.addBond(0, 1, IBond.Order.SINGLE);
		mol.addBond(1, 2, IBond.Order.SINGLE);
		mol.addBond(2, 3, IBond.Order.SINGLE);
		mol.addBond(3, 4, IBond.Order.SINGLE);
		mol.addBond(0, 4, IBond.Order.SINGLE);
		
		return addHydrogens(mol);
	}
	
	public static IAtomContainer makeCyclohexene() throws CDKException {
		IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
		IAtomContainer mol = builder.newInstance(IAtomContainer.class);
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//0
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//1
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//2
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//3
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//4
		mol.addAtom(builder.newInstance(IAtom.class,"C"));//5

		mol.addBond(0, 1, IBond.Order.DOUBLE);
		mol.addBond(1, 2, IBond.Order.SINGLE);
		mol.addBond(2, 3, IBond.Order.SINGLE);
		mol.addBond(3, 4, IBond.Order.SINGLE);
		mol.addBond(4, 5, IBond.Order.SINGLE);
		mol.addBond(0, 5, IBond.Order.SINGLE);
		
		return addHydrogens(mol);
	}
	
	public static IAtomContainer addHydrogens(IAtomContainer atomContainer) throws CDKException {
	    AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(atomContainer);
        //add implicit hydrogens:
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(atomContainer.getBuilder());
        adder.addImplicitHydrogens(atomContainer);
        //convert implicit to explicit hydrogens:
        AtomContainerManipulator.convertImplicitToExplicitHydrogens(atomContainer);
        
        return atomContainer;
	}

}
