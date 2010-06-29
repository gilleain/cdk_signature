package app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

public class SignatureControlPanel extends JPanel implements ActionListener {
    
    private JButton testValues; // XXX TMP
    
    private JButton addFormulaButton;
    
    private JButton addSignatureButton;
    
    private JButton runButton;
    
    private JTextField formulaField;
    
    private JTextField signatureField;
    
    private JSpinner counterField;
    
    private String currentFormula;
    
    private JButton selectAtomSaturationListenerButton;
    
    private JButton selectBondCreationListenerButton;
    
    private JButton selectBondRejectionListenerButton;
    
    private JButton selectOrbitSaturationListenerButton;
    
    private JButton clearButton;
    
    private List<String> signatures;
    
    private List<Integer> counts;
    
    public enum ListenerType { 
        BOND_CREATION, ATOM_SATURATION, ORBIT_SATURATION, BOND_REJECTION 
    };
    
    private ListenerType selectedListenerType;
    
    public SignatureControlPanel() {
        setLayout(new GridLayout(2, 6));
        
        testValues = new JButton("Test");
        testValues.setActionCommand("TEST");
        testValues.addActionListener(this);
        add(testValues);
        
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
        add(formulaField);
        
        addSignatureButton = new JButton("Add Signature");
        addSignatureButton.setActionCommand("ADDS");
        addSignatureButton.addActionListener(this);
        add(addSignatureButton);
        
        signatureField = new JTextField();
        add(signatureField);
        
        counterField = new JSpinner();
        add(counterField);
        
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
        
        signatures = new ArrayList<String>();
        counts = new ArrayList<Integer>();
        
        selectedListenerType = ListenerType.BOND_CREATION;
    }
    
    public String getCurrentFormula() {
        return currentFormula;
    }
    
    public List<String> getSignatures() {
        return signatures;
    }
    
    public List<Integer> getCounts() {
        return counts;
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
        } else if (e.getActionCommand().equals("ADDS")) {
            String signature = signatureField.getText().trim();
            int count = (int) ((Integer)counterField.getValue());
            System.out.println("adding signature " + signature + " " + count);
            signatures.add(signature);
            counts.add(count);
        } else if (e.getActionCommand().equals("TEST")) {
            signatures.clear();
            signatures.add("[C]([C][C][C][H])");
            signatures.add("[C]([C][C][H][H])");
            signatures.add("[C]([C][H][H][H])");
            signatures.add("[H]([C])");
            counts.add(1);
            counts.add(3);
            counts.add(1);
            counts.add(10);
            currentFormula = "C5H10";
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
