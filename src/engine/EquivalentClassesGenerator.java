package engine;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

public class EquivalentClassesGenerator {
    
    private SaturationCalculator saturationCalculator = 
        new SaturationCalculator();
    
    public class AttachmentPoint {
        
        private IAtomContainer fragment;
        
        private int atomIndex;
        
        private Group group;
        
        public AttachmentPoint(IAtomContainer fragment, IAtom atom, Group group) {
            this(fragment, fragment.getAtomNumber(atom), group);
        }
        
        public AttachmentPoint(IAtomContainer fragment, int atomIndex, Group group) {
            this.fragment = fragment;
            this.atomIndex = atomIndex;
            this.group = group;
        }
        
        public List<IAtom> getConnected() {
            return fragment.getConnectedAtomsList(fragment.getAtom(atomIndex));
        }
        
        public boolean isSaturated() {
            return saturationCalculator.calculateUnsaturation(
                    fragment.getAtom(atomIndex), fragment) == 0;
        }
        
        public Group getGroup() {
            return this.group;
        }

        public IAtomContainer getFragment() {
            return this.getFragment();
        }
        
        public AttachmentPoint copy(Group group) {
            try {
                return new AttachmentPoint(
                        (IAtomContainer)this.fragment.clone(), atomIndex, group);
            } catch (CloneNotSupportedException c) {
                return null;
            }
        }
        
