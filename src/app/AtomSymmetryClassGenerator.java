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
//            orbitColorMap.put(o, getColorForIndex(i));
            orbitColorMap.put(o, colourRamp(i, 0, orbits.size()));
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
    
    private Color colourRamp(int v, int vmin, int vmax) {
        double r = 1.0;
        double g = 1.0;
        double b = 1.0;
        if (v < vmin) { v = vmin; }
        if (v > vmax) { v = vmax; }
        int dv = vmax - vmin;

        try  {
            if (v < (vmin + 0.25 * dv)) {
                r = 0.0;
                g = 4.0 * (v - vmin) / dv;
            } else if (v < (vmin + 0.5 * dv)) {
                r = 0.0;
                b = 1.0 + 4.0 * (vmin + 0.25 * dv - v) / dv;
            } else if (v < (vmin + 0.75 * dv)) {
                r = 4.0 * (v - vmin - 0.5  * dv) / dv;
                b = 0.0;
            } else {
                g = 1.0 + 4.0 * (vmin + 0.75 * dv - v) / dv;
                b = 0.0;
            }
            float[] hsb = Color.RGBtoHSB(
                    (int)(r * 255), (int)(g * 255), (int)(b * 255), null);
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        } catch (ArithmeticException zde) {
            float[] hsb = Color.RGBtoHSB(0, 0, 0, null);
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }

    }

    public IRenderingElement generate(IAtomContainer atomContainer,
            RendererModel model) {
        ElementGroup elements = new ElementGroup();
        double atomHighlightRadius = 0.15;
        for (Orbit o : orbitColorMap.keySet()) {
            Color color = orbitColorMap.get(o);
            System.out.println("setting color " + color);
            for (int i : o) {
                IAtom a = atomContainer.getAtom(i);
                Point2d p = a.getPoint2d();
                elements.add(new OvalElement(p.x, p.y, atomHighlightRadius, color));
            }
        }
        return elements;
    }

    public List<IGeneratorParameter<?>> getParameters() {
        return new ArrayList<IGeneratorParameter<?>>();
    }

}
