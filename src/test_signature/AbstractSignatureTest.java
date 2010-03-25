package test_signature;

import org.openscience.cdk.Atom;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class AbstractSignatureTest {
    
    public static NoNotificationChemObjectBuilder builder =
        NoNotificationChemObjectBuilder.getInstance();
    
    public static IMolecule makeDodecahedrane() {
        IMolecule dodec = builder.newMolecule();
        for (int i = 0; i < 20; i++) {
            dodec.addAtom(builder.newAtom("C"));
        }
        dodec.addBond(0, 1, IBond.Order.SINGLE);
        dodec.addBond(0, 4, IBond.Order.SINGLE);
        dodec.addBond(0, 5, IBond.Order.SINGLE);
        dodec.addBond(1, 2, IBond.Order.SINGLE);
        dodec.addBond(1, 6, IBond.Order.SINGLE);
        dodec.addBond(2, 3, IBond.Order.SINGLE);
        dodec.addBond(2, 7, IBond.Order.SINGLE);
        dodec.addBond(3, 4, IBond.Order.SINGLE);
        dodec.addBond(3, 8, IBond.Order.SINGLE);
        dodec.addBond(4, 9, IBond.Order.SINGLE);
        dodec.addBond(5, 10, IBond.Order.SINGLE);
        dodec.addBond(5, 14, IBond.Order.SINGLE);
        dodec.addBond(6, 10, IBond.Order.SINGLE);
        dodec.addBond(6, 11, IBond.Order.SINGLE);
        dodec.addBond(7, 11, IBond.Order.SINGLE);
        dodec.addBond(7, 12, IBond.Order.SINGLE);
        dodec.addBond(8, 12, IBond.Order.SINGLE);
        dodec.addBond(8, 13, IBond.Order.SINGLE);
        dodec.addBond(9, 13, IBond.Order.SINGLE);
        dodec.addBond(9, 14, IBond.Order.SINGLE);
        dodec.addBond(10, 16, IBond.Order.SINGLE);
        dodec.addBond(11, 17, IBond.Order.SINGLE);
        dodec.addBond(12, 18, IBond.Order.SINGLE);
        dodec.addBond(13, 19, IBond.Order.SINGLE);
        dodec.addBond(14, 15, IBond.Order.SINGLE);
        dodec.addBond(15, 16, IBond.Order.SINGLE);
        dodec.addBond(15, 19, IBond.Order.SINGLE);
        dodec.addBond(16, 17, IBond.Order.SINGLE);
        dodec.addBond(17, 18, IBond.Order.SINGLE);
        dodec.addBond(18, 19, IBond.Order.SINGLE);
        
        return dodec;
    }
    
    public static IMolecule makeCage() {
        /*
         * This 'molecule' is the example used to illustrate the
         * algorithm outlined in the 2004 Faulon &ct. paper
         */
        IMolecule cage = builder.newMolecule();
        for (int i = 0; i < 16; i++) {
            cage.addAtom(builder.newAtom("C"));
        }
        cage.addBond(0, 1, IBond.Order.SINGLE);
        cage.addBond(0, 3, IBond.Order.SINGLE);
        cage.addBond(0, 4, IBond.Order.SINGLE);
        cage.addBond(1, 2, IBond.Order.SINGLE);
        cage.addBond(1, 6, IBond.Order.SINGLE);
        cage.addBond(2, 3, IBond.Order.SINGLE);
        cage.addBond(2, 8, IBond.Order.SINGLE);
        cage.addBond(3, 10, IBond.Order.SINGLE);
        cage.addBond(4, 5, IBond.Order.SINGLE);
        cage.addBond(4, 11, IBond.Order.SINGLE);
        cage.addBond(5, 6, IBond.Order.SINGLE);
        cage.addBond(5, 12, IBond.Order.SINGLE);
        cage.addBond(6, 7, IBond.Order.SINGLE);
        cage.addBond(7, 8, IBond.Order.SINGLE);
        cage.addBond(7, 13, IBond.Order.SINGLE);
        cage.addBond(8, 9, IBond.Order.SINGLE);
        cage.addBond(9, 10, IBond.Order.SINGLE);
        cage.addBond(9, 14, IBond.Order.SINGLE);
        cage.addBond(10, 11, IBond.Order.SINGLE);
        cage.addBond(11, 15, IBond.Order.SINGLE);
        cage.addBond(12, 13, IBond.Order.SINGLE);
        cage.addBond(12, 15, IBond.Order.SINGLE);
        cage.addBond(13, 14, IBond.Order.SINGLE);
        cage.addBond(14, 15, IBond.Order.SINGLE);
        return cage;
    }

    /**
     * Strictly speaking, this is more like a cube than cubane, as it has no
     * hydrogens.
     * 
     * @return
     */
    public static IMolecule makeCubane() {
        IMolecule mol = builder.newMolecule();
        mol.addAtom(new Atom("C")); // 0
        mol.addAtom(new Atom("C")); // 1
        mol.addAtom(new Atom("C")); // 2
        mol.addAtom(new Atom("C")); // 3
        mol.addAtom(new Atom("C")); // 4
        mol.addAtom(new Atom("C")); // 5
        mol.addAtom(new Atom("C")); // 6
        mol.addAtom(new Atom("C")); // 7
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(0, 7, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(1, 6, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE); 
        mol.addBond(2, 5, IBond.Order.SINGLE); 
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(4, 7, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(6, 7, IBond.Order.SINGLE);
        return mol;
    }

    public static IMolecule makeCuneane() {
        IMolecule mol = builder.newMolecule();
        mol.addAtom(new Atom("C")); // 0
        mol.addAtom(new Atom("C")); // 1
        mol.addAtom(new Atom("C")); // 2
        mol.addAtom(new Atom("C")); // 3
        mol.addAtom(new Atom("C")); // 4
        mol.addAtom(new Atom("C")); // 5
        mol.addAtom(new Atom("C")); // 6
        mol.addAtom(new Atom("C")); // 7
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(0, 5, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(1, 7, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(2, 7, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(4, 6, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(6, 7, IBond.Order.SINGLE);
        return mol;
    }
    
    public static IMolecule makeCyclobutane() {
        IMolecule mol = builder.newMolecule();
        mol.addAtom(new Atom("C")); // 0
        mol.addAtom(new Atom("C")); // 1
        mol.addAtom(new Atom("C")); // 2
        mol.addAtom(new Atom("C")); // 3
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        return mol;
    }
    
    public static IMolecule makeBridgedCyclobutane() {
        IMolecule mol = AbstractSignatureTest.makeCyclobutane();
        mol.addBond(0, 2, IBond.Order.SINGLE);
        return mol;
    }

    public static IMolecule makeNapthalene() {
        IMolecule mol = builder.newMolecule();
        mol.addAtom(new Atom("C")); // 0
        mol.addAtom(new Atom("C")); // 1
        mol.addAtom(new Atom("C")); // 2
        mol.addAtom(new Atom("C")); // 3
        mol.addAtom(new Atom("C")); // 4
        mol.addAtom(new Atom("C")); // 5
        mol.addAtom(new Atom("C")); // 6
        mol.addAtom(new Atom("C")); // 7
        mol.addAtom(new Atom("C")); // 8
        mol.addAtom(new Atom("C")); // 9
        for (IAtom atom : mol.atoms()) {
            atom.setFlag(CDKConstants.ISAROMATIC, true);
        }
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(2, 7, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE); 
        mol.addBond(5, 6, IBond.Order.SINGLE); 
        mol.addBond(6, 7, IBond.Order.SINGLE);
        mol.addBond(7, 8, IBond.Order.SINGLE);
        mol.addBond(8, 9, IBond.Order.SINGLE);
        mol.addBond(9, 0, IBond.Order.SINGLE);
        for (IBond bond : mol.bonds()) {
            bond.setFlag(CDKConstants.ISAROMATIC, true);
        }
        return mol; 
    }

    public static IMolecule makeHexane() {
        IMolecule mol = builder.newMolecule();
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        
        return mol;
    }

    public static IMolecule makeBenzene() {
        IMolecule mol = builder.newMolecule();
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        for (IAtom atom : mol.atoms()) {
            atom.setFlag(CDKConstants.ISAROMATIC, true);
        }
        
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(5, 0, IBond.Order.SINGLE);
        for (IBond bond : mol.bonds()) {
            bond.setFlag(CDKConstants.ISAROMATIC, true);
        }
        return mol;
    }

    /**
     * This may not be a real molecule, but it is a good, simple test. 
     * It is something like cyclobutane with a single carbon bridge across it,
     * or propellane without one of its bonds (see makePropellane).
     *  
     * @return
     */
    public static IMolecule makePseudoPropellane() {
        IMolecule mol = builder.newMolecule();
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        mol.addAtom(new Atom("C"));
        
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 2, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);
        mol.addBond(1, 4, IBond.Order.SINGLE);
        mol.addBond(2, 4, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        
        return mol;
    }

    public static IMolecule makePropellane() {
        IMolecule mol = AbstractSignatureTest.makePseudoPropellane();
        mol.addBond(0, 4, IBond.Order.SINGLE);
        return mol;
    }

}
