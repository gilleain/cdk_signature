package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openscience.cdk.structgen.deterministic.AtomSaturationEvent;
import org.openscience.cdk.structgen.deterministic.AtomSaturationListener;
import org.openscience.cdk.structgen.deterministic.BondCreationEvent;
import org.openscience.cdk.structgen.deterministic.BondCreationListener;
import org.openscience.cdk.structgen.deterministic.BondRejectionEvent;
import org.openscience.cdk.structgen.deterministic.BondRejectionListener;
import org.openscience.cdk.structgen.deterministic.DeterministicEnumerator;
import org.openscience.cdk.structgen.deterministic.Graph;
import org.openscience.cdk.structgen.deterministic.OrbitSaturationEvent;
import org.openscience.cdk.structgen.deterministic.OrbitSaturationListener;

import app.FormulaControlPanel.ListenerType;

import signature.AbstractVertexSignature;
import signature.display.ColoredTreePanel;

public class FormulaDebugger extends JFrame 
    implements ActionListener, AtomSaturationListener, 
               BondCreationListener, ListSelectionListener, MouseListener,
               OrbitSaturationListener, BondRejectionListener {
    
    private SearchTreePanel searchTreePanel;
    
    private JScrollPane searchTreeScrollPane;
    
    private DeterministicEnumerator enumerator;
    
    private GraphThumbViewer thumbViewer;
    
    private GraphPanel mainGraphPanel;
    
    private ColoredTreePanel actualTreePanel;
    
    private MoleculePanel molPanel;
    
    private FormulaControlPanel controlPanel;
    
    public static final int SEARCH_TREE_PANEL_WIDTH = 300;
    
    public static final int SEARCH_TREE_PANEL_HEIGHT = 750;
    
    public static final int THUMB_PANEL_WIDTH = 400;
    
    public static final int THUMB_PANEL_HEIGHT = 750;
    
    public static final int GRAPH_PANEL_WIDTH = 500;
    
    public static final int GRAPH_PANEL_HEIGHT = 250;
    
    public static final int TREE_PANEL_WIDTH = 500;
    
    public static final int TREE_PANEL_HEIGHT = 250;
    
    public static final int MOL_PANEL_WIDTH = 500;
    
    public static final int MOL_PANEL_HEIGHT = 250;
    
    public FormulaDebugger() {
        setLayout(new BorderLayout());
        
        JPanel westPanel = new JPanel(new GridLayout(1, 2));
        searchTreePanel = new SearchTreePanel(
                SEARCH_TREE_PANEL_WIDTH, SEARCH_TREE_PANEL_HEIGHT);
        searchTreeScrollPane = new JScrollPane(searchTreePanel);
        searchTreeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        searchTreeScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        searchTreeScrollPane.setPreferredSize(
              new Dimension(SEARCH_TREE_PANEL_WIDTH, SEARCH_TREE_PANEL_HEIGHT));
        searchTreePanel.addMouseListener(this);
        westPanel.add(searchTreeScrollPane);
        
        thumbViewer = new GraphThumbViewer(
                THUMB_PANEL_WIDTH, THUMB_PANEL_HEIGHT);
        thumbViewer.setBorder(BorderFactory.createEtchedBorder());
        thumbViewer.addSelectionListener(this);
        westPanel.add(thumbViewer);
        add(westPanel, BorderLayout.WEST);
        
        controlPanel = new FormulaControlPanel();
        controlPanel.addButtonListener(this);
        add(controlPanel, BorderLayout.NORTH);
        
        JPanel rightPanel = new JPanel(new GridLayout(3, 1));
        
        mainGraphPanel = new GraphPanel(
                GRAPH_PANEL_WIDTH, GRAPH_PANEL_HEIGHT, false);
        mainGraphPanel.setBorder(BorderFactory.createEtchedBorder());
        mainGraphPanel.addMouseListener(this);
        rightPanel.add(mainGraphPanel);
        
        molPanel = new MoleculePanel(
                MOL_PANEL_WIDTH, MOL_PANEL_HEIGHT);
        rightPanel.add(molPanel);
        
        actualTreePanel = new ColoredTreePanel(
                TREE_PANEL_WIDTH, TREE_PANEL_HEIGHT);
        rightPanel.add(actualTreePanel);
        add(rightPanel, BorderLayout.EAST);
//        setPreferredSize(new Dimension(1200, 600));
        
        pack();
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("RUN")) {
            String formula = controlPanel.getCurrentFormula();
            System.out.println("formula = " + formula);
            run(formula);
        } else if (e.getActionCommand().equals("CLEAR")) {
            thumbViewer.clear();
            mainGraphPanel.clear();
            actualTreePanel.clear();
            molPanel.clear();
            searchTreePanel.clear();
            repaint();
        }
    }

    private void run(String formula) {
        
        enumerator = new DeterministicEnumerator(formula);
        ListenerType listenerType = controlPanel.getSelectedListenerType();
        if (listenerType == ListenerType.ATOM_SATURATION) {
            enumerator.setAtomSaturationListener(this);
        } else if (listenerType == ListenerType.BOND_CREATION) {
            enumerator.setBondCreationListener(this);
        } else if (listenerType == ListenerType.BOND_REJECTION) {
            enumerator.setBondRejectionListener(this);
        } else if (listenerType == ListenerType.ORBIT_SATURATION) {
            enumerator.setOrbitSaturationListener(this);
        }
        enumerator.generate();
        searchTreePanel.repaint();
    }

    public void atomSaturated(AtomSaturationEvent atomSaturationEvent) {
        thumbViewer.addGraph(atomSaturationEvent.graph);
        thumbViewer.repaint();
    }

    public void bondAdded(BondCreationEvent bondCreationEvent) {
        searchTreePanel.addRelation(
                bondCreationEvent.parent, bondCreationEvent.child);
        thumbViewer.addGraph(bondCreationEvent.child);
        thumbViewer.repaint();
    }
    
    public void bondRejected(BondRejectionEvent bondRejectionEvent) {
        thumbViewer.addGraph(bondRejectionEvent.graph);
        thumbViewer.repaint();
    }

    public void orbitSaturation(OrbitSaturationEvent orbitSaturationEvent) {
        thumbViewer.addGraph(orbitSaturationEvent.graph);
        thumbViewer.repaint();
    }

    public void valueChanged(ListSelectionEvent e) {
        Graph selected = thumbViewer.getSelected();
        if (selected != null) {
            mainGraphPanel.setGraph(selected);
            molPanel.setMoleculeFromGraph(selected);
            System.out.println(selected.getOrbits());
        }
    }
    

    public void mouseClicked(MouseEvent e) {
//        int x = e.getX();
//        int y = e.getY();
//        int selected = mainGraphPanel.select(x, y);
//        int target = mainGraphPanel.getTarget();
//        if (target != -1) {
//            displayTargetSignature(target);
//            molPanel.selectAtom(selected);
//            displayActualSignature(selected);
//        }
//        repaint();
    }
    
    public void displayActualSignature(int atomIndex) {
        String sig = mainGraphPanel.getSignature(atomIndex);
        actualTreePanel.setTree(AbstractVertexSignature.parse(sig));
    }

    public void mouseEntered(MouseEvent e) {   }

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (e.getSource() == searchTreePanel) {
            searchTreePanel.select(x, y);
            List<Graph> selected = searchTreePanel.getSelectedBranch();
            thumbViewer.setGraphs(selected);
        } else {
            int selected = mainGraphPanel.select(x, y);
            if (selected != -1) {
                molPanel.selectAtom(selected);
                displayActualSignature(selected);
            }
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {}


    public static void main(String[] args) {
        new FormulaDebugger();
    }

}
