package app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point2d;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.elements.TextElement;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;

public class NumberingGenerator implements IAtomContainerGenerator {
    
    public boolean on = true;

    public IRenderingElement generate(
            IAtomContainer atomContainer, RendererModel model) {
        ElementGroup elements = new ElementGroup();
        if (on) {
            for (int i = 0; i < atomContainer.getAtomCount(); i++) {
                IAtom a = atomContainer.getAtom(i);
                Point2d p = a.getPoint2d();
                elements.add(
                        new TextElement(p.x, p.y, String.valueOf(i), Color.BLACK));
            }
        }
        return elements;
    }

    public List<IGeneratorParameter<?>> getParameters() {
        return new ArrayList<IGeneratorParameter<?>>();
    }

}
