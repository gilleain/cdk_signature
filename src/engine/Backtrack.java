package engine;

import java.util.ArrayList;
import java.util.List;

public class Backtrack {

    public int lmax;

    public int bmax;

    public List<List<Pair>> results;

    public List<Character> alphabet;

    public class Pair {
        public char c;
        public int n;

        public Pair(char c, int n) {
            this.c = c;
            this.n = n;
        }

        public String toString() {
            return "(" + c + ", " + n + ")";
        }
    }

    public Backtrack(int lmax, int bmax, List<Character> alphabet) {
        this.lmax = lmax;
        this.bmax = bmax;
        this.results = new ArrayList<List<Pair>>();
        this.alphabet = alphabet;
    }

    public void backtrack(List<Pair> pairs, int l) {
        if (l == this.lmax) {
            this.results.add(pairs);
        } else {
            Pair last = getLast(pairs);
            for (int i = alphabet.indexOf(last.c); i < alphabet.size(); i++) {
                char c = alphabet.get(i);
                for (int j = last.n; j < this.bmax; j++) {
                    List<Pair> copy = new ArrayList<Pair>();
                    copy.addAll(pairs);
                    copy.add(new Pair(c, j));
                    backtrack(copy, l + 1);
                }
            }
        }
    }

    public Pair getLast(List<Pair> pairs) {
        if (pairs.size() == 0) {
            return new Pair(this.alphabet.get(0), 1);
        } else {
            return pairs.get(pairs.size() - 1);
        }
    }

    public List<List<Pair>> generate() {
        backtrack(new ArrayList<Pair>(), 0);
        return results;
    }

    public static void main(String[] args) {
        List l = new ArrayList() {{ add('C'); add('H'); }};
        Backtrack bt = new Backtrack(4, 4, l);
        for (List<Pair> result : bt.generate()) {
            System.out.println(result);
        }
    }
}
