package engine;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;

public class EqClGen {
    
    public class Pair {
        
        public final int i; // the group index to attach
        
        public final int n; // the number of elements from that group to use

        public Pair(int i, int n) {
            this.i = i;
            this.n = n;
        }

        public String toString() {
            return "(" + i + ", " + n + ")";
        }
    }
    
    public class Group {
        
        public int valence;
        
        private List<Integer> atomIndices;
        
        public Group() {
            this.valence = 0;
            this.atomIndices = new ArrayList<Integer>();
        }
        
        public Group(Group o) {
            this.valence = o.valence;
            this.atomIndices = new ArrayList<Integer>();
            for (Integer i : o.atomIndices) {
                this.atomIndices.add(new Integer(i));
            }
        }
        
        public int size() {
            return this.atomIndices.size();
        }
    }
    
    public class Graph {
        
        private IChemObjectBuilder builder;
        
        private IAtomContainer atomContainer;
        
        private List<Group> groups;
        
        public Graph(List<String> elementSymbols, List<Integer> counts) {
            assert elementSymbols.size() == counts.size();
            
            builder = NoNotificationChemObjectBuilder.getInstance();
            atomContainer = builder.newAtomContainer();
            this.groups = new ArrayList<Group>();

            int atomIndex = 0;
            for (int i = 0; i < elementSymbols.size(); i++) {
                Group group = new Group();
                String elementSymbol = elementSymbols.get(i);
                int count = counts.get(i);
                for (int j = 0; j < count; j++) {
                    atomContainer.addAtom(builder.newAtom(elementSymbol));
                    group.atomIndices.add(atomIndex);
                    atomIndex++;
                }
                groups.add(group);
            }
        }
        
        public Graph(Graph o) {
            try {
                this.atomContainer = (IAtomContainer) o.atomContainer.clone();
                this.groups = new ArrayList<Group>();
                for (Group group : o.groups) {
                    this.groups.add(new Group(group));
                }
            } catch (CloneNotSupportedException cnse) {
                // TODO re-raise or catch this higher up
            }
        }
        
        public List<Graph> saturate() {
            List<Graph> results = new ArrayList<Graph>();
            for (int i = 0; i < this.groups.size(); i++) {
                this.saturate(i, results);
            }
            return results;
        }
        
        public boolean isComplete() {
            return true;
        }
        
        public void computeGroups() {
            
        }
        
        public void saturate(int groupID, List<Graph> results) {
            Group group = this.groups.get(groupID);
            if (group.size() == 0) {
                results.add(this);
            } else {
                int lmax = group.size();  // the maximum pair list length
                int bmax = group.valence; // the maximum valence
                List<List<Pair>> pairLists = backtrack(lmax, bmax);
                for (List<Pair> pairs : pairLists) {
                    Graph child = new Graph(this);
                    for (int i = 0; i < group.atomIndices.size(); i++) {
                        int atomIndex = group.atomIndices.get(i);
                        Pair p = pairs.get(i);
                        child.connect(atomIndex, p.i, p.n);
                    }
                    child.saturate(groupID, results);
                }
            }
        }
        
        public void saturate(int atomIndex,
                             int groupIndex,
                             List<Graph> partials, 
                             int lastClass, 
                             int lastNum) {
            
            if (isFullySaturated(atomIndex)) {
                partials.add(this);
            } else {
                for (int i = lastClass; i < groups.size(); i++) {
                    for (int j = lastNum; j < 100; j++) {
                        saturate(atomIndex, groupIndex, partials, i, j);
                    }
                }
            }
        }
        
        public boolean isFullySaturated(int atomIndex) {
            return true;
        }

        /**
         * Saturate an atom at <code>atomIndex</code> by adding
         * <code>number</code> elements of the group at
         * <code>otherGroupIndex</code>.
         * 
         * @param atomIndex
         * @param otherGroupIndex
         * @param number
         */
        public void connect(int atomIndex, int otherGroupIndex, int number) {
            IAtom atom = atomContainer.getAtom(atomIndex);
            Group otherGroup = groups.get(otherGroupIndex);
            for (int i = 0; i < number; i++) {
                int otherAtomIndex = otherGroup.atomIndices.get(i);
                IAtom otherAtom = atomContainer.getAtom(otherAtomIndex);
                IBond bond = atom.getBuilder().newBond(atom, otherAtom);
                atomContainer.addBond(bond);
            }
        }
        
        public List<List<Pair>> backtrack(int lmax, int bmax) {
            List<List<Pair>> results = new ArrayList<List<Pair>>();
            backtrack(new ArrayList<Pair>(), results, 0, lmax, bmax);
            return results;
        }
    
        public void backtrack(List<Pair> pairs, 
                              List<List<Pair>> results,
                              int l, int lmax, int bmax) {
            if (l == lmax) {
                results.add(pairs);
            } else {
                Pair last = getLast(pairs);
                for (int i = last.i; i < this.groups.size(); i++) {
                    for (int j = last.n; j <= bmax; j++) {
                        List<Pair> copy = new ArrayList<Pair>();
                        copy.addAll(pairs);
                        copy.add(new Pair(i, j));
                        backtrack(copy, results, l + 1, lmax, bmax);
                    }
                }
            }
        }
        
        public Pair getLast(List<Pair> pairs) {
            if (pairs.size() == 0) {
                // the first group (group index 0) and a valence of 1
                return new Pair(0, 1);
            } else {
                return pairs.get(pairs.size() - 1);
            }
        }

    }
    
    public List<Graph> generate(Graph g) {
        return g.saturate();
    }
    
    public void saturate(Graph graph, List<Graph> graphs) {
        if (graph.isComplete()) {
            graphs.add(graph);
        } else {
            graph.computeGroups();
            List<Graph> results = new ArrayList<Graph>();
            graph.saturate(0, results);
            for (Graph h : results) {
                saturate(h, graphs);
            }
        }
    }
    
    public List<Graph> generate(List<String> elements, List<Integer> counts) {
        return generate(new Graph(elements, counts));
    }
    
    public static void main(String[] args) {
        List<String> elements = new ArrayList<String>() {{ add("C"); add("H"); }};
        List<Integer> counts = new ArrayList<Integer>() {{ add(4); add(8); }};
        EqClGen ecg = new EqClGen();
        List<Graph> results = ecg.generate(elements, counts);
        SmilesGenerator smilesGenerator = new SmilesGenerator();
        for (Graph g : results) {
            if (ConnectivityChecker.isConnected(g.atomContainer)) {
                System.out.println(
                    smilesGenerator.createSMILES((IMolecule) g.atomContainer));
            } else {
                System.out.println("not connected");
            }
        }
    }

}
