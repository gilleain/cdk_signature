package org.openscience.cdk.signature;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

import signature.AbstractGraphBuilder;

/**
 * Builds a molecule from a signature.
 * 
 * @author maclean
 * @cdk.module signature
 * 
 */
public class MoleculeFromSignatureBuilder extends AbstractGraphBuilder {
    
    /**
     * The chem object builder
     */
    private IChemObjectBuilder builder;
    
    /**
     * The container that is being constructed
     */
    private IAtomContainer container;
    
    /**
     * This default constructor uses a {@link NoNotificationChemObjectBuilder}
     */
    public MoleculeFromSignatureBuilder() {
        this.builder = NoNotificationChemObjectBuilder.getInstance();
    }
    
    /**
     * Uses the chem object builder for making molecules.
     * 
     * @param builder a builder for CDK molecules.
     */
    public MoleculeFromSignatureBuilder(IChemObjectBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void makeEdge(int vertexIndex1, int vertexIndex2,
            String vertexSymbol1, String vertexSymbol2) {
        this.container.addBond(vertexIndex1, vertexIndex2, IBond.Order.SINGLE);
    }

    @Override
    public void makeGraph() {
        this.container = this.builder.newInstance(IAtomContainer.class);
    }

    @Override
    public void makeVertex(String label) {
        this.container.addAtom(this.builder.newInstance(IAtom.class, label));
    }
    
    /**
     * Gets the atom container.
     * 
     * @return the constructed atom container
     */
    public IAtomContainer getAtomContainer() {
        return this.container;
    }

}
