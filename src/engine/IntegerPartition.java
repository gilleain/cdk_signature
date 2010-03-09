package engine;

public class IntegerPartition {
    
    /**
     * The partitions of the integers 1-4 (and of 0).
     */
    public static final int[][][] basicPartitions = {
        { {} },
        { {1} },
        { {1, 1}, {2} },
//        { {2}, {1, 1} },
        { {1, 1, 1}, {1, 2}, {3} },
//        { {3}, {2, 1}, {1, 1, 1} },
        { {1, 1, 1, 1}, {1, 1, 2}, {2, 2}, {1, 3} }
//        { {1, 3}, {2, 2}, {1, 1, 2}, {1, 1, 1, 1} }
    };
    
    private int[] parts;
    
    public IntegerPartition(int[] parts) {
        this.parts = parts;
    }
    
    public void reverse() {
        int[] tmp = new int[this.parts.length];
        int j = 0;
        for (int i = this.parts.length - 1; i > -1; i--) {
            tmp[j] = this.parts[i];
            j++;
        }
        this.parts = tmp;
    }
    
    public int getPart(int i) {
        return this.parts[i];
    }
    
    public int size() {
        return this.parts.length;
    }
    
    /**
     * Get a particular sub partition for a part.
     * 
     * @param part
     * @param subpartitionIndex
     * @return
     */
    public static int[] subPartitionForPart(int part, int subpartitionIndex) {
        return IntegerPartition.basicPartitions[part][subpartitionIndex];
    }
    
    /**
     * Search through the parts of this partition for a part that matches the
     * subPart - returns -1 if none found. A match means that the part is 
     * greater than or equal to the subPart.
     * 
     * @param subPart
     * @param startIndex
     * @return
     */
    public int findMatchingPartIndex(int subPart, int startIndex) {
        assert startIndex < this.parts.length;
        for (int i = startIndex; i < parts.length; i++) {
            int part = this.parts[i];
            if (part >= subPart) {
                return i;
            } else if (part < subPart) {
                return -1;
            }
        }
        return -1;
    }
    
    public String toString() {
        StringBuffer b = new StringBuffer("[");
        for (int i = 0; i < this.parts.length - 1; i++) {
            b.append(this.parts[i]).append(", ");
        }
        b.append(this.parts[this.parts.length - 1]).append("]");
        return b.toString();
    }

}
