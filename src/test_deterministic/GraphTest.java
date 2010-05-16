package test_deterministic;

import java.util.Arrays;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.structgen.deterministic.FragmentConverter;
import org.openscience.cdk.structgen.deterministic.Graph;
import org.openscience.cdk.structgen.deterministic.TargetMolecularSignature;
import org.openscience.cdk.structgen.deterministic.Util;

public class GraphTest extends AbstractDeterministicTest {

    @Test
    public void assigningSignaturesToAtomsTest() {
        String formulaString = "C4H8";
        IAtomContainer initialContainer = 
            Util.makeAtomContainerFromFormulaString(formulaString, builder); 
        Graph graph = new Graph(initialContainer);
        IAtomContainer fragment = AbstractDeterministicTest.makeCH2(); 
        TargetMolecularSignature signature = 
            FragmentConverter.convert(fragment, 4);
        
        graph.assignAtomsToTarget(signature);
        String table = Arrays.deepToString(signature.getLookupTable());
        System.out.println(
                signature + "\t" + table + "\t" + graph.getAtomTargetMap());
    }
}
