package cages;

import java.util.ArrayList;

import org.openscience.cdk.group.Permutation;

public class Orbit {
    
    public int[] X;
    
    private ArrayList<Integer> OptX;
    
    public Permutation beta;
    
    public Orbit(int n) {
        this.X = new int[n];
        this.OptX = new ArrayList<Integer>();
        this.beta = new Permutation(n);
    }
    
    public int getOption(int i) {
        return this.OptX.get(i);
    }
    
    public void addOption(int i) {
        this.OptX.add(i);
    }
    
    public void setOption(int i, int value) {
        this.OptX.set(i, value);
    }
    
    public ArrayList<Integer> getOptions() {
        return this.OptX;
    }
    
    public int getNumberOfOptions() {
        return this.OptX.size();
    }

}
