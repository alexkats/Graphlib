package com.katsman.graphlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 5/15/20
 */

public class B {

    public static int[] solution(int[][] times, int times_limit) {
        int n = times.length;
        int[][] d = new int[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(times[i], 0, d[i], 0, n);
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    d[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (d[i][i] < 0) {
                int[] result = new int[n - 2];

                for (int j = 0; j < n - 2; j++) {
                    result[j] = j;
                }

                return result;
            }
        }

        int k = n - 2;
        int[] bunnies = new int[k];

        for (int i = 0; i < k; i++) {
            bunnies[i] = i;
        }

        for (int permSize = k; permSize > 0; permSize--) {
            boolean[] was = new boolean[k];
            List<List<Integer>> permutations = new ArrayList<>();
            genPermutations(bunnies, permSize, was, permutations, new ArrayList<>());

            for (List<Integer> permutation : permutations) {
                int[] tmp = permutation.stream().mapToInt(e -> e).toArray();
                int time = getTime(tmp, permSize, n, d);

                if (time <= times_limit) {
                    Arrays.sort(tmp);
                    return tmp;
                }
            }
        }

        return new int[]{};
    }

    private static void genPermutations(int[] bunnies, int permSize, boolean[] was, List<List<Integer>> ans, List<Integer> curr) {
        if (curr.size() == permSize) {
            ans.add(new ArrayList<>(curr));
            return;
        }

        for (int bunny : bunnies) {
            if (was[bunny]) {
                continue;
            }

            was[bunny] = true;
            curr.add(bunny);
            genPermutations(bunnies, permSize, was, ans, curr);
            curr.remove(curr.size() - 1);
            was[bunny] = false;
        }
    }

    private static int getTime(int[] bunnies, int k, int n, int[][] g) {
        int res = g[0][bunnies[0] + 1] + g[bunnies[k - 1] + 1][n - 1];

        for (int i = 1; i < k; i++) {
            res += g[bunnies[i - 1] + 1][bunnies[i] + 1];
        }

        return res;
    }

    public static void main(String[] args) {
/*
        int[][] times = new int[][]{
                {2, 2},
                {2, 2}
        };
*/
//        int timesLimit = 5;
/*
        int[][] times = new int[][]{
                {0, 1, 1, 1, 1, 1, 1},
                {1, 0, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 1, 1},
                {1, 1, 1, 1, 0, 1, 1},
                {1, 1, 1, 1, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 0}
        };
*/
//        int timesLimit = 6;

        int[][] times = new int[][]{
                {0, 2, 2, 2, -1},
                {9, 0, 2, 2, -1},
                {9, 3, 0, 2, -1},
                {9, 3, 2, 0, -1},
                {9, 3, 2, 2, 0}
        };
        int timesLimit = 3;

/*
        int[][] times = new int[][]{
                {0, 1, 1, 1, 1},
                {1, 0, 1, 1, 1},
                {1, 1, 0, 1, 1},
                {1, 1, 1, 0, 1},
                {1, 1, 1, 1, 0}
        };
*/
//        int timesLimit = 3;
        System.out.println(Arrays.toString(solution(times, timesLimit)));

//        List<Integer> l = new ArrayList<>();
//        l.add(1);
//        l.add(2);
//        l.add(3);
//        System.out.println(Arrays.toString(l.stream().mapToInt(e -> e).toArray()));
    }
}
