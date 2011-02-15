package test_degree;

import java.util.List;

import org.junit.Test;

import degree.DegreeSequenceGenerator;

public class DegreeGenTest {
    
    @Test
    public void colexRankingUnrankingTest() {
        int k = 3;
        int n = 5;
        for (int i = 0; i < 10; i++) {
            List<Integer> intSet = DegreeSequenceGenerator.colexUnrank(i, k, n);
            System.out.println(
                    intSet + " " + DegreeSequenceGenerator.colexRank(intSet));
        }
    }
    
    @Test
    public void simpleTestA() {
        DegreeSequenceGenerator gen = new DegreeSequenceGenerator();
        gen.gen(3, 3, 2, 2, 2, 2);
    }
    
    @Test
    public void simpleTestB() {
        DegreeSequenceGenerator gen = new DegreeSequenceGenerator();
        gen.gen(3, 3, 2, 2, 2);
    }
    
    @Test
    public void simpleTestC() {
        DegreeSequenceGenerator gen = new DegreeSequenceGenerator();
        gen.gen(2,2,1,1);
    }
    
    @Test
    public void largeTest() {
        DegreeSequenceGenerator gen = new DegreeSequenceGenerator();
        gen.gen(4, 3, 3, 3, 2, 2, 1);
    }


}
