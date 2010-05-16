package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import org.openscience.cdk.structgen.deterministic.Graph;

public class GraphListCellRenderer extends JPanel implements ListCellRenderer {
    
    private Graph graph;
    
    public GraphListCellRenderer(int w, int h) {
        setPreferredSize(new Dimension(w, h));
    }

    public Component getListCellRendererComponent(
            JList list, Object value, int index, 
            boolean isSelected, boolean cellHasFocus) {
        if (value instanceof Graph) {
            graph = (Graph) value;
//            System.out.println("setting graph " + graph);
        }
        
        if (isSelected) {
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }
        
        return this;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        if (graph != null) {
            int w = getWidth();
            int h = getHeight();
            int center = w / 2;
            int axis = (2 * h) / 3;
            int selectedBond = graph.getAtomContainer().getBondCount() - 1;
            GraphRenderer.paintDiagram(
                    graph, g, center, w, axis, true, -1, selectedBond);
        }
    }

}
