//package app;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.JPanel;
//
//import model.GraphMoleculePair;
//
//import org.openscience.cdk.interfaces.IAtomContainer;
//import org.openscience.cdk.interfaces.IMolecule;
//import org.openscience.cdk.layout.StructureDiagramGenerator;
//import org.openscience.cdk.renderer.Renderer;
//import org.openscience.cdk.renderer.RendererModel;
//import org.openscience.cdk.renderer.RenderingParameters.AtomShape;
//import org.openscience.cdk.renderer.elements.IRenderingElement;
//import org.openscience.cdk.renderer.font.AWTFontManager;
//import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
//import org.openscience.cdk.renderer.generators.BoundsGenerator;
//import org.openscience.cdk.renderer.generators.IGenerator;
//import org.openscience.cdk.renderer.generators.RingGenerator;
//import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
//
//public class DisplayPanel extends JPanel {
//    
//    private AtomContainerRenderer renderer;
//    
//    private StructureDiagramGenerator sdg;
//    
//    private List<IMolecule> molecules;
//    
//    private ArrayList<GraphMoleculePair> moleculeMap;
//    
//    private int panelWidth;
//    
////    private int panelHeight;
//    
//    public int moleculeWidth;
//    
//    public int moleculeHeight;
//    
//    public DisplayPanel(int panelWidth, int panelHeight) {
//        renderer = new Renderer(getGenerators(), new AWTFontManager());
//        renderer.getRenderer2DModel().setCompactShape(AtomShape.OVAL);
//        renderer.getRenderer2DModel().setIsCompact(true);
//        renderer.getRenderer2DModel().setKekuleStructure(true);
//        renderer.getRenderer2DModel().setAtomRadius(0.1);
//        renderer.getRenderer2DModel().setBondWidth(2);
//        renderer.getRenderer2DModel().setBondDistance(0.1);
//        sdg = new StructureDiagramGenerator();
//        this.panelWidth = panelWidth;
////        this.panelHeight = panelHeight;
//        this.setPreferredSize(new Dimension(panelWidth, panelHeight));
//        this.setBackground(Color.WHITE);
//    }
//
//    private List<IGenerator> getGenerators() {
//        List<IGenerator> generators = new ArrayList<IGenerator>();
//        generators.add(new IGenerator() {
//            public IRenderingElement generate(IAtomContainer ac, RendererModel m) {
//                return new BoundsGenerator().generate((IMolecule)ac, m);
//            }
//        });
//        generators.add(new RingGenerator());
////        generators.add(new BasicBondGenerator());
//        generators.add(new BasicAtomGenerator());
//        
//        return generators;
//    }
//    
//    public void setMolecule(IMolecule molecule) {
//        this.molecules = new ArrayList<IMolecule>();
//        this.molecules.add(diagramGenerate(molecule));
//    }
//
//    public void setMolecules(List<IMolecule> molecules) {
//        this.molecules = new ArrayList<IMolecule>();
//        for (IMolecule molecule : molecules) {
//            IMolecule generated = diagramGenerate(molecule);
//            if (generated != null) {
//                this.molecules.add(generated);
//            }
//        }
//    }
//    
//    private IMolecule diagramGenerate(IMolecule molecule) {
//        this.sdg.setMolecule(molecule, true);
//        try {
//            this.sdg.generateCoordinates();
//        } catch (Exception c) {
////            c.printStackTrace();
//            return null;
//        }
//        return sdg.getMolecule();
//    }
//    
//    public void setMoleculeMap(ArrayList<GraphMoleculePair> pairList) {
//        this.moleculeMap = new ArrayList<GraphMoleculePair>();
//        for (GraphMoleculePair pair : pairList) {
//            IMolecule generated = diagramGenerate(pair.molecule);
//            if (generated != null) {
//                this.moleculeMap.add(
//                        new GraphMoleculePair(
//                                pair.graph, diagramGenerate(pair.molecule)));
//            } else {
//                System.err.println("error generating graph " + pair.graph);
//            }
//        }
//    }
//    
//    public void paint(Graphics g) {
//        super.paint(g);
//        if (molecules != null) {
//            paintMolecules(g);
//        } else {
//            paintGraphMolecules(g);
//        }
//    }
//    
//    private void paintMolecules(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g;
//        int columns = panelWidth / (moleculeWidth / 2);
////        int x = moleculeWidth / 4;
//        int x = moleculeWidth / 2;
////        int y = moleculeHeight / 4;
//        int y = moleculeHeight / 3;
//        int i = 0;
//        renderer.setDrawCenter(x, y);
//        for (IMolecule molecule : molecules) {
//            i++;
//            renderer.paintMolecule(molecule, new AWTDrawVisitor(g2));
//            if (i == columns) {
//                y += moleculeHeight / 2;
//                x = moleculeWidth / 2;
//                i = 0;
//            } else {
//                x += moleculeWidth / 2;
//            }
//            renderer.setDrawCenter(x, y);
//        }
//    }
//    
//    private void paintGraphMolecules(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g;
//        int columns = panelWidth / (moleculeWidth / 2);
//        int x = moleculeWidth / 4;
//        int y = moleculeHeight / 4;
//        int i = 0;
//        renderer.setDrawCenter(x, y);
//        for (GraphMoleculePair pair : moleculeMap) {
//            i++;
//            renderer.paintMolecule(pair.molecule, new AWTDrawVisitor(g2));
//            GraphRenderer.paintDiagram(pair.graph, g2, x, moleculeWidth / 2, y + moleculeHeight / 2);
//            if (i == columns) {
//                y += moleculeHeight;
//                x = moleculeWidth / 2;
//                i = 0;
//            } else {
//                x += moleculeWidth / 2;
//            }
//            renderer.setDrawCenter(x, y);
//        }
//    }
//}