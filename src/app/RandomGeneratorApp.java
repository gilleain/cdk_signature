package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.structgen.deterministic.TargetMolecularSignature;
import org.openscience.cdk.structgen.stochastic.RandomSignatureStructureGenerator;

public class RandomGeneratorApp extends JFrame implements ActionListener {
    
    private JPanel molPanel;
    
    private JPanel buttonPanel;
    
    private JButton goButton;
    
    private JSpinner countSelector;
    
    private JScrollPane scrollPane;
    
    private RandomSignatureStructureGenerator generator;

    public RandomGeneratorApp() {
        setLayout(new BorderLayout());
        
        molPanel = new JPanel();
        scrollPane = new JScrollPane(
                molPanel, 
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, 
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(750, 280));
        add(scrollPane, BorderLayout.CENTER);
        
        buttonPanel = new JPanel();
        goButton = new JButton("Go");
        goButton.addActionListener(this);
        
        countSelector = new JSpinner();
        countSelector.setValue(new Integer(3));
        
        buttonPanel.add(goButton);
        buttonPanel.add(countSelector);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        String formulaString = "C4H8";
        TargetMolecularSignature tms = new TargetMolecularSignature(1);
        tms.add("[H]([C])", 8);
        tms.add("[C]([C][C][C][H])", 1);
        tms.add("[C]([C][C][H][H])", 1);
        tms.add("[C]([C][H][H][H])", 2);
        generator = new RandomSignatureStructureGenerator(formulaString, tms);
        
        pack();
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e) {
        int count = (Integer) countSelector.getValue();
        for (int i = 0; i < count; i++) {
            IAtomContainer container = generator.generate();
            MoleculePanel moleculePanel = new MoleculePanel(250, 250);
            IMolecule mol = container.getBuilder().newInstance(
                    IMolecule.class, container);
            moleculePanel.setMolecule(mol);
            molPanel.add(moleculePanel);
        }
        pack();
    }

    public static void main(String[] args) {
        new RandomGeneratorApp();
    }
}
