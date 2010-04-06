package test_signature;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.signature.SignatureQuotientGraph;


public class SignatureQuotientGraphTest extends AbstractSignatureTest {
    
    public void checkParameters(SignatureQuotientGraph qGraph,
                                int expectedVertexCount, 
                                int expectedEdgeCount, 
                                int expectedLoopEdgeCount) {
        System.out.println(qGraph);
        Assert.assertEquals(expectedVertexCount, qGraph.getVertexCount());
        Assert.assertEquals(expectedEdgeCount, qGraph.getEdgeCount());
        Assert.assertEquals(expectedLoopEdgeCount, qGraph.numberOfLoopEdges());
    }
    
    @Test
    public void testCubane() {
        IMolecule cubane = AbstractSignatureTest.makeCubane();
        SignatureQuotientGraph qGraph = new SignatureQuotientGraph(cubane);
        checkParameters(qGraph, 1, 1, 1);
    }
    
    @Test
    public void testCuneaneAtHeight1() {
        IMolecule cuneane = AbstractSignatureTest.makeCuneane();
        SignatureQuotientGraph qGraph = new SignatureQuotientGraph(cuneane, 1);
        checkParameters(qGraph, 1, 1, 1);
    }
    
    @Test
    public void testCuneaneAtHeight2() {
        IMolecule cuneane = AbstractSignatureTest.makeCuneane();
        SignatureQuotientGraph qGraph = new SignatureQuotientGraph(cuneane, 2);
        checkParameters(qGraph, 3, 5, 3);
    }
    
    @Test
    public void testPropellane() {
        IMolecule propellane = AbstractSignatureTest.makePropellane();
        SignatureQuotientGraph qGraph = new SignatureQuotientGraph(propellane);
        checkParameters(qGraph, 2, 2, 1);
    }

}
