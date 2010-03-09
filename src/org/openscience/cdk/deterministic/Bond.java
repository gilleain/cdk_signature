package org.openscience.cdk.deterministic;


public class Bond implements Comparable<Bond>{
    int a,b;String aS, bS;
    public Bond(int a, int b, String aS, String bS) {
        if (a < b) {
            this.a = a; this.b = b;
            this.aS = aS; this.bS = bS;
        } else {
            this.a = b; this.b = a;
            this.aS = bS; this.bS = aS;
        }
    }
    
    public int compareTo(Bond o) {
        if (this.a < o.a || (this.a == o.a && this.b < o.b)) {
            return -1;
        } else {
            if (this.a == o.a && this.b == o.b) {
                return 0;
            } else {
                return 1;
            }
        }
    }
    
    public String toString() {
        return String.format("%02d%s:%02d%s", a, aS, b, bS);
//        return String.format("%02d:%02d", a, b);
    }
}
