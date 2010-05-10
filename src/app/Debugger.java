package app;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openscience.cdk.deterministic.AtomSaturationEvent;
import org.openscience.cdk.deterministic.AtomSaturationListener;
import org.openscience.cdk.deterministic.BondCreationEvent;
import org.openscience.cdk.deterministic.BondCreationListener;
import org.openscience.cdk.deterministic.BondRejectionEvent;
import org.openscience.cdk.deterministic.BondRejectionListener;
import org.openscience.cdk.deterministic.DeterministicEnumerator;
import org.openscience.cdk.deterministic.Graph;
import org.openscience.cdk.deterministic.OrbitSaturationEvent;
import org.openscience.cdk.deterministic.OrbitSaturationListener;
import org.openscience.cdk.signature.TargetMolecularSignature;

import app.ControlPanel.ListenerType;

import signature.AbstractVertexSignature;
import signature.display.ColoredTreePanel;

public class Debugger extends JFrame 
    implements ActionListener, AtomSaturationListener, 
               BondCreationListener, ListSelectionListener, MouseListener,
               OrbitSaturationListener, BondRejectionListener {
    
    private DeterministicEnumerator enumerator;
    
    private GraphThumbViewer thumbViewer;
    
    private GraphPanel mainGraphPanel;
    
    private ColoredTreePanel targetTreePanel;
    
    private ColoredTreePanel actualTreePanel;
    
    private MoleculePanel molPanel;
    
    private ControlPanel controlPanel;
    
    public static final int THUMB_PANEL_WIDTH = 300;
    
    public static final int THUMB_PANEL_HEIGHT = 600;
    
    public static final int GRAPH_PANEL_WIDTH = 500;
    
    public static final int GRAPH_PANEL_HEIGHT = 300;
    
    public static final int TREE_PANEL_WIDTH = 500;
    
    public static final int TREE_PANEL_HEIGHT = 300;
    
    public static final int MOL_PANEL_WIDTH = 500;
    
    public static final int MOL_PANEL_HEIGHT = 300;
    
    public Debugger() {
        setLayout(new BorderLayout());
        thumbViewer = new GraphThumbViewer(
                THUMB_PANEL_WIDTH, THUMB_PANEL_HEIGHT);
        thumbViewer.setBorder(BorderFactory.createEtchedBorder());
        thumbViewer.addSelectionListener(this);
        add(thumbViewer, BorderLayout.WEST);
        
        JPanel centralPanel = new JPanel(new GridLayout(2, 1));
        mainGraphPanel = new GraphPanel(
                GRAPH_PANEL_WIDTH, GRAPH_PANEL_HEIGHT, false);
        mainGraphPanel.setBorder(BorderFactory.createEtchedBorder());
        mainGraphPanel.addMouseListener(this);
        centralPanel.add(mainGraphPanel);
        
        targetTreePanel = new ColoredTreePanel(TREE_PANEL_WIDTH, TREE_PANEL_HEIGHT);
        targetTreePanel.setBorder(BorderFactory.createEtchedBorder());
        centralPanel.add(targetTreePanel);
        add(centralPanel, BorderLayout.CENTER);
        
        controlPanel = new ControlPanel();
        controlPanel.addButtonListener(this);
        add(controlPanel, BorderLayout.NORTH);
        
        JPanel rightPanel = new JPanel(new GridLayout(2, 1));
        molPanel = new MoleculePanel(MOL_PANEL_WIDTH, MOL_PANEL_HEIGHT);
        rightPanel.add(molPanel);
        
        actualTreePanel = new ColoredTreePanel(TREE_PANEL_WIDTH, TREE_PANEL_HEIGHT);
        rightPanel.add(actualTreePanel);
        add(rightPanel, BorderLayout.EAST);
//        setPreferredSize(new Dimension(1200, 600));
        
        pack();
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("RUN")) {
            List<String> signatures = controlPanel.getSignatures();
            List<Integer> counts = controlPanel.getCounts();
            String formula = controlPanel.getCurrentFormula();
            if (signatures.size() != counts.size()) {
                System.err.println("SIGS != COUNTS");
                return;
            }
            System.out.println("formula = " + formula);
            for (int i = 0; i < signatures.size(); i++) {
                System.out.println(signatures.get(i) + " x" + counts.get(i));
            }
            
            run(formula, signatures, counts);
        } else if (e.getActionCommand().equals("CLEAR")) {
            thumbViewer.clear();
            mainGraphPanel.clear();
            targetTreePanel.clear();
            actualTreePanel.clear();
            molPanel.clear();
            repaint();
        }
    }

    private void run(
            String formula, List<String> signatures, List<Integer> counts) {
        // XXX height!
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        for (int i = 0; i < signatures.size(); i++) {
            tms.add(signatures.get(i), counts.get(i));
        }
        enumerator = new DeterministicEnumerator(formula, tms);
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
    }

    public void atomSaturated(AtomSaturationEvent atomSaturationEvent) {
        thumbViewer.addGraph(atomSaturationEvent.graph);
        thumbViewer.repaint();
    }

    public void bondAdded(BondCreationEvent bondCreationEvent) {
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
    
    public void displayTargetSignature(int atomIndex) {
        List<String> signatures = controlPanel.getSignatures();
        String selectedSignature = signatures.get(atomIndex);
        targetTreePanel.setTree(
                AbstractVertexSignature.parse(selectedSignature));
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
        int selected = mainGraphPanel.select(x, y);
        int target = mainGraphPanel.getTarget();
        if (target != -1) {
            displayTargetSignature(target);
            molPanel.selectAtom(selected);
            displayActualSignature(selected);
        }
        repaint();
    }

    public void mouseReleased(MouseEvent e) {}


    public static void main(String[] args) {
        new Debugger();
    }

}
