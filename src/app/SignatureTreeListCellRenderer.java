package app;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import signature.AbstractVertexSignature;
import signature.ColoredTree;
import signature.display.DisplayableColoredTree;

public class SignatureTreeListCellRenderer extends JPanel implements
        ListCellRenderer {

    private String signature;

    public SignatureTreeListCellRenderer(int w, int h) {
        setPreferredSize(new Dimension(w, h));
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof String) {
            signature = (String) value;
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
        if (signature != null) {
            int w = getWidth() - 2;
            int h = getHeight() - 2;
            ColoredTree tree = AbstractVertexSignature.parse(signature);
            DisplayableColoredTree displayTree = new DisplayableColoredTree(w, h);
            displayTree.makeFromColoredTree(tree);
            displayTree.paint(g);
        }
    }

}
