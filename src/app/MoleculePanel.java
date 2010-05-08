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
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BoundsGenerator;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;

public class MoleculePanel extends JPanel {
    
    private AtomContainerRenderer renderer;
    
    private StructureDiagramGenerator sdg;
    
    private IMolecule molecule;
    
    public int moleculeWidth;
    
    public int moleculeHeight;
    
    public MoleculePanel(int panelWidth, int panelHeight) {
        renderer = new AtomContainerRenderer(getGenerators(), new AWTFontManager());
        setRenderingParameters();
        sdg = new StructureDiagramGenerator();
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
        this.setBackground(Color.WHITE);
    }
    
    private void setRenderingParameters() {
        
//        renderer.getRenderer2DModel().setCompactShape(AtomShape.OVAL);
//        renderer.getRenderer2DModel().setIsCompact(true);
//        renderer.getRenderer2DModel().setKekuleStructure(true);
//        renderer.getRenderer2DModel().setAtomRadius(0.1);
//        renderer.getRenderer2DModel().setBondWidth(2);
//        renderer.getRenderer2DModel().setBondDistance(0.1);
        renderer.getRenderer2DModel().setScale(0.75);
    }

    private List<IAtomContainerGenerator> getGenerators() {
        List<IAtomContainerGenerator> generators = 
            new ArrayList<IAtomContainerGenerator>();
        generators.add(new IAtomContainerGenerator() {
            public IRenderingElement generate(IAtomContainer ac, RendererModel m) {
                return new BoundsGenerator().generate((IMolecule)ac, m);
            }

            public List<IGeneratorParameter<?>> getParameters() {
                return new ArrayList<IGeneratorParameter<?>>();
            }

        });
//        generators.add(new RingGenerator());
//        generators.add(new BasicBondGenerator());
        generators.add(new TmpBondGenerator());
        generators.add(new BasicAtomGenerator());
        
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
                    System.out.println("removing atom " + i);
                } else {
                    System.out.println("keeping atom " + i);
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
                renderer.paintMolecule(
                        molecule, new AWTDrawVisitor(g2), getBounds(), false);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }

        }
    }
    
}