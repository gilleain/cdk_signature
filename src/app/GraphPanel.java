package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.openscience.cdk.deterministic.Graph;

public class GraphPanel extends JPanel {
    
    public boolean isThumbnail;
    
    private Graph graph;
    
    private int selected;
    
    public GraphPanel(int w, int h, boolean isThumbnail) {
        graph = null;
        this.isThumbnail = isThumbnail;
        setPreferredSize(new Dimension(w, h));
        setBackground(Color.WHITE);
        selected = -1;
    }
    
    public void setGraph(Graph graph) {
        this.graph = graph;
        repaint();
    }
    
    public void setSelected(int selected) {
        this.selected = selected;
    }
    
    public int getTarget() {
        if (selected == -1) {
            return -1;
        } else {
            return graph.getAtomTargetMap().get(selected);
        }
    }
    
    public void select(int x, int y) {
        int w = getWidth();
        int h = getHeight();
        int center = w / 2;
        int axis = (2 * h) / 3;
       
        selected = GraphRenderer.getSelected(
                x, y, graph, center, w, axis, isThumbnail);
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        int w = getWidth();
        int h = getHeight();
        int center = w / 2;
        int axis = (2 * h) / 3;
        if (graph != null) {
            GraphRenderer.paintDiagram(
                    graph, g, center, w, axis, isThumbnail, selected);
        } else {
            // XXX : left edge?
//            GraphRenderer.paintBackground(g, 0, axis, w, h);
        }
    }

}
