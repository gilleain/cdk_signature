package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ColoredEdgewiseGenerator {

    public List<Graph> graphs;
    
    public int maxLength;
    
    public int maxDegree;
    
    public Map<Integer, Integer> colors;
    
    public ColoredEdgewiseGenerator(
            int maxLength, int maxDegree, Map<Integer, Integer> colors) {
        this.graphs = new ArrayList<Graph>();
        this.maxLength = maxLength;
        this.maxDegree = maxDegree;
        this.colors = colors;
    }
    
    public void generate() {
        Graph initialGraph = new Graph();
        this.generate(initialGraph, this.colors);
    }
    
    public void generate(Graph g, Map<Integer, Integer> remainingColors) {
        if (g.getVertexCount() >= this.maxLength) return;
        
        if (g.getVertexCount() < 2 || CanonicalChecker.isCanonical4(g)) {
            System.out.println("CANON\t" + g + " " + remainingColors);
            if (g.getVertexCount() == this.maxLength - 1 && g.isConnected()) {
                this.graphs.add(g);
            }
        } else {
            System.out.println(".....\t" + g + " " + remainingColors);
            return;
        }
        
        int n = Math.max(2, g.getVertexCount());
        for (int i = 0; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                if (g.getVertexCount() < 2) {
                    makeNew(i, j, g, remainingColors);
                } else if (j < g.getVertexCount()) {
                    makeCycle(i, j, g, remainingColors);
                } else {
                    makeExtension(i, j, g, remainingColors);
                }
            }
        }
    }

    public void makeNew(int i, int j, Graph g, Map<Integer, Integer> remainingColors) {
        for (int colorI : remainingColors.keySet()) {
            Map<Integer, Integer> remainingColorsAgain = removeColor(remainingColors, colorI);
            for (int colorJ : remainingColorsAgain.keySet()) {
                if (canAddBond(i, j, g)) {
                    generate(
                            g.makeNew(i, j, colorI, colorJ), 
                            removeColor(remainingColorsAgain, colorJ)
                            );
                } else {
                    continue;
                }
            }
        }
    }
    
    public void makeCycle(int i, int j, Graph g, Map<Integer, Integer> remainingColors) {
        if (canAddBond(i, j, g)) {
            generate(g.makeNew(i, j), remainingColors);
        } else {
            return;
        }
    }
    
    public void makeExtension(int i, int j, Graph g, Map<Integer, Integer> remainingColors) {
        for (int colorJ : remainingColors.keySet()) {
            if (canAddBond(i, j, g)) {
                generate(
                 g.makeNew(i, j, colorJ), removeColor(remainingColors, colorJ));
            } else {
                continue;
            }
        }
    }
    
    public Map<Integer, Integer> removeColor(
            Map<Integer, Integer> colorMap, int colorToRemove) {
        Map<Integer, Integer> smaller = new HashMap<Integer, Integer>();
        for (int color : colorMap.keySet()) {
            if (color == colorToRemove) {
                if (colorMap.get(color) > 1) {
                    smaller.put(color, colorMap.get(color) - 1);
                }
            } else {
                smaller.put(color, colorMap.get(color));
            }
        }
        return smaller;
    }
    
    public boolean canAddBond(int i, int j, Graph g) {
        return     i != j 
                && !g.isConnected(i, j)
                && !g.saturated(i, maxDegree)
                && !g.saturated(j, maxDegree);
    }
}
