package engine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.group.Graph;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;



/**
 * Uses a Generator to make graphs, and then convert those graphs into IMolecule
 * objects. ONLY WORKS FOR HYDROCARBONS...
 * 
 * 
 * @author maclean
 *
 */
public class CarbonGenerator {
    
    public static IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public static SmilesGenerator smilesGenerator = new SmilesGenerator();
    
    /**
     * Generate a subset of the space for each partition, and then check that
     * subset for duplicates. 
     * 
     * @param valence
     * @param atomNum
     * @return
     */
    public static List<IMolecule> partitionGenerate(int valence, int atomNum) {
        List<IMolecule> molecules = new ArrayList<IMolecule>();
        IntegerPartition[] partitions = PartitionCalculator.partition(valence, atomNum);
        for (IntegerPartition partition : partitions) {
            if (partition.getPart(0) > 4) continue;
            List<IMolecule> subset = CarbonGenerator.generateForPartition(partition);
            molecules.addAll(subset);
        }
        return molecules;
    }
    
    /**
     * The hopeful process that does NOT check for duplicates.
     * 
     * @param valence
     * @param atomNum
     * @return
     */
    public static List<IMolecule> partitionGenerateWithDuplicates(int valence, int atomNum) {
        List<IMolecule> molecules = new ArrayList<IMolecule>();
        for (Graph graph : PartitioningGenerator.generate(valence, atomNum, 4)) {
            molecules.add(CarbonGenerator.convert(graph));
        }
        return molecules;
    }

    /**
     * Given a particular partition, generate all the resulting structures, and
     * do an all-v-all check on them to remove duplicates
     *  
     * @param partition
     * @return
     */
    public static List<IMolecule> generateForPartition(IntegerPartition partition) {
        List<Graph> graphs = new ArrayList<Graph>();
        PartitioningGenerator.generateForPartition(partition, graphs);
        List<IMolecule> subset = new ArrayList<IMolecule>();
        for (Graph graph : graphs) {
            IMolecule mol = CarbonGenerator.convert(graph);
            boolean unique = true;
            for (IMolecule other : subset) {
                try {
                    if (UniversalIsomorphismTester.isIsomorph(mol, other)) {
                        unique = false;
                        break;
                    }
                } catch (CDKException c) {
                    
                }
            }
            if (unique) {
                subset.add(mol);
            }
        }
        return subset;
    }
    
    public static IMolecule convert(Graph graph) {
        IMolecule molecule = builder.newInstance(IMolecule.class);
        for (String label : graph.getLabels()) {
            molecule.addAtom(builder.newInstance(IAtom.class, label));
        }
        
        for (Graph.Edge arc : graph.edges) {
            int l = arc.a;
            int r = arc.b;
            int o = arc.o;
            molecule.addBond(l, r, IBond.Order.values()[o - 1]);
        }
        return molecule;
    }
    
    /**
     * Generate the molecules, but write them out as they are generated, in
     * bunches that share the same partition.
     * 
     * @param filename
     * @param valence
     * @param atomNum
     */
    public static void toSDF(String filename, int valence, int atomNum) {
        try {
            MDLWriter writer = new MDLWriter(new FileWriter(filename));
            StructureDiagramGenerator sdg = new StructureDiagramGenerator();
            IntegerPartition[] partitions = PartitionCalculator.partition(valence, atomNum);
            for (IntegerPartition partition : partitions) {
                if (partition.getPart(0) > 4) continue;
                List<IMolecule> subset = CarbonGenerator.generateForPartition(partition);
                for (IMolecule container : subset) {
                    try {
                        if (ConnectivityChecker.isConnected(container)) {
                            sdg.setMolecule(container);
                            sdg.generateCoordinates();
                            writer.write(sdg.getMolecule());
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int valence = Integer.parseInt(args[0]);
        int numberOfAtoms = Integer.parseInt(args[1]);
//            List<IMolecule> molecules = 
//                CarbonGenerator.partitionGenerate(valence, numberOfAtoms);
        CarbonGenerator.toSDF(args[2], valence, numberOfAtoms);
    }
}