        public void toString(StringBuffer stringBuffer) {
            for (int i = 0; i < fragment.getAtomCount(); i++) {
                IAtom atom = fragment.getAtom(i);
                stringBuffer.append(atom.getSymbol());
                if (i == atomIndex) {
                    stringBuffer.append("*");
                }
            }
        }
        
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            this.toString(stringBuffer);
            return stringBuffer.toString();
        }
        
    }
    
    public class Group {
        
        private int unsaturation;
        
        private List<AttachmentPoint> attachmentPoints;
        
        public Group() {
            this.attachmentPoints = new ArrayList<AttachmentPoint>();
        }
        
        public Group(IAtom atom, IAtomContainer fragment, int unsaturation) {
            this();
            this.unsaturation = unsaturation;
            this.addAttachmentPoint(fragment, atom);
        }
        
        public Group copy(int indexOfAttachmentPointToIgnore) {
            Group group = new Group();
            int i = 0;
            for (AttachmentPoint attachment : this.attachmentPoints) {
                if (i != indexOfAttachmentPointToIgnore) {
                    group.attachmentPoints.add(attachment.copy(group));
                }
                i++;
            }
            group.unsaturation = this.unsaturation;
            return group;
        }
        
        public int getCount() {
            return this.attachmentPoints.size();
        }
        
        public void addAttachmentPoint(IAtomContainer fragment, IAtom atom) {
            this.attachmentPoints.add(new AttachmentPoint(fragment, atom, this));
        }
        
        public boolean in(IAtom atom, IAtomContainer fragment, int unsaturation) {
            if (unsaturation == this.unsaturation) {
                List<IAtom> connected = fragment.getConnectedAtomsList(atom);
                List<IAtom> other = attachmentPoints.get(0).getConnected();
                for (IAtom c : connected) {
                    if (find(c, other)) {
                        continue;
                    } else {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
        
        private boolean find(IAtom c, List<IAtom> other) {
            for (IAtom o : other) {
                if (c.getSymbol().equals(o.getSymbol())) {
                    other.remove(o);
                    return true;
                }
            }
            return false;
        }
        
        public AttachmentPoint removeElement() {
            return this.attachmentPoints.remove(0);
        }
        
        /**
         * Get all attachment points in this group that have more 'elements'
         * (atoms?) connected to them than in the attachment point <code>last
         * </code> that is passed in.
         * 
         * @param last the reference attachment point
         * @return a list of attachment points that are greater in size
         */
        public List<AttachmentPoint> getAttachments(AttachmentPoint last) {
            if (last == null) {
                return this.attachmentPoints;
            }
            
            int minimumSize = last.getFragment().getAtomCount();
            List<AttachmentPoint> attachmentPointsToReturn = 
                new ArrayList<AttachmentPoint>();
            for (AttachmentPoint other : this.attachmentPoints) {
                if (other.getFragment().getAtomCount() > minimumSize) {
                    attachmentPointsToReturn.add(other);
                }
            }
            return attachmentPointsToReturn;
        }
        
        public int indexOf(AttachmentPoint attachmentPoint) {
            return this.attachmentPoints.indexOf(attachmentPoint);
        }
        
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            this.toString(stringBuffer);
            return stringBuffer.toString();
        }
        
        public void toString(StringBuffer stringBuffer) {
            int l = attachmentPoints.size();
            stringBuffer.append("[");
            if (l == 0) {
                stringBuffer.append("]");
            } else if (l == 1) {
                attachmentPoints.get(0).toString(stringBuffer);
                stringBuffer.append("]");
            } else {
                for (int i = 0; i < l - 1; i++) {
                    attachmentPoints.get(i).toString(stringBuffer);
                    stringBuffer.append(",");
                }
                attachmentPoints.get(l - 1).toString(stringBuffer);
                stringBuffer.append("]");
            }
        }
        
    }
    
    public class Graph {
        
        private List<Group> groups;
        
        public AttachmentPoint elementBeingSaturated;
        
        public Graph() {
            this.groups = new ArrayList<Group>();
        }
        
        public Graph(List<IAtomContainer> fragments) {
            this();
            for (IAtomContainer fragment : fragments) {
                this.addFragment(fragment);
            }
        }
        
        /**
         * A saturated graph is a single group with no attachment points
         * 
         * @return
         */
        public boolean isSaturated() {
            return groups.size() == 1 && groups.get(0).getCount() == 0;
        }
        
        public void addFragment(IAtomContainer fragment) {
            for (IAtom atom : fragment.atoms()) {
                int unsaturation = 
                    saturationCalculator.calculateUnsaturation(atom, fragment);
                if (unsaturation == 0) {
                    continue;
                } else {
                    // determine which class to put it in, based on saturation
                    addToGroup(atom, fragment, unsaturation);
                }
            }
            
        }
        
        public void addToGroup(
                IAtom atom, IAtomContainer fragment, int unsaturation) {
            for (Group group : groups) {
                if (group.in(atom, fragment, unsaturation)) {
                    group.addAttachmentPoint(fragment, atom);
                    return;
                }
            }
            groups.add(new Group(atom, fragment, unsaturation));
        }
        
        public void computeEquivalenceClasses() {
            
        }
        
        public List<Graph> saturate() {
            List<Graph> children = new ArrayList<Graph>();
            this.saturate(this.groups.get(0), this, children);
            return children;
        }
        
        /**
         * Saturate each fragment in a group until the group is empty
         * 
         * @param group
         * @param g
         * @param solutions
         */
        private void saturate(Group group, Graph g, List<Graph> solutions) {
            if (group.getCount() == 0) {
                this.addToSolutions(g, solutions);
            } else {
                
                // for an element (attachment point) of the class (group)
                // make all the graphs possible by saturating this element
                AttachmentPoint element = group.removeElement();
                List<Graph> intermediateGraphs = new ArrayList<Graph>();
                this.saturateElement(element, g, intermediateGraphs, null);
                
                for (Graph h : intermediateGraphs) {
                    this.saturate(group, h, solutions);
                }
            }
        }
        
        /**
         * Saturate a fragment all possible ways
         * 
         * @param element the element to saturate
         * @param graph the current graph
         * @param results the list of graphs to build up
         * @param last the last attachment point used 
         */
        private void saturateElement(AttachmentPoint element, 
                                     Graph graph, 
                                     List<Graph> results,
                                     AttachmentPoint last) {
            System.out.println("saturating " + element + " in " + graph);
            if (element.isSaturated()) {
                System.out.println("saturated");
                addToSolutions(graph, results);
            } else {
                // use only groups with an index greater than the last used
                List<Group> groupsToUse;
                List<Group> groups = graph.getGroups();
                if (last == null) {
                    groupsToUse = groups;
                } else {
                    int i = groups.indexOf(last.getGroup());
                    groupsToUse = groups.subList(i, groups.size());
                }
                
                // again, use only attachment points that are 'greater' than
                // the last one used, in terms of number of attached atoms
                for (Group group : groupsToUse) {
                    for (AttachmentPoint other : group.getAttachments(last)) {
                        Graph child = graph.connect(element, other);
                        saturateElement(
                            child.elementBeingSaturated, child, results, other);
                    }
                }
                
            }
        }
        
        /**
         * A key method that takes two attachment points, makes a bond between
         * the atoms that they refer to, but in a new graph so that the graph
         * instance making the new bond does not contain the bond.
         * 
         * @param a the attachment point to be saturated 
         * @param b the attachment point to use to saturate a
         * @return a new graph with a bond between a and b
         */
        private Graph connect(AttachmentPoint a, AttachmentPoint b) {
            Graph graph = new Graph();
            
            // get the indices for a, b, and their groups
            int indexOfGroupOfA = groups.indexOf(a.getGroup());
            int indexOfGroupOfB = groups.indexOf(b.getGroup());
            int indexOfB = groups.get(indexOfGroupOfB).indexOf(b);
            
            // copy all the groups and their attachment points, ignoring only
            // b which is being removed from its group, and attached to a
            for (Group group : this.groups) {
                graph.groups.add(group.copy(indexOfB));
            }
            
            // copy across all the atoms and bonds from b to a
            Group groupACopy = graph.groups.get(indexOfGroupOfA);
            AttachmentPoint attachmentPointACopy = a.copy(groupACopy);
            
            int numberOfAtomsInA = a.fragment.getAtomCount();
            try {
                IAtomContainer bClone = (IAtomContainer) b.fragment.clone();
                for (IAtom atom : bClone.atoms()) {
                    attachmentPointACopy.fragment.addAtom(atom);
                }
                for (IBond bond : bClone.bonds()) {
                    attachmentPointACopy.fragment.addBond(bond);
                }
            } catch (CloneNotSupportedException c) {
                
            }
            
            // finally, add the new bond
            int newAtomIndex = numberOfAtomsInA + b.atomIndex;
            IAtom x = attachmentPointACopy.fragment.getAtom(a.atomIndex);
            IAtom y = attachmentPointACopy.fragment.getAtom(newAtomIndex);
            IBond.Order o = IBond.Order.SINGLE;
            IBond bond = a.fragment.getBuilder().newBond(x, y, o);
            attachmentPointACopy.fragment.addBond(bond);
            
            graph.elementBeingSaturated = attachmentPointACopy;
            
            return graph;
        }
        
        public List<Group> getGroups() {
            return this.groups;
        }

        private void addToSolutions(Graph graph, List<Graph> solutions) {
            if (graph.cni()) {
                solutions.add(graph);
            } else {
                // isomorphism check
                for (Graph solution : solutions) {
                    if (isomorphic(graph, solution)) {
                        return;
                    }
                }
                solutions.add(graph);
            }
            
        }
        
        private boolean isomorphic(Graph a, Graph b) {
            return true;    // TODO
        }
        
        private boolean cni() {
            return true;    // TODO
        }
        
        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            for (Group group : this.groups) {
                stringBuffer.append(group.toString());
            }
            stringBuffer.append("]");
            return stringBuffer.toString();
        }
    }
    
    public List<IAtomContainer> generate(List<IAtomContainer> fragments) {
        List<IAtomContainer> results = new ArrayList<IAtomContainer>();
        Graph initialGraph = new Graph(fragments);
        this.saturate(initialGraph, new ArrayList<Graph>());
        return results;
    }
    
    private void saturate(Graph graph, List<Graph> solutions) {
        if (isValid(graph)) {
            solutions.add(graph);
        } else {
            graph.computeEquivalenceClasses();
            for (Graph child : graph.saturate()) {
                saturate(child, solutions);
            }
        }
    }
    
    /**
     * Check that the graph is connected, and saturated
     * 
     * @param graph the graph to check
     * @return true if it both connected and saturated
     */
    private boolean isValid(Graph graph) {
        System.out.println(graph);
        return graph.isSaturated();
    }
}
