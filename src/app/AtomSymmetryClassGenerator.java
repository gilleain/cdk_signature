package app;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point2d;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.elements.OvalElement;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.signature.Orbit;

public class AtomSymmetryClassGenerator implements IAtomContainerGenerator {
    
    private Map<Orbit, Color> orbitColorMap;
    
    public AtomSymmetryClassGenerator() {
        orbitColorMap = new HashMap<Orbit, Color>();
    }
    
    public void setOrbits(List<Orbit> orbits) {
        orbitColorMap.clear();
        int i = 0;
        for (Orbit o : orbits) {
            orbitColorMap.put(o, getColorForIndex(i));
            i++;
        }
        
    }
    
    public Color getColorForIndex(int i) {
        switch (i) {
            case 0: return Color.RED;
            case 1: return Color.ORANGE;
            case 2: return Color.YELLOW;
            case 3: return Color.GREEN;
            case 4: return Color.BLUE;
            case 5: return Color.MAGENTA;
            default: return Color.BLACK;
        }
    }

    public IRenderingElement generate(IAtomContainer atomContainer,
            RendererModel model) {
        ElementGroup elements = new ElementGroup();
        for (Orbit o : orbitColorMap.keySet()) {
            Color color = orbitColorMap.get(o);
            System.out.println("setting color " + color);
            for (int i : o) {
                IAtom a = atomContainer.getAtom(i);
                Point2d p = a.getPoint2d();
                elements.add(new OvalElement(p.x, p.y, 0.25, color));
            }
        }
        return elements;
    }

    public List<IGeneratorParameter<?>> getParameters() {
        return new ArrayList<IGeneratorParameter<?>>();
    }

}
