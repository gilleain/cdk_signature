package app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class SignatureToMoleculeApp extends JFrame implements ActionListener {
    
    private MoleculePanel molPanel;
    
    private JTextField signatureField;
    
    public SignatureToMoleculeApp() {
        super("Paste a signature into the text field");
        
        setLayout(new BorderLayout());
        molPanel = new MoleculePanel(500, 500);
        add(molPanel, BorderLayout.CENTER);
        
        signatureField = new JTextField();
        signatureField.addActionListener(this);
        add(signatureField, BorderLayout.SOUTH);
        
        pack();
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String signature = signatureField.getText().trim();
        molPanel.setMoleculeFromSignature(signature);
        repaint();
    }

    public static void main(String[] args) {
        new SignatureToMoleculeApp();
    }

}
