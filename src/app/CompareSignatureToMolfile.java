package app;

import java.io.FileReader;
import java.io.IOException;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.signature.MoleculeFromSignatureBuilder;
import org.openscience.cdk.smsd.algorithm.cdk.CDKMCS;

import signature.AbstractVertexSignature;

public class CompareSignatureToMolfile {
    
    public static boolean compare(String molfilePath, String signature) {
        try {
            IMolecule molFromFile = readMolfile(molfilePath);
            IMolecule molFromSignature = moleculeFromSignature(signature);
            
            return CDKMCS.isIsomorph(molFromFile, molFromSignature);
        } catch (CDKException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static IMolecule moleculeFromSignature(String signature) {
        MoleculeFromSignatureBuilder builder = 
            new MoleculeFromSignatureBuilder(
                    NoNotificationChemObjectBuilder.getInstance());
        builder.makeFromColoredTree(AbstractVertexSignature.parse(signature));
        IAtomContainer container = builder.getAtomContainer();
        return container.getBuilder().newInstance(IMolecule.class, container);
    }

    private static IMolecule readMolfile(String molfilePath)
        throws CDKException, IOException {
        ISimpleChemObjectReader reader =
            new MDLReader(new FileReader(molfilePath));
        if (reader == null) return null;
        return reader.read(new Molecule());
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage : compare <molfile> <signature>");
        }
        String molfilePath = args[0];
        String signature = args[1];
        
        boolean result = CompareSignatureToMolfile.compare(molfilePath, signature);
        System.out.println(result);
    }

}
