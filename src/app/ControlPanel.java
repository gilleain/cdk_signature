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

public class ControlPanel extends JPanel implements ActionListener {
    
    private JButton testValues; // XXX TMP
    
    private JButton addFormulaButton;
    
    private JButton addSignatureButton;
    
    private JButton runButton;
    
    private JTextField formulaField;
    
    private JTextField signatureField;
    
    private JSpinner counterField;
    
    private String currentFormula;
    
    private List<String> signatures;
    
    private List<Integer> counts;
    
    public ControlPanel() {
        setLayout(new GridLayout(1, 8));
        
        testValues = new JButton("Test");
        testValues.setActionCommand("TEST");
        testValues.addActionListener(this);
        add(testValues);
        
        runButton = new JButton("Run");
        add(runButton);
        
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
        
        signatures = new ArrayList<String>();
        counts = new ArrayList<Integer>();
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
    
    public void addRunListener(ActionListener listener) {
        runButton.addActionListener(listener);
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
            counts.add(2);
            counts.add(2);
            counts.add(2);
            counts.add(12);
            currentFormula = "C6H12";
        }
    }

}
