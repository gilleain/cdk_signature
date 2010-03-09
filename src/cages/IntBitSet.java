package cages;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public class IntBitSet implements SortedSet<Integer> {
    
    private static final int WORDSIZE = 32;
    
    private static final int LOOK[]=
    {
     0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4,1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,
     1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
     1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
     1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5,2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,
     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
     2,3,3,4,3,4,4,5,3,4,4,5,4,5,5,6,3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,
     3,4,4,5,4,5,5,6,4,5,5,6,5,6,6,7,4,5,5,6,5,6,6,7,5,6,6,7,6,7,7,8
    };
    
    private final static int leftbit[] =   
           {8,7,6,6,5,5,5,5,4,4,4,4,4,4,4,4,
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    
    private static final int LOOK_MASK = 0377;
    
    private int[] V;
    
    public IntBitSet(int n) {
        this.V = new int[n];
        Arrays.fill(this.V, 0);
    }

    private int getIndex(int u) {
        return u / IntBitSet.WORDSIZE;
    }
    
    private int getBit(int u) {
        return IntBitSet.WORDSIZE - 1 - (u % IntBitSet.WORDSIZE);
    }
    
    private int firstBit(int x) {
        boolean a = (x & 037777600000) == 1;
        boolean b = (x & 037700000000) == 1;
        boolean c = (x & 0177400) == 1;
        return (31-(a ? (b ? 
                leftbit[((x)>>24) & 0377] : 8+leftbit[(x)>>16]) 
                : (c ? 16+leftbit[(x)>>8] : 24+leftbit[x])));
    }
    
    public boolean add(Integer u) {
        int i = this.getIndex(u);
        int j = this.getBit(u);
        
        if ((this.V[i] & (1 << j)) == 1) {
            this.V[i] = this.V[i] | (1 << j);
            return true;
        } else {
            return false;
        }
    }
    
    public void union(IntBitSet other) {
        for (int i = 0; i < this.V.length; i++) {
            if (i < other.V.length) {
                this.V[i] |= other.V[i];
            }
        }
    }
    
    public void intersect(IntBitSet other) {
        for (int i = 0; i < this.V.length; i++) {
            if (i < other.V.length) {
                this.V[i] &= other.V[i];
            }
        }
    }
    
    public void difference(IntBitSet other) {
        for (int i = 0; i < this.V.length; i++) {
            if (i < other.V.length) {
                this.V[i] = this.V[i] &~ other.V[i];
            }
        }
    }
    
    public boolean hasNonemptyIntersection(IntBitSet other) {
        for (int i = 0; i < this.V.length; i++) {
            if (i < other.V.length) {
                if ((this.V[i] & other.V[i]) == 1) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean equals(IntBitSet other) {
        if (this.V.length != other.V.length) return false;
        for (int i = 0; i < this.V.length; i++) {
            if (this.V[i] != other.V[i]) return false;
        }
        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < this.V.length; i++) {
            if (this.V[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends Integer> c) {
        if (c instanceof IntBitSet) {
            this.union((IntBitSet) c);
        } else {
            for (Integer i : c) {
                this.add(i);
            }
        }
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        if (c instanceof IntBitSet) {
            IntBitSet other = (IntBitSet) c;
            if (this.hasNonemptyIntersection(other)) {
                this.intersect(other);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        if (c instanceof IntBitSet) {
            IntBitSet other = (IntBitSet) c;
            if (this.hasNonemptyIntersection(other)) {
                this.difference(other);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        if (c instanceof IntBitSet) {
            return this.equals((IntBitSet) c);
        } else {
            for (Object i : c) {
                if (!this.contains(i)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean remove(Object o) {
        if (!(o instanceof Integer)) {
            return false;
        } else {
            int u = (Integer) o;
            int i = this.getIndex(u);
            int j = this.getBit(u);
            if ((this.V[i] & (1 << j)) == 1) {
                this.V[i] = this.V[i] &~ (1 << j);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean contains(Object o) {
        if (o instanceof Integer) {
            int u = (Integer) o;
            int i = this.getIndex(u);
            int j = this.getBit(u);
            return ((this.V[i] & (1 << j)) == 1);
        } else {
            return false;
        }
    }

    public int size() {
        int ans = 0;
        for (int i = 0; i < this.V.length; i++) {
            int x = this.V[i];
            while (x != 0) {
                ans += IntBitSet.LOOK[x & IntBitSet.LOOK_MASK];
                x >>= 8;
            }
        }
        return ans;
    }

    public void clear() {
        Arrays.fill(this.V, 0);
    }

    public Comparator<? super Integer> comparator() {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer first() {
        int i = 0;
        while (i < this.V.length && (this.V[i] == 0)) i++;
        int a = this.V[i];
        int j = this.firstBit(a);
        int k = (IntBitSet.WORDSIZE - 1 - j) + (i * IntBitSet.WORDSIZE);
        return (k < this.V.length)? k : -1;
    }

    public SortedSet<Integer> headSet(Integer toElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer last() {
        // TODO Auto-generated method stub
        return null;
    }

    public SortedSet<Integer> subSet(Integer fromElement, Integer toElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public SortedSet<Integer> tailSet(Integer fromElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public Iterator<Integer> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object[] toArray() {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> T[] toArray(T[] a) {
        // TODO Auto-generated method stub
        return null;
    }
    
    

}
