package org.openscience.cdk.deterministic;


public class Bond implements Comparable<Bond>{
    
    private int a, b, o;
    
    private String aS, bS;
    
    public Bond(int a, int b, String aS, String bS, int o) {
        if (a < b) {
            this.a = a; this.b = b;
            this.aS = aS; this.bS = bS;
        } else {
            this.a = b; this.b = a;
            this.aS = bS; this.bS = aS;
        }
        this.o = o;
    }
    
    public int compareTo(Bond other) {
        if (this.a < other.a || (this.a == other.a && this.b < other.b)) {
            return -1;
        } else {
            if (this.a == other.a && this.b == other.b) {
                return 0;
            } else {
                return 1;
            }
        }
    }
    
    public String toString() {
        return String.format("%02d%s:%02d%s(%s)", a, aS, b, bS, o);
//        return String.format("%02d:%02d", a, b);
    }
}
