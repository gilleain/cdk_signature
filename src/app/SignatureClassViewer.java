package app;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.signature.MoleculeSignature;
import org.openscience.cdk.signature.Orbit;

public class SignatureClassViewer extends JFrame implements ListSelectionListener {
    
    private MoleculePanel moleculePanel;
    
    private TreeThumbViewer treeThumbViewer;
    
    private Map<String, Orbit> orbitMap;
    
    private AtomSymmetryClassGenerator atomSymmetryClassGenerator;
    
    public SignatureClassViewer(String[] args) {
        setLayout(new BorderLayout());
        
        List<IAtomContainerGenerator> initialGenerators = 
            new ArrayList<IAtomContainerGenerator>();
        atomSymmetryClassGenerator = new AtomSymmetryClassGenerator();
        initialGenerators.add(atomSymmetryClassGenerator);
        
        moleculePanel = new MoleculePanel(700, 700, initialGenerators);
        add(moleculePanel, BorderLayout.CENTER);
        
        treeThumbViewer = new TreeThumbViewer(700, 700);
        add(treeThumbViewer, BorderLayout.EAST);
        treeThumbViewer.addSelectionListener(this);
        
        orbitMap = new HashMap<String, Orbit>();
        
        String filename;
        if (args.length != 0) {
            filename = args[0];
            loadFile(filename);
        }
        pack();
        setVisible(true);
    }
    
    public void loadFile(String filename)  {
        try {
            ReaderFactory factory = new ReaderFactory();
            ISimpleChemObjectReader reader =
                new MDLReader(new FileReader(filename));
//                factory.createReader(new FileReader(filename));
            if (reader == null) return;
            IMolecule molecule = reader.read(new Molecule());
            System.out.println("read");
            moleculePanel.setMoleculeWithoutLayout(molecule);
            for (int i = 0; i < molecule.getAtomCount(); i++) {
                List<IAtom> connected = 
                    molecule.getConnectedAtomsList(molecule.getAtom(i));
                System.out.print(connected.size() + " " + (i + 1) + " ");
                for (IAtom a : connected) {
                    int j = molecule.getAtomNumber(a) + 1;
                    System.out.print(j + ",");
                }
                System.out.println();
            }
            makeSignatures(molecule);
            
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CDKException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
    
    public void makeSignatures(IMolecule molecule) {
        MoleculeSignature molSig = new MoleculeSignature(molecule);
        List<Orbit> orbits = molSig.calculateOrbits();
        orbitMap.clear();
        for (Orbit o : orbits) {
            String sig = o.getLabel();
            treeThumbViewer.addSignature(sig);
            orbitMap.put(sig, o);
        }
    }
    
    public void displaySelectedOrbits(List<Orbit> orbits) {
        atomSymmetryClassGenerator.setOrbits(orbits);
        repaint();
    }
    
    
    public static void main(String[] args) {
//        new SignatureClassViewer(args);
        new SignatureClassViewer(new String[] {
//        "/Users/maclean/bucky_proper_laidout.mol"});
        "/Users/maclean/Downloads/c70.mol"});
//        "/Users/maclean/buckyball.mol"});
    }

    public void valueChanged(ListSelectionEvent e) {
        List<String> selectedList = treeThumbViewer.getSelected();
        List<Orbit> selectedOrbits = new ArrayList<Orbit>();
        for (String selected : selectedList) {
            System.out.println("selected " + orbitMap.get(selected));
            selectedOrbits.add(orbitMap.get(selected));
        }
        displaySelectedOrbits(selectedOrbits);
    }

}
