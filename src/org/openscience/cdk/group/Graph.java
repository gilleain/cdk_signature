package org.openscience.cdk.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple graph for testing.
 * 
 * @author maclean
 *
 */
public class Graph {
    
    public class Edge {
        
        public int a;
        
        public int b;
        
        public int o = 1;
        
        public Edge(int a, int b) {
            this.a = a;
            this.b = b;
        }
        
        public Edge(int a, int b, int o) {
            this(a, b);
            this.o = o;
        }
        
        public Edge(Edge e) {
            this.a = e.a;
            this.b = e.b;
        }
        
        public String getSortedPermutedColorOnlyString(int[] p, List<Integer> colors) {
            int pa = p[this.a];
            int pb = p[this.b];
            int ca = colors.get(pa);
            int cb = colors.get(pb);
            if (ca < cb) {
                return ca + ":" + cb;
            } else {
                return cb + ":" + ca;
            }
        }
        
        public String getSortedPermutedColoredString(int[] p, List<Integer> colors) {
            int pa = p[this.a];
            int pb = p[this.b];
            if (pa < pb) {
//                return pa + "(" + colors.get(pa) + "):" + pb + "(" + colors.get(pb) + ")";
                return pa + "(" + colors.get(a) + "):" + pb + "(" + colors.get(b) + ")";
            } else {
//                return pb + "(" + colors.get(pb) + "):" + pa + "(" + colors.get(pa) + ")";
                return pb + "(" + colors.get(b) + "):" + pa + "(" + colors.get(a) + ")";
            }
        }
        
        public String getSortedPermutedString(int[] p) {
            int pa = p[this.a];
            int pb = p[this.b];
            if (pa < pb) {
                return pa + ":" + pb;
            } else {
                return pb + ":" + pa;
            }
        }
        
        public String getPermutedString(int[] p) {
            return p[this.a] + ":" + p[this.b];
        }
        
        public String toSortedColorOnlyString(List<Integer> colors) {
            int ca = colors.get(a);
            int cb = colors.get(b);
            if (ca < cb) {
                return ca + ":" + cb;
            } else {
                return cb + ":" + ca;
            }
            
        }
        
        public String toSortedColoredString(List<Integer> colors) {
            if (a < b) {
                return a + "(" + colors.get(a) + "):" + b + "(" + colors.get(b) + ")";
            } else {
                return b + "(" + colors.get(b) + "):" + a + "(" + colors.get(a) + ")";
            }
        }
        
        public String toSortedString() {
            if (a < b) {
                return a + ":" + b;
            } else {
                return b + ":" + a;
            }
        }
        
        public String toSortedStringWithEdgeOrder() {
            if (a < b) {
                return a + ":" + b + "(" + o + ")";
            } else {
                return b + ":" + a + "(" + o + ")";
            }
        }

        public String toString() {
            return this.a + ":" + this.b;
        }
    }
    
    public List<Edge> edges;
    
    public int maxVertexIndex;
    
    public List<Integer> colors;
    
    public Graph() {
        this.edges = new ArrayList<Edge>();
        this.colors = new ArrayList<Integer>();
    }
    
    public Graph(String graphString) {
        this();
        if (graphString.startsWith("[")) {
            graphString = graphString.substring(1, graphString.length() - 1);
        }
        for (String edgeString : graphString.split(",")) {
            String[] vertexStrings = edgeString.split(":");
            int a = Integer.parseInt(vertexStrings[0].trim());
            int b = Integer.parseInt(vertexStrings[1].trim());
            this.makeEdge(a, b);
        }
    }
    
    public Graph(Graph other) {
        this();
        for (Edge edge : other.edges) {
            this.makeEdge(edge.a, edge.b, edge.o);
        }
    }
    
    public Graph getPermutedGraph(int[] permutation) {
        Graph graph = new Graph();
        for (Edge e : this.edges) {
            graph.makeEdge(permutation[e.a], permutation[e.b]);
        }
        return graph;
    }
    
    public int getColor(int index) {
        if (index >= colors.size()) {
            return 0;
        } else { 
            return colors.get(index);
        }
    }
    
    public void setColors(int... colors) {
        for (int color : colors) {
            this.colors.add(color);
        }
    }
    
    public List<Integer> getPermutedColorString(Permutation p) {
        List<Integer> permutedColors = new ArrayList<Integer>();
        for (int i = 0; i < this.colors.size(); i++) {
            permutedColors.add(this.colors.get(p.get(i)));
        }
        return permutedColors;
    }
    
    public List<String> getLabels() {
        return null;
    }
    
    private void updateMaxVertexIndex(int a, int b) {
        if (a > maxVertexIndex) maxVertexIndex = a;
        if (b > maxVertexIndex) maxVertexIndex = b;
    }
    
    public Edge getEdge(int i, int j) {
        for (Edge e : this.edges) {
            if ((e.a == i && e.b ==j) || (e.a == j && e.b == i)) {
                return e;
            }
        }
        return null;
    }
    
    public void makeEdge(int a, int b) {
        Edge e = getEdge(a, b);
        if (e == null) {
            this.edges.add(new Edge(a, b));
        } else {
            e.o++;
        }
        this.updateMaxVertexIndex(a, b);
    }
    
    public void makeEdge(int a, int b, int c) {
        this.edges.add(new Edge(a, b, c));
        this.updateMaxVertexIndex(a, b);
    }
    
    public int getVertexCount() {
        return this.maxVertexIndex + 1;
    }
    
    public List<Integer> getConnected(int vertexIndex) {
        List<Integer> connected = new ArrayList<Integer>();
        for (Edge edge : this.edges) {
            if (edge.a == vertexIndex) {
                connected.add(edge.b);
            } else if (edge.b == vertexIndex) {
                connected.add(edge.a);
            } else {
                continue;
            }
        }
        return connected;
    }
    
