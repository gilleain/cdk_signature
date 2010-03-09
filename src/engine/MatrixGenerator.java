package engine;

import java.util.Arrays;
import java.util.List;

public class MatrixGenerator {

    public static void run(int n, List<String> strings) {
        int vmin = 2 * (n - 1);
        int vmax = (n * 4) - n;
        for (int v = vmin; v <= vmax; v += 2) {
            for (IntegerPartition p : PartitionCalculator.partition(v, n)){
                if (p.getPart(0) > 4) continue;
                p.reverse();
                Matrix m = new Matrix(p);
                backtrack(m, 0, n, (int)Math.pow(2, n), strings);
            }
        }
    }

    
    public static void run(int n) {
        int vmin = 2 * (n - 1);
        int vmax = (n * 4) - n;
        for (int v = vmin; v <= vmax; v += 2) {
            for (IntegerPartition p : PartitionCalculator.partition(v, n)){
                if (p.getPart(0) > 4) continue;
                p.reverse();
                Matrix m = new Matrix(p);
                backtrack(m, 0, n, (int)Math.pow(2, n));
            }
        }
    }
    
    public static void backtrack(Matrix matrix, int l, int n, int m) {
        if (l == n) {
//            System.out.println(matrix);
            if (matrix.isCanonical()) {
            System.out.println(
                                     + matrix.rowSum() 
                            + "\t\t" + matrix
                            + "\t\t" + Arrays.toString(matrix.getHalfMatrixNumber())
//                            + "\t\t" + matrix.getUpperDiagonal()
                            + "\t\t" + matrix.getHalfMatrixSum()
                            + "\t\t" + matrix.toSmiles() 
                                    
            );
            }
        } else {
            for (int i : matrix.nextNumbers(l, m)) {
                backtrack(matrix.extendBy(i), l + 1, n, m);
            }
        }
    }
    
    public static void backtrack(Matrix matrix, int l, int n, int m, List<String> strings) {
        if (l == n) {
            if (matrix.isCanonical()) {
                System.out.println(matrix.rowSum()+ "\t\t" + Arrays.toString(matrix.getHalfMatrixNumber()) + "\t\t" + matrix.toSmiles());
                strings.add(matrix.toSmiles());
            }
        } else {
            for (int i : matrix.nextNumbers(l, m)) {
                backtrack(matrix.extendBy(i), l + 1, n, m, strings);
            }
        }
    }


}
