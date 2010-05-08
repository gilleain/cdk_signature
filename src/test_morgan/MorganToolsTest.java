package test_morgan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class MorganToolsTest {
    
    public static IChemObjectBuilder builder =
        NoNotificationChemObjectBuilder.getInstance();

    public long[] getMorganNumbers(IAtomContainer atomContainer) {
        int N = atomContainer.getAtomCount();
        long[] current = new long[N];
        long[] previous = new long[N];
        for (int f = 0; f < N; f++) {
            int connectedCount = atomContainer.getConnectedBondsCount(f); 
            current[f] = connectedCount;
            previous[f] = connectedCount;
        }
        for (int e = 0; e < N; e++) {
            for (int f = 0; f < N; f++) {
                current[f] = 0;
                IAtom a = atomContainer.getAtom(f);
                for (IAtom connected : atomContainer.getConnectedAtomsList(a)) {
                    int c = atomContainer.getAtomNumber(connected);
                    current[f] += previous[c];
                }
            }
            System.out.println(Arrays.toString(current) + "\t" + makePartition(current));
            System.arraycopy(current, 0, previous, 0, N);
        }
        return previous;
    }
    
    public Partition makePartition(long[] morganNumbers) {
        Map<Long, Integer> morganToCellIndexMap = new HashMap<Long, Integer>();
        Map<Integer, List<Integer>> cells = 
            new HashMap<Integer, List<Integer>>();
        int currentMaxIndex = 0;
        for (int i = 0; i < morganNumbers.length; i++) {
            long morganNumber = morganNumbers[i];
            if (morganToCellIndexMap.containsKey(morganNumber)) {
                int index = morganToCellIndexMap.get(morganNumber);
                cells.get(index).add(i);
            } else {
                morganToCellIndexMap.put(morganNumber, currentMaxIndex);
                List<Integer> cell = new ArrayList<Integer>();
                cell.add(i);
                cells.put(currentMaxIndex, cell);
                currentMaxIndex++;
            }
        }
        
        Partition partition = new Partition();
        List<Long> sortedMorganNumbers = new ArrayList<Long>();
        sortedMorganNumbers.addAll(morganToCellIndexMap.keySet());
        Collections.sort(sortedMorganNumbers);
        for (Long morganNumber : sortedMorganNumbers) {
            Integer index = morganToCellIndexMap.get(morganNumber);
            partition.addCell(cells.get(index));
        }
        return partition;
    }
    
    @Test
    public void methylCycloButaneTest() {
        IMolecule molecule = makeMethylCycloButane();
        getMorganNumbers(molecule);
    }
    
    @Test
    public void bridgedMethylCycloPentaneTest() {
        IMolecule molecule = makeBridgedMethylCycloPentane();
        getMorganNumbers(molecule);
    }
    
    @Test
    public void cuneaneTest() {
        IMolecule molecule = makeCuneane();
        getMorganNumbers(molecule);
    }
    
    @Test
    public void threeFourSixSevenRingTest() {
        IMolecule molecule = makeThreeFourSixSevenRing();
        getMorganNumbers(molecule);
    }

    public IMolecule makeMethylCycloButane() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        for (int i = 0; i < 5; i++) {
            mol.addAtom(builder.newInstance(IAtom.class, "C"));
        }
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(1, 4, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(3, 4, IBond.Order.SINGLE);
        return mol;
    }
    
    public IMolecule makeBridgedMethylCycloPentane() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        for (int i = 0; i < 7; i++) {
            mol.addAtom(builder.newInstance(IAtom.class, "C"));
        }
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(1, 2, IBond.Order.SINGLE);
        mol.addBond(1, 3, IBond.Order.SINGLE);
        mol.addBond(2, 4, IBond.Order.SINGLE);
        mol.addBond(3, 5, IBond.Order.SINGLE);
        mol.addBond(3, 6, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(4, 6, IBond.Order.SINGLE);
        return mol;
    }

    public IMolecule makeCuneane() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        for (int i = 0; i < 8; i++) {
            mol.addAtom(builder.newInstance(IAtom.class, "C"));
        }
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
    
    public IMolecule makeThreeFourSixSevenRing() {
        IMolecule mol = builder.newInstance(IMolecule.class);
        for (int i = 0; i < 12; i++) {
            mol.addAtom(builder.newInstance(IAtom.class, "C"));
        }
        mol.addBond(0, 1, IBond.Order.SINGLE);
        mol.addBond(0, 2, IBond.Order.SINGLE);
        mol.addBond(0, 4, IBond.Order.SINGLE);
        mol.addBond(1, 3, IBond.Order.SINGLE);
        mol.addBond(2, 3, IBond.Order.SINGLE);
        mol.addBond(3, 6, IBond.Order.SINGLE);
        mol.addBond(4, 5, IBond.Order.SINGLE);
        mol.addBond(4, 7, IBond.Order.SINGLE);
        mol.addBond(5, 6, IBond.Order.SINGLE);
        mol.addBond(6, 8, IBond.Order.SINGLE);
        mol.addBond(7, 9, IBond.Order.SINGLE);
        mol.addBond(8, 10, IBond.Order.SINGLE);
        mol.addBond(9, 10, IBond.Order.SINGLE);
        mol.addBond(9, 11, IBond.Order.SINGLE);
        mol.addBond(10, 11, IBond.Order.SINGLE);
        return mol;
    }

}
