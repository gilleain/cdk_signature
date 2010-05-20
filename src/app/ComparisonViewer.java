package app;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import signature.AbstractVertexSignature;
import signature.display.ColoredTreePanel;

public class ComparisonViewer extends JFrame {
    
    public class SignatureFieldTreePanel extends JPanel implements ActionListener {
        
        private ColoredTreePanel treePanel;
        
        private JTextField signatureStringField;
        
        public SignatureFieldTreePanel() {
            setLayout(new BorderLayout());
            
            treePanel = new ColoredTreePanel(1000, 400);
            treePanel.setDrawKey(true);
            add(treePanel, BorderLayout.CENTER);
            
            signatureStringField = new JTextField();
            signatureStringField.addActionListener(this);
            add(signatureStringField, BorderLayout.SOUTH);
        }

        public void actionPerformed(ActionEvent e) {
            String signature = signatureStringField.getText();
            treePanel.setTree(AbstractVertexSignature.parse(signature));
            treePanel.repaint();
        }
        
    }
    
    private SignatureFieldTreePanel upperPanel;
    
    private SignatureFieldTreePanel lowerPanel;
    
    public ComparisonViewer() {
        setLayout(new GridLayout(2, 1));
        
        upperPanel = new SignatureFieldTreePanel();
        add(upperPanel);
        
        lowerPanel = new SignatureFieldTreePanel();
        add(lowerPanel);
        
        pack();
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new ComparisonViewer();
    }

}
