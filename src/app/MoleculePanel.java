package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.openscience.cdk.deterministic.Graph;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.renderer.selection.IChemObjectSelection;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;

public class MoleculePanel extends JPanel {
    
    private AtomContainerRenderer renderer;
    
    private StructureDiagramGenerator sdg;
    
    private IMolecule molecule;
    
    public int moleculeWidth;
    
    public int moleculeHeight;

    private BasicAtomGenerator basicAtomGenerator;
    
    public MoleculePanel(int panelWidth, int panelHeight) {
        this(panelWidth, panelHeight, new ArrayList<IAtomContainerGenerator>());
    }
    
    public MoleculePanel(int panelWidth, int panelHeight, 
            List<IAtomContainerGenerator> initialGenerators) {
        initialGenerators.addAll(getGenerators());
        renderer = new AtomContainerRenderer(
                initialGenerators, new AWTFontManager());
        setRenderingParameters();
        sdg = new StructureDiagramGenerator();
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(Color.WHITE);
    }
    
    public void selectAtom(int atomIndex) {
        IChemObjectSelection selection = 
            new BondFlagSelection(atomIndex, molecule);
        renderer.getRenderer2DModel().setSelection(selection);
    }
    
    private void setRenderingParameters() {
        RendererModel model = renderer.getRenderer2DModel();
        model.setDrawNumbers(true);
        model.getRenderingParameter(
                BasicAtomGenerator.CompactShape.class).setValue(
                        BasicAtomGenerator.Shape.OVAL);
        model.getRenderingParameter(
                BasicAtomGenerator.CompactAtom.class).setValue(true);
        model.getRenderingParameter(
                BasicAtomGenerator.KekuleStructure.class).setValue(true);
//        for (IGeneratorParameter p : model.getRenderingParameters()) {
//            System.out.println(p.getClass().getSimpleName() + " " + p.getValue());
//        }
//        System.out.println("IS COMPACT : " + model.getRenderingParameter(
//                BasicAtomGenerator.CompactAtom.class).getValue());
    }

    private List<IAtomContainerGenerator> getGenerators() {
        List<IAtomContainerGenerator> generators = 
            new ArrayList<IAtomContainerGenerator>();
//        generators.add(new RingGenerator());
//        generators.add(new BasicBondGenerator());
        generators.add(new TmpBondGenerator());
        basicAtomGenerator = new BasicAtomGenerator(); 
        generators.add(basicAtomGenerator);
        
        return generators;
    }
    
    public void setMoleculeFromGraph(Graph graph) {
        IAtomContainer atomContainer = graph.getAtomContainer();
        
        try {
            IAtomContainer clonedContainer = 
                (IAtomContainer) atomContainer.clone();
            int i = 0;
            List<IAtom> keptAtoms = new ArrayList<IAtom>();
            for (IAtom atom : clonedContainer.atoms()) {
                if (clonedContainer.getConnectedAtomsCount(atom) == 0) {
//                    clonedContainer.removeAtom(atom);
//                    System.out.println("removing atom " + i);
                } else {
//                    System.out.println("keeping atom " + i);
                    keptAtoms.add(atom);
                }
                i++;
            }
            IAtom[] keptAtomArr = new IAtom[keptAtoms.size()];
            for (int j = 0; j < keptAtoms.size(); j++) { 
                keptAtomArr[j] = keptAtoms.get(j); 
            }
            clonedContainer.setAtoms(keptAtomArr);
//            System.out.println(new Graph(mol));
            if (ConnectivityChecker.isConnected(clonedContainer)) {
                System.out.println("CONNECTED");
                IMolecule mol = 
                    clonedContainer.getBuilder().newInstance(IMolecule.class);
                for (IAtom a : clonedContainer.atoms()) { mol.addAtom(a); }
                for (IBond b : clonedContainer.bonds()) { mol.addBond(b); }
                setMolecule(mol);
            } else {
                System.out.println("NOT CONNECTED");
                IMoleculeSet molecules = 
                    ConnectivityChecker.partitionIntoMolecules(clonedContainer);
                setMolecule(molecules.getMolecule(0));
            }
        } catch (CloneNotSupportedException cnse) {
            
        }
    }
    
    public void setMolecule(IMolecule molecule) {
        this.molecule = diagramGenerate(molecule);
        for (int i = 0; i < molecule.getAtomCount(); i++) {
            this.molecule.getAtom(i).setFormalCharge(0);
        }
        System.out.println("Setting molecule " + new Graph(molecule));
        repaint();
    }

    public void setMoleculeWithoutLayout(IMolecule molecule) {
        this.molecule = molecule;
        repaint();
    }
    
    private IMolecule diagramGenerate(IMolecule molecule) {
        this.sdg.setMolecule(molecule, true);
        try {
            this.sdg.generateCoordinates();
        } catch (Exception c) {
            c.printStackTrace();
            return null;
        }
        return sdg.getMolecule();
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        if (molecule != null) {
            Graphics2D g2 = (Graphics2D) g;
            try {
                renderer.setup(molecule, getBounds());
                renderer.paintMolecule(
                        molecule, new AWTDrawVisitor(g2), getBounds(), false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }

        }
    }

    public void clear() {
        this.molecule = null;
    }
    
}