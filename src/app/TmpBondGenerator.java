package app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.elements.LineElement;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;

public class TmpBondGenerator implements IAtomContainerGenerator {

    public IRenderingElement generate(
            IAtomContainer atomContainer, RendererModel model) {
        ElementGroup bondGroup = new ElementGroup();
        for (IBond bond : atomContainer.bonds()) {
            IAtom a = bond.getAtom(0);
            IAtom b = bond.getAtom(1);
            Point2d pA = a.getPoint2d();
            Point2d pB = b.getPoint2d();
            bondGroup.add(
                    new LineElement(pA.x, pA.y, pB.x, pB.y, 1, Color.BLACK));
        }
        return bondGroup;
    }

    public List<IGeneratorParameter<?>> getParameters() {
        return new ArrayList<IGeneratorParameter<?>>();
    }

}
