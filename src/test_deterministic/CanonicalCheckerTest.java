package test_deterministic;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.graph.AtomContainerAtomPermutor;
import org.openscience.cdk.group.Permutation;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.structgen.deterministic.AlternateCanonicalChecker;
import org.openscience.cdk.structgen.deterministic.CanonicalChecker;
import org.openscience.cdk.structgen.deterministic.Graph;
import org.openscience.cdk.tools.manipulator.BondManipulator;

public class CanonicalCheckerTest {
    
    private IChemObjectBuilder builder = 
        NoNotificationChemObjectBuilder.getInstance();
    
    public boolean canonical(IAtomContainer ac) {
//        return CanonicalChecker.isCanonical(ac);
//        return AlternateCanonicalChecker.isCanonicalByVisitor(ac);
//        return CanonicalChecker.isCanonicalWithSignaturePartition(ac);
//        return CanonicalChecker.isCanonicalByMagic(ac);
        return CanonicalChecker.isCanonicalByProperMagic(ac);
    }
    
    @Test
    public void butyl_1_ene_3_yne() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"H"));
        }
        ac.addBond(0, 1, IBond.Order.TRIPLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(1, 4, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.DOUBLE);
        ac.addBond(2, 5, IBond.Order.SINGLE);
        ac.addBond(3, 6, IBond.Order.SINGLE);
        ac.addBond(3, 7, IBond.Order.SINGLE);
        
        canonical(ac);
//        boolean canon = CanonicalChecker.isCanonicalByCombinedVertexSymbol(ac);
//        System.out.println(canon);
    }
    
    public void checkSigOrder(IAtomContainer ac) {
        if (CanonicalChecker.signaturesOrdered(ac)) {
            System.out.println("ORD " + new Graph(ac));
        } else {
            System.out.println("DIS " + new Graph(ac));
        }
    }
    
    @Test
    public void H4C3Test() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"H"));
        }
        for (int i = 0; i < 3; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        
        ac.addBond(0, 5, IBond.Order.SINGLE);
        checkSigOrder(ac);
        
        ac.addBond(1, 6, IBond.Order.SINGLE);
        checkSigOrder(ac);
        
        ac.addBond(2, 4, IBond.Order.SINGLE);
        checkSigOrder(ac);
        
        ac.addBond(3, 4, IBond.Order.SINGLE);
        checkSigOrder(ac);
        
        ac.addBond(5, 6, IBond.Order.SINGLE);
        checkSigOrder(ac);
    }
    
    public IAtomContainer makeDisconnectedSeparateBonds() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        return ac;
    }
    
    public IAtomContainer makeDisconnectedNestedBonds() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addBond(0, 3, IBond.Order.SINGLE);
        ac.addBond(1, 2, IBond.Order.SINGLE);
        return ac;
    }
    
    public IAtomContainer makeDisconnectedOverlappingBonds() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(1, 3, IBond.Order.SINGLE);
        return ac;
    }
    
    @Test
    public void getSignatureListDisconnectedOverlappingTest() {
        IAtomContainer ac = makeDisconnectedOverlappingBonds();
        Assert.assertEquals(2, 
                AlternateCanonicalChecker.getSignatureList(ac).size());
    }
    
    @Test
    public void getSignatureListDisconnectedSeparateTest() {
        IAtomContainer ac = makeDisconnectedSeparateBonds();
        Assert.assertEquals(2, 
                AlternateCanonicalChecker.getSignatureList(ac).size());
    }
    
    @Test
    public void getSignatureListDisconnectedNestedTest() {
        IAtomContainer ac = makeDisconnectedNestedBonds();
        Assert.assertEquals(2, 
                AlternateCanonicalChecker.getSignatureList(ac).size());
    }
    
    @Test
    public void trivialTest() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        Assert.assertEquals(true, canonical(ac));
    }
    
    @Test
    public void disconnectedSeparateBondsTest() {
        IAtomContainer ac = makeDisconnectedSeparateBonds();
        Assert.assertEquals(true, canonical(ac));
    }
    
    @Test
    public void disconnectedNestedBondsTest() {
        IAtomContainer ac = makeDisconnectedNestedBonds();
        Assert.assertEquals(true, canonical(ac));
    }

    @Test
    public void disconnectedOverlappingBondsTest() {
        IAtomContainer ac = makeDisconnectedOverlappingBonds();
        Assert.assertEquals(true, canonical(ac));
    }
    
    @Test
    public void testFourCycle() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"C"));
        ac.addAtom(builder.newInstance(IAtom.class,"O"));
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(1, 3, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        Assert.assertEquals(true, canonical(ac));
    }
    
    @Test
    public void cycloPropeneMethyl1ene() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"H"));
        }
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(0, 3, IBond.Order.DOUBLE);
        ac.addBond(1, 4, IBond.Order.SINGLE);
        ac.addBond(2, 5, IBond.Order.SINGLE);
        
        ac.addBond(1, 2, IBond.Order.DOUBLE);
