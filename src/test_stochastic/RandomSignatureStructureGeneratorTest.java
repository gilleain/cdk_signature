package test_stochastic;

import org.junit.Test;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.structgen.deterministic.FragmentConverter;
import org.openscience.cdk.structgen.deterministic.TargetMolecularSignature;
import org.openscience.cdk.structgen.stochastic.RandomSignatureStructureGenerator;

import test_deterministic.AbstractDeterministicTest;

public class RandomSignatureStructureGeneratorTest {
    
public static SmilesGenerator smilesGenerator = new SmilesGenerator();
    
    public static IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public static String toSmiles(IAtomContainer container) {
        if (ConnectivityChecker.isConnected(container)) {
            return smilesGenerator.createSMILES(
                    builder.newInstance(IMolecule.class, container));
        } else {
            return "disconnected";
        }
    }
    
    public void genStructure(TargetMolecularSignature tms, String formulaString) {
        RandomSignatureStructureGenerator generator = 
            new RandomSignatureStructureGenerator(formulaString, tms);
        IAtomContainer container = generator.generate();
        System.out.println(toSmiles(container));
    }
    
    @Test
    public void twistaneTest() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 4);
        tms.add("[C]([C][C][H][H])", 6);
        tms.add("[H]([C])", 16);
        String formulaString = "C10H16";
        genStructure(tms, formulaString);
    }

    @Test
    public void mixedCarbonC6H14Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[C]([C][C][H][H])", 2);
        tms.add("[C]([C][H][H][H])", 3);
        tms.add("[H]([C])", 14);
        String formulaString = "C6H14";
        genStructure(tms, formulaString);
    }
    
    @Test
    public void mixedCarbonC7H16Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[C]([C][C][H][H])", 3);
        tms.add("[C]([C][H][H][H])", 3);
        tms.add("[H]([C])", 16);
        String formulaString = "C7H16";
        genStructure(tms, formulaString);
    }
    
    @Test
    public void degreeThreeDodecahedraneTest() {
        IAtomContainer degreeThreeFragment = 
            AbstractDeterministicTest.makeDegreeThreeFragment();
        int count = 20;
        TargetMolecularSignature tms = 
            FragmentConverter.convert(degreeThreeFragment, count);
        System.out.println(tms);
        String formulaString = "C20H20";
        genStructure(tms, formulaString);
    }
    
    @Test
    public void degreeThreeHeight2Test() {
        TargetMolecularSignature tms = new TargetMolecularSignature(2);
        tms.add("[C]([C]([C][C][H])[C]([C][C][H])[C]([C][C][H])[H])", 12);
        tms.add("[H]([C]([C][C][C]))", 12);
        String formulaString = "C12H12";
        genStructure(tms, formulaString);
    }
    
    public static void main(String[] args) {
//        new RandomSignatureStructureGeneratorTest().mixedCarbonC6H14Test();
//        new RandomSignatureStructureGeneratorTest().mixedCarbonC7H16Test();
        new RandomSignatureStructureGeneratorTest().twistaneTest();
//        new RandomSignatureStructureGeneratorTest().degreeThreeDodecahedraneTest();
//        new RandomSignatureStructureGeneratorTest().degreeThreeHeight2Test();
    }
}
