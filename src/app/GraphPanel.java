package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.openscience.cdk.deterministic.Graph;

public class GraphPanel extends JPanel {
    
    public boolean isThumbnail;
    
    private Graph graph;
    
    private int selectedAtom;
    
    private int selectedBond;
    
    public GraphPanel(int w, int h, boolean isThumbnail) {
        graph = null;
        this.isThumbnail = isThumbnail;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.WHITE);
        selectedAtom = -1;
        selectedBond = -1;
    }
    
    public String getSignature(int atomIndex) {
        if (graph != null) {
            return graph.getSignatureOfAtom(atomIndex);
        } else {
            return "";
        }
    }
    
    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }
    
    public void setSelectedAtom(int selectedAtom) {
        this.selectedAtom = selectedAtom;
    }
    
    public int getTarget() {
        if (selectedAtom == -1) {
            return -1;
        } else {
            return graph.getAtomTargetMap().get(selectedAtom);
        }
    }
    
    public int select(int x, int y) {
        int w = getWidth();
        int h = getHeight();
        int center = w / 2;
        int axis = (2 * h) / 3;
       
        selectedAtom = GraphRenderer.getSelected(
                x, y, graph, center, w, axis, isThumbnail);
        return selectedAtom;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        int w = getWidth();
        int h = getHeight();
        int center = w / 2;
        int axis = (2 * h) / 3;
        if (graph != null) {
            GraphRenderer.paintDiagram(
                graph, g, center, w, axis, isThumbnail, selectedAtom, selectedBond);
        } else {
            // XXX : left edge?
//            GraphRenderer.paintBackground(g, 0, axis, w, h);
        }
    }

    public void clear() {
        this.graph = null;
        this.selectedAtom = -1;
    }

}
