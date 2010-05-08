package test_group;

import org.junit.Test;
import org.openscience.cdk.group.CDKDiscretePartitionRefiner;
import org.openscience.cdk.group.CDKEquitablePartitionRefiner;
import org.openscience.cdk.group.Partition;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;

public class CDKEquitablePartitionRefinerTest {
    
    private IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
    
    @Test
    public void testInvariants() {
       IAtomContainer ac = builder.newInstance(IAtomContainer.class);
       ac.addAtom(builder.newInstance(IAtom.class, "C"));
       ac.addAtom(builder.newInstance(IAtom.class, "C"));
       ac.addAtom(builder.newInstance(IAtom.class, "C"));
       ac.addBond(0, 1, IBond.Order.DOUBLE);
       ac.addBond(0, 2, IBond.Order.SINGLE);
       
       CDKDiscretePartitionRefiner discreteRefiner = new CDKDiscretePartitionRefiner();
       CDKEquitablePartitionRefiner equitableRefiner 
           = new CDKEquitablePartitionRefiner(discreteRefiner.makeConnectionTable(ac));
       Partition finer = equitableRefiner.refine(Partition.unit(3));
       System.out.println(finer);
    }

}
