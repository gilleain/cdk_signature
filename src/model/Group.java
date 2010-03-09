package model;

import java.util.ArrayList;

public class Group implements Comparable<Group> {
    
    public final int degree;
    
    public final ArrayList<Integer> memberIndices;
    
    public final Graph graph;
    
    public Group(Graph graph, int degree) {
        memberIndices = new ArrayList<Integer>();
        this.degree = degree;
        this.graph = graph;
    }

    /* 
     * Unusually, orders in what could be considered as reverse order, as a list
     * of groups will be sorted with the highest degree first.
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Group o) {
        if (degree > o.degree) {
            return -1;
        } else if (degree < o.degree) {
            return 1;
        } else {
            return 0;
        }
    }

}
