package test_signature;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class AbstractSignatureTest {
    
    public static IChemObjectBuilder builder =
        NoNotificationChemObjectBuilder.getInstance();
        
    public static void print(IMolecule mol) {
        for (int i = 0; i < mol.getAtomCount(); i++) {
            IAtom a = mol.getAtom(i);
            System.out.print(a.getSymbol() + " " + i + " ");
        }
        System.out.println();
        for (IBond bond : mol.bonds()) {
            IAtom aa = bond.getAtom(0);
            IAtom ab = bond.getAtom(1);
            int o = bond.getOrder().ordinal() + 1;
            int x = mol.getAtomNumber(aa);
            int y = mol.getAtomNumber(ab);
            if (x < y) {
                System.out.print(x + "-" + y + "(" + o + "),");
            } else {
                System.out.print(y + "-" + x + "(" + o + "),");
            }
        }
    }
    
    public static void addHydrogens(IMolecule mol, int carbonIndex, int count) {
        for (int i = 0; i < count; i++) {
            mol.addAtom(builder.newInstance(IAtom.class, "H"));
            int hydrogenIndex = mol.getAtomCount() - 1;
            mol.addBond(carbonIndex, hydrogenIndex, IBond.Order.SINGLE);
        }
    }
    
    public static void addCarbons(IMolecule mol, int count) {
        for (int i = 0; i < count; i++) {
            mol.addAtom(builder.newInstance(IAtom.class, "C"));
        }
    }
    
    public static void addRing(int atomToAttachTo, int ringSize, IMolecule mol) {
        int numberOfAtoms = mol.getAtomCount();
        int previous = atomToAttachTo;
        for (int i = 0; i < ringSize; i++) {
            mol.addAtom(builder.newInstance(IAtom.class, "C"));
            int current = numberOfAtoms + i;
            mol.addBond(previous, current, IBond.Order.SINGLE);
            previous = current;
        }
        mol.addBond(
             numberOfAtoms, numberOfAtoms + (ringSize - 1), IBond.Order.SINGLE);
    }
    
    public static IMolecule makeBowtieane() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 10);
        mol.addBond(0, 1, IBond.Order.SINGLE);
