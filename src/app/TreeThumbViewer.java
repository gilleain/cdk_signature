package app;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

public class TreeThumbViewer extends JPanel {
    
    public int numberOfPanels;
    
    public int thumbWidth;
    
    public int thumbHeight;
    
    private JScrollPane scrollPane;
    
    private JList list;
    
    public TreeThumbViewer(int w, int h) {
        numberOfPanels = 0;
        thumbWidth = 300;
        thumbHeight = 200;
        list = new JList(new DefaultListModel());
        list.setCellRenderer(new SignatureTreeListCellRenderer(
                thumbWidth, thumbHeight));
        scrollPane = new JScrollPane(list, 
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane);
        Dimension d = new Dimension(w, h);
        scrollPane.setPreferredSize(d);
        setPreferredSize(d);
    }
    
    public void addSelectionListener(ListSelectionListener listener) {
        list.addListSelectionListener(listener);
    }
    
    public List<String> getSelected() {
        List<String> selected = new ArrayList<String>();
        for (Object o : list.getSelectedValues()) {
            if (o instanceof String) {
                selected.add((String) o);
            } 
        }
        return selected;
    }
    
    public void addSignature(String signature) {
        ((DefaultListModel)list.getModel()).addElement(signature);
        repaint();
    }

    public void clear() {
        ((DefaultListModel)list.getModel()).clear();
        repaint();
    }

}