//        ac.addBond(1, 2, IBond.Order.SINGLE);
        
        ac.addBond(3, 6, IBond.Order.SINGLE);
        ac.addBond(3, 7, IBond.Order.SINGLE);
//        findCanonical(ac);
        chompTest(ac);
//        Assert.assertEquals(true, 
//                CanonicalChecker.isCanonicalWithColorPartition(ac));
//                canonical(ac));
    }
    
    @Test
    public void ccnnCycle() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 2; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 2; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"N"));
        }
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(1, 3, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        findCanonical(ac);
    }
    
    @Test
    public void cncnCycle() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 2; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 2; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"N"));
        }
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(0, 3, IBond.Order.SINGLE);
        ac.addBond(1, 2, IBond.Order.SINGLE);
        ac.addBond(1, 3, IBond.Order.SINGLE);
        findCanonical(ac);
    }
    
    @Test
    public void cccNNNPrism() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 3; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 3; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"N"));
        }
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(0, 3, IBond.Order.SINGLE);
        ac.addBond(1, 2, IBond.Order.SINGLE);
        ac.addBond(1, 4, IBond.Order.SINGLE);
        ac.addBond(2, 5, IBond.Order.SINGLE);
        ac.addBond(3, 4, IBond.Order.SINGLE);
        ac.addBond(3, 5, IBond.Order.SINGLE);
        ac.addBond(4, 5, IBond.Order.SINGLE);
//        findCanonical(ac);
        Assert.assertTrue(canonical(ac));
    }
    
    @Test
    public void ccnNNCPrism() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 3; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 3; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"N"));
        }
        ac.addBond(0, 1, IBond.Order.SINGLE);
        ac.addBond(0, 3, IBond.Order.SINGLE);
        ac.addBond(0, 5, IBond.Order.SINGLE);
        ac.addBond(1, 3, IBond.Order.SINGLE);
        ac.addBond(1, 4, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.SINGLE);
        ac.addBond(2, 4, IBond.Order.SINGLE);
        ac.addBond(2, 5, IBond.Order.SINGLE);
        ac.addBond(4, 5, IBond.Order.SINGLE);
        findCanonical(ac);
//        Assert.assertTrue(canonical(ac));
    }
    
    @Test
    public void methylCyclopropaneTest() {
        IAtomContainer acA = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) {
            acA.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 8; i++) {
            acA.addAtom(builder.newInstance(IAtom.class,"H"));
        }
        multiBond(acA, 0, 1, 2, 3, 4);
        multiBond(acA, 1, 2, 5, 6);
        multiBond(acA, 2, 7, 8);
        multiBond(acA, 3, 9, 10, 11);
        boolean canonA = totallyCanonical(acA);
        System.out.println(canonA);
        
        IAtomContainer acB = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) {
            acB.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 8; i++) {
            acB.addAtom(builder.newInstance(IAtom.class,"H"));
        }
        multiBond(acB, 0, 1, 2, 3, 4);
        multiBond(acB, 1, 5, 6, 7);
        multiBond(acB, 2, 3, 8, 9);
        multiBond(acB, 3, 10, 11);
        boolean canonB = totallyCanonical(acB);
        System.out.println(canonB);
    }
    
    @Test
    public void dimethylCyclobutane() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 6; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 12; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"H"));
        }
        multiBond(ac, 0, 2, 3, 4, 6);
        multiBond(ac, 1, 2, 3, 5, 7);
        multiBond(ac, 2, 8, 9);
        multiBond(ac, 3, 10, 11);
        multiBond(ac, 4, 12, 13, 14);
        multiBond(ac, 5, 15, 16, 17);
