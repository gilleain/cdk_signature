package test_app;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.deterministic.Graph;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import app.GraphRenderer;

public class TestGraphRenderer extends JPanel {
    
    public Graph graph;
    
    public TestGraphRenderer() {
        IAtomContainer atomContainer = new AtomContainer();
        atomContainer.addAtom(new Atom("C"));
        atomContainer.addAtom(new Atom("C"));
        atomContainer.addAtom(new Atom("C"));
        atomContainer.addAtom(new Atom("H"));
        atomContainer.addAtom(new Atom("H"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 3, IBond.Order.SINGLE);
        atomContainer.addBond(0, 4, IBond.Order.SINGLE);
        graph = new Graph(atomContainer);
        this.setPreferredSize(new Dimension(500, 300));
    }
    
    public void paint(Graphics g) {
        if (graph != null) {
            int w = getWidth();
            int h = getHeight();
            int center = w / 2;
            int axis = (2 * h) / 3;
            GraphRenderer.paintDiagram(graph, g, center, w, axis, false, 2, 3);
        }
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.add(new TestGraphRenderer());
        f.pack();
        f.setVisible(true);
    }

}
