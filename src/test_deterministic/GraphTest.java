package test_deterministic;

import org.junit.Test;
import org.openscience.cdk.deterministic.FragmentConverter;
import org.openscience.cdk.deterministic.Graph;
import org.openscience.cdk.deterministic.Util;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.signature.TargetMolecularSignature;

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
        System.out.println(signature + "\t" + graph.getAtomTargetMap());
    }
}