//        totallyCanonical(ac);
        chompTest(ac);
    }
    
    @Test
    public void but_1_3_dieneTest() {
        IAtomContainer ac = builder.newInstance(IAtomContainer.class);
        for (int i = 0; i < 4; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"C"));
        }
        for (int i = 0; i < 8; i++) {
            ac.addAtom(builder.newInstance(IAtom.class,"H"));
        }
        ac.addBond(0, 1, IBond.Order.DOUBLE);
        ac.addBond(0, 2, IBond.Order.SINGLE);
        ac.addBond(0, 4, IBond.Order.SINGLE);
        ac.addBond(1, 6, IBond.Order.SINGLE);
        ac.addBond(1, 7, IBond.Order.SINGLE);
        ac.addBond(2, 3, IBond.Order.DOUBLE);
        ac.addBond(3, 8, IBond.Order.SINGLE);
        ac.addBond(3, 9, IBond.Order.SINGLE);
        boolean canonB = 
            CanonicalChecker.isCanonicalWithCompactSignaturePartition(ac);
        System.out.println(canonB +
                " " + CanonicalChecker.degreeOrdered(ac) +
                " " + CanonicalChecker.edgesInOrder(ac));
        chompTest(ac);
    }
    
    public boolean totallyCanonical(IAtomContainer ac) {
        boolean s = CanonicalChecker.isCanonicalWithCompactSignaturePartition(ac);
//        boolean s = CanonicalChecker.isCanonicalWithColorPartition(ac);
        boolean d = CanonicalChecker.degreeOrdered(ac);
        boolean e = CanonicalChecker.edgesInOrder(ac);
        if (!s) { System.out.println("NOT SIG-PARTIION CANON " + new Graph(ac)); }
        if (!d) { System.out.println("NOT DEGR-ORDERED CANON " + new Graph(ac)); }
        if (!e) { System.out.println("NOT EDGE-ORDERED CANON " + new Graph(ac)); }
        return s && d && e;
            
    }
    
    public void chompTest(IAtomContainer ac) {
        int c = ac.getBondCount();
        while (c >= 1) {
            removeFinalBond(ac);
//            if (totallyCanonical(ac)) {
            if (CanonicalChecker.isCanonicalByCombinedVertexSymbol(ac)) {
                System.out.println("chomped " + c + " now " + ac.getBondCount());
            } else {
                System.out.println("failed at bond " + c);
            }
            c = ac.getBondCount();
        }
    }
    
    public void removeFinalBond(IAtomContainer ac) {
        List<IBond> bonds = new ArrayList<IBond>();
        int i = 0;
        for (IBond bond : ac.bonds()) {
            if (bond == null) {
                continue;
            } else if (i == ac.getBondCount() - 1) {
                if (bond.getOrder() == IBond.Order.SINGLE) {
                    continue;
                } else {
                    BondManipulator.decreaseBondOrder(bond);
                }
            }
            bonds.add(bond);
            i++;
        }
//        System.out.println(Arrays.toString(bonds));
        IBond[] bondArr = new IBond[bonds.size()];
        for (int j = 0; j < bonds.size(); j++) {
            bondArr[j] = bonds.get(j);
        }
        ac.setBonds(bondArr);
    }
    
    public void multiBond(IAtomContainer ac, int index, int... partners) {
        for (int partner : partners) {
            ac.addBond(index, partner, IBond.Order.SINGLE);
        }
    }
    
    public void findCanonical(IAtomContainer container) {
        AtomContainerAtomPermutor permutor = 
            new AtomContainerAtomPermutor(container);
        while (permutor.hasNext()) {
            IAtomContainer permutation = permutor.next(); 
            if (canonical(permutation)) {
                int n = container.getAtomCount();
                String edgestring = 
                    CanonicalChecker.edgeString(permutation, new Permutation(n));
                System.out.println("YES " + edgestring);
                return;
            }
        }
        System.out.println("NO");
    }

}
