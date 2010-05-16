package test_app;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.structgen.deterministic.Graph;

import app.GraphThumbViewer;

public class TestGraphThumbViewer extends JPanel {
    
    public TestGraphThumbViewer() {
        int w = 300;
        int h = 600;
        GraphThumbViewer viewer = new GraphThumbViewer(w, h);
        viewer.addGraph(new Graph(makeStructureA()));
        viewer.addGraph(new Graph(makeStructureB()));
        add(viewer);
        setPreferredSize(new Dimension(w, h));
    }
    
    public IAtomContainer makeStructureA() {
        IAtomContainer atomContainer = new AtomContainer();
        atomContainer.addAtom(new Atom("C"));
        atomContainer.addAtom(new Atom("C"));
        atomContainer.addAtom(new Atom("C"));
        atomContainer.addAtom(new Atom("H"));
        atomContainer.addAtom(new Atom("H"));
        atomContainer.addBond(0, 1, IBond.Order.SINGLE);
        atomContainer.addBond(0, 2, IBond.Order.SINGLE);
        atomContainer.addBond(0, 3, IBond.Order.SINGLE);
        return atomContainer;
    }
    
    public IAtomContainer makeStructureB() {
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
        return atomContainer;
    }
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.add(new TestGraphThumbViewer());
        f.pack();
        f.setVisible(true);
    }

}
