package app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormulaControlPanel extends JPanel implements ActionListener {
    
    private JButton addFormulaButton;
    
    private JButton runButton;
    
    private JTextField formulaField;
    
    private String currentFormula;
    
    private JButton selectAtomSaturationListenerButton;
    
    private JButton selectBondCreationListenerButton;
    
    private JButton selectBondRejectionListenerButton;
    
    private JButton selectOrbitSaturationListenerButton;
    
    private JButton clearButton;
    
    public enum ListenerType { 
        BOND_CREATION, ATOM_SATURATION, ORBIT_SATURATION, BOND_REJECTION 
    };
    
    private ListenerType selectedListenerType;
    
    public FormulaControlPanel() {
        setLayout(new GridLayout(2, 5));
        
        runButton = new JButton("Run");
        runButton.setActionCommand("RUN");
        add(runButton);
        
        clearButton = new JButton("Clear");
        clearButton.setActionCommand("CLEAR");
        add(clearButton);
        
        addFormulaButton = new JButton("Add Formula");
        addFormulaButton.setActionCommand("ADDF");
        addFormulaButton.addActionListener(this);
        add(addFormulaButton);
        
        formulaField = new JTextField();
        currentFormula = "C4H4";
        formulaField.setText(currentFormula);
        add(formulaField);
        
        selectAtomSaturationListenerButton = new JButton("ATOM_SAT");
        selectAtomSaturationListenerButton.setActionCommand("SELECT_ATOM_SAT");
        selectAtomSaturationListenerButton.addActionListener(this);
        add(selectAtomSaturationListenerButton);
        
        selectBondCreationListenerButton = new JButton("BOND_ACC");
        selectBondCreationListenerButton.setActionCommand("SELECT_BOND_CRE");
        selectBondCreationListenerButton.addActionListener(this);
        add(selectBondCreationListenerButton);
        
        selectBondRejectionListenerButton = new JButton("BOND_REJ");
        selectBondRejectionListenerButton.setActionCommand("SELECT_BOND_REJ");
        selectBondRejectionListenerButton.addActionListener(this);
        add(selectBondRejectionListenerButton);
        
        selectOrbitSaturationListenerButton = new JButton("ORBIT_SAT");
        selectOrbitSaturationListenerButton.setActionCommand("SELECT_ORBIT_SAT");
        selectOrbitSaturationListenerButton.addActionListener(this);
        add(selectOrbitSaturationListenerButton);
        
        selectedListenerType = ListenerType.BOND_CREATION;
    }
    
    public String getCurrentFormula() {
        return currentFormula;
    }
    
    public void addButtonListener(ActionListener listener) {
        runButton.addActionListener(listener);
        clearButton.addActionListener(listener);
    }
    
    public ListenerType getSelectedListenerType() {
        return selectedListenerType;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ADDF")) {
            currentFormula = formulaField.getText();
            System.out.println("adding formula " + currentFormula);
        } else if (e.getActionCommand().equals("SELECT_ATOM_SAT")) {
            selectedListenerType = ListenerType.ATOM_SATURATION;
        } else if (e.getActionCommand().equals("SELECT_BOND_CRE")) {
            selectedListenerType = ListenerType.BOND_CREATION;
        } else if (e.getActionCommand().equals("SELECT_BOND_REJ")) {
            selectedListenerType = ListenerType.BOND_REJECTION;
        } else if (e.getActionCommand().equals("SELECT_ORBIT_SAT")) {
            selectedListenerType = ListenerType.ORBIT_SATURATION;
        }
    }

}