//        mol.addBond(0, 4, IBond.Order.SINGLE);  // this makes the graph 3-reg
        mol.addBond(0, 7, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(1, 8, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(2, 6, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(3, 9, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(5, 9, IBond.Order.SINGLE);
        mol.addBond(6, 7, IBond.Order.SINGLE);
        mol.addBond(7, 8, IBond.Order.SINGLE);
        mol.addBond(8, 9, IBond.Order.SINGLE);
        
        return mol;
    }
    
    public static IMolecule makeBowtieaneA() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 10);
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 3, IBond.Order.SINGLE);  
        mol.addBond(0, 7, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(1, 9, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(2, 5, IBond.Order.SINGLE); // this makes the graph 3-reg
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 8, IBond.Order.SINGLE);
        mol.addBond(4, 9, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(5, 8, IBond.Order.SINGLE);
        mol.addBond(6, 7, IBond.Order.SINGLE);
        mol.addBond(6, 9, IBond.Order.SINGLE);
        mol.addBond(7, 8, IBond.Order.SINGLE);
        
        return mol;
    }
    
    public static IMolecule makeRhLikeStructure(int pCount, int ringCount) {
        IMolecule ttpr = builder.newInstance(IMolecule.class);
        ttpr.addAtom(builder.newInstance(IAtom.class, "Rh"));
        for (int i = 1; i <= pCount; i++) {
            ttpr.addAtom(builder.newInstance(IAtom.class, "P"));
            ttpr.addBond(0, i, IBond.Order.SINGLE);
        }
        
        for (int j = 1; j <= pCount; j++) {
            for (int k = 0; k < ringCount; k++) {
                AbstractSignatureTest.addRing(j, 6, ttpr);
            }
        }
        
        return ttpr;
    }
    
    public static IMolecule makeCycleWheel(int ringSize, int ringCount) {
        IMolecule mol = builder.newInstance(IMolecule.class);
        mol.addAtom(builder.newInstance(IAtom.class, "C"));
        for (int r = 0; r < ringCount; r++) {
            AbstractSignatureTest.addRing(0, ringSize, mol);
        }
        return mol;
    }
    
    public static IMolecule makeSandwich(int ringSize, boolean hasMethyl) {
        IMolecule mol = builder.newInstance(IMolecule.class);
        AbstractSignatureTest.addCarbons(mol, (ringSize * 2));
        mol.addAtom(builder.newInstance(IAtom.class, "Fe"));
        int center = ringSize * 2;
        // face A
        for (int i = 0; i < ringSize - 1; i++) {
            mol.addBond(i, i + 1, IBond.Order.SINGLE);
            mol.addBond(i, center, IBond.Order.SINGLE);
        }
        mol.addBond(ringSize - 1, 0, IBond.Order.SINGLE);
        mol.addBond(ringSize - 1, center, IBond.Order.SINGLE);
        
//        // face B
        for (int i = 0; i < ringSize - 1; i++) {
            mol.addBond(i + ringSize, i + ringSize + 1, IBond.Order.SINGLE);
            mol.addBond(i + ringSize, center, IBond.Order.SINGLE);
        }
        mol.addBond((2 * ringSize) - 1, ringSize, IBond.Order.SINGLE);
        mol.addBond((2 * ringSize) - 1, center, IBond.Order.SINGLE);
        
        if (hasMethyl) {
            mol.addAtom(builder.newInstance(IAtom.class, "C"));
            mol.addBond(0, mol.getAtomCount() - 1, IBond.Order.SINGLE);
        }
        
        return mol;
    }
    
    public static IMolecule makeC7H16A() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        AbstractSignatureTest.addCarbons(mol, 7);
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(3, 5, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        AbstractSignatureTest.addHydrogens(mol, 0, 3);
        AbstractSignatureTest.addHydrogens(mol, 1, 2);
        AbstractSignatureTest.addHydrogens(mol, 2, 2);
        AbstractSignatureTest.addHydrogens(mol, 3, 1);
        AbstractSignatureTest.addHydrogens(mol, 4, 3);
        AbstractSignatureTest.addHydrogens(mol, 5, 2);
        AbstractSignatureTest.addHydrogens(mol, 6, 3);
        return mol;
    }
    
    public static IMolecule makeC7H16B() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        AbstractSignatureTest.addCarbons(mol, 7);
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(2, 5, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        AbstractSignatureTest.addHydrogens(mol, 0, 3);
        AbstractSignatureTest.addHydrogens(mol, 1, 2);
        AbstractSignatureTest.addHydrogens(mol, 2, 1);
        AbstractSignatureTest.addHydrogens(mol, 3, 2);
        AbstractSignatureTest.addHydrogens(mol, 4, 3);
        AbstractSignatureTest.addHydrogens(mol, 5, 2);
        AbstractSignatureTest.addHydrogens(mol, 6, 3);
        return mol;
    }
    
    public static IMolecule makeC7H16C() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        AbstractSignatureTest.addCarbons(mol, 7);
        mol.addBond(0, 2, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        AbstractSignatureTest.addHydrogens(mol, 0, 3);
        AbstractSignatureTest.addHydrogens(mol, 1, 3);
        AbstractSignatureTest.addHydrogens(mol, 2, 1);
        AbstractSignatureTest.addHydrogens(mol, 3, 2);
        AbstractSignatureTest.addHydrogens(mol, 4, 2);
        AbstractSignatureTest.addHydrogens(mol, 5, 2);
        AbstractSignatureTest.addHydrogens(mol, 6, 3);
        return mol;
    }
    
    public static IMolecule makeDodecahedrane() {
        IMolecule dodec = builder.newInstance(IMolecule.class);
        for (int i = 0; i < 20; i++) {
            dodec.addAtom(builder.newInstance(IAtom.class, "C"));
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
        IMolecule cage = builder.newInstance(IMolecule.class);
        for (int i = 0; i < 16; i++) {
            cage.addAtom(builder.newInstance(IAtom.class, "C"));
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
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 8);
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
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 8);
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
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 4);
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
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 10);
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
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 6);
        
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        
        return mol;
    }
    
    public static IMolecule makeTwistane() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 10);
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 2, IBond.Order.SINGLE);
        mol.addBond(1, 3, IBond.Order.SINGLE);
        mol.addBond(1, 5, IBond.Order.SINGLE);
        mol.addBond(2, 4, IBond.Order.SINGLE);
        mol.addBond(2, 7, IBond.Order.SINGLE);
        mol.addBond(3, 8, IBond.Order.SINGLE);
        mol.addBond(3, 9, IBond.Order.SINGLE);
        mol.addBond(4, 6, IBond.Order.SINGLE);
        mol.addBond(4, 9, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(7, 8, IBond.Order.SINGLE);
        return mol;
    }

    public static IMolecule makeBenzene() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 6);
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
        IMolecule mol = builder.newInstance(IMolecule.class);
        addCarbons(mol, 5);
        
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