    public List<Integer> getSameColorConnected(int vertexIndex) {
        int color = getColor(vertexIndex);
        List<Integer> connected = new ArrayList<Integer>();
        for (Edge edge : this.edges) {
            if (edge.a == vertexIndex && getColor(edge.b) == color) {
                connected.add(edge.b);
            } else if (edge.b == vertexIndex && getColor(edge.a) == color) {
                connected.add(edge.a);
            } else {
                continue;
            }
        }
        
        return connected;
    }
    
    public boolean isConnected(int i, int j) {
        for (Edge e : this.edges) {
            if ((e.a == i && e.b ==j) || (e.a == j && e.b == i)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isConnected() {
        Map<Integer, List<Integer>> connectionTable = this.getConnectionTable();
        List<Integer> visited = new ArrayList<Integer>();
        dfs(0, visited, connectionTable);
        return visited.size() == this.getVertexCount();
    }
    
    public Map<Integer, List<Integer>> getConnectionTable() {
        Map<Integer, List<Integer>> connectionTable = 
            new HashMap<Integer, List<Integer>>();
        for (Edge e : this.edges) {
            if (connectionTable.containsKey(e.a)) {
                connectionTable.get(e.a).add(e.b);
            } else {
                List<Integer> connected = new ArrayList<Integer>();
                connected.add(e.b);
                connectionTable.put(e.a, connected);
            }
            if (connectionTable.containsKey(e.b)) {
                connectionTable.get(e.b).add(e.a);
            } else {
                List<Integer> connected = new ArrayList<Integer>();
                connected.add(e.a);
                connectionTable.put(e.b, connected);
            }
        }
        return connectionTable;
    }
    
    public void dfs(
        int vertex, List<Integer> visited, Map<Integer, List<Integer>> table) {
        visited.add(vertex);
        try {
            for (int connected : table.get(vertex)) {
                if (visited.contains(connected)) {
                    continue;
                } else {
                    dfs(connected, visited, table);
                }
            }
        } catch (NullPointerException npe) {
            System.out.println("ERR" + this);
        }
    }
    
    public boolean saturated(int i, int maxDegree) {
        int degree = 0;
        for (Edge e : this.edges) {
            if (e.a == i || e.b == i) {
                degree += e.o;
            }
            
            if (degree == maxDegree) {
                return true;
            }
        }
        return false;
    }
    
    public boolean saturated(int i, Map<Integer, Integer> colorDegrees) {
        return saturated(i, colorDegrees.get(getColor(i)));
    }
    
    public Graph makeNew(int i, int j) {
        Graph copy = new Graph();
        for (Edge e : this.edges) {
            copy.makeEdge(e.a, e.b, e.o);
        }
        for (int color : this.colors) { copy.colors.add(color); }
        copy.makeEdge(i, j);
        return copy;
    }
    
    public Graph makeNew(int i, int j, int colorJ) {
        Graph copy = new Graph();
        for (Edge e : this.edges) {
            copy.makeEdge(e.a, e.b, e.o);
        }
        copy.makeEdge(i, j);
        for (int color : this.colors) { copy.colors.add(color); }
        copy.colors.add(colorJ);
        return copy;
    }
    
    public Graph makeNew(int i, int j, int colorI, int colorJ) {
        Graph copy = new Graph();
        for (Edge e : this.edges) {
            copy.makeEdge(e.a, e.b, e.o);
        }
        copy.makeEdge(i, j);
        for (int color : this.colors) { copy.colors.add(color); }
        copy.colors.add(colorI);
        copy.colors.add(colorJ);
        return copy;
    }
    
    public String getSortedPermutedColoredOnlyEdgeString(int[] p) {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
            edgeStrings.add(e.getSortedPermutedColorOnlyString(p, colors));
        }
        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedPermutedColoredEdgeString(int[] p) {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
            edgeStrings.add(e.getSortedPermutedColoredString(p, colors));
        }
        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedColorOnlyEdgeString() {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
            edgeStrings.add(e.toSortedColorOnlyString(colors));
        }
        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getColorOnlyEdgeString() {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
            edgeStrings.add(e.toSortedColorOnlyString(colors));
        }
        return edgeStrings.toString();
    }
    
    public String getSortedColoredEdgeString() {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
            edgeStrings.add(e.toSortedColoredString(colors));
        }
        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedPermutedEdgeString(int[] p) {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
//            edgeStrings.add(e.getPermutedString(p));
            edgeStrings.add(e.getSortedPermutedString(p));
        }
        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedEdgeStringWithEdgeOrder() {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
            edgeStrings.add(e.toSortedStringWithEdgeOrder());
        }
        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getSortedEdgeString() {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
//            edgeStrings.add(e.toString());
            edgeStrings.add(e.toSortedString());
        }
        Collections.sort(edgeStrings);
        return edgeStrings.toString();
    }
    
    public String getPermutedEdgeString(int[] p) {
        List<String> edgeStrings = new ArrayList<String>();
        for (Edge e : this.edges) {
            edgeStrings.add(e.getSortedPermutedString(p));
        }
        return edgeStrings.toString();
    }
    
    public boolean edgesInOrder() {
        if (this.edges.size() == 0) return true;
        String edgeString = this.edges.get(0).toString();
        for (int i = 1; i < this.edges.size(); i++) {
            String current = this.edges.get(i).toString(); 
            if (current.compareTo(edgeString) < 0) {
                return false;
            }
            edgeString = current; 
        }
        return true;
    }
    
    public boolean colorsInOrder() {
        int prev = -1;
        for (int color : colors) {
            if (color >= prev) {
                prev = color;
            } else {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return edges.toString();
    }
    
}
