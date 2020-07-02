package com.katsman.graphlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 5/17/20
 */

public class C {

    private static class Pair {
        int x, y;

        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static int[][] map = new int[10010][10010];
    private static List<Pair> map1 = new ArrayList<>();
    private static List<Pair> map2 = new ArrayList<>();
    private static long[] dists = new long[800000];
    //    private static Map<Long, Long> dists = new HashMap<>();
    private static boolean[] was = new boolean[800000];
    //    private static Set<Integer> was = new HashSet<>();
    private static int depth = 0;

    private static int centerX = 5005;
    private static int centerY = 5005;

    private static long sqr(int x) {
        return (long) x * (long) x;
    }

    private static long sqr(long x) {
        return x * x;
    }

    private static int constructMap(int[] dimensions, int[] myPos, int[] guardPos, int distance) {
        myPos[0] += centerX;
        myPos[1] += centerY;
        guardPos[0] += centerX;
        guardPos[1] += centerY;

        map1.add(new Pair(myPos[0], myPos[1]));
//        map[myPos[0]][myPos[1]] = 1;
//        map[guardPos[0]][guardPos[1]] = 2;
        map2.add(new Pair(guardPos[0], guardPos[1]));
        Arrays.fill(dists, -1L);

        for (int i = myPos[0]; i < centerX * 2;) {
            for (int j = myPos[1]; j < centerY * 2;) {
                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevI = i;
                int prevJ = j;
                i += (dimensions[0] - diffX) * 2;
                j += (dimensions[1] - diffY) * 2;

                if (dist > sqr(distance)) {
                    continue;
                }

                if (i < centerX * 2) {
                    map1.add(new Pair(i, prevJ));
                }

                if (j < centerY * 2) {
                    map1.add(new Pair(prevI, j));
                }
            }
        }

        for (int i = guardPos[0]; i < centerX * 2;) {
            for (int j = guardPos[1]; j < centerY * 2;) {
                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevI = i;
                int prevJ = j;
                i += (dimensions[0] - diffX) * 2;
                j += (dimensions[1] - diffY) * 2;

                if (dist > sqr(distance)) {
                    continue;
                }

                if (i < centerX * 2) {
                    map2.add(new Pair(i, prevJ));
                }

                if (j < centerY * 2) {
                    map2.add(new Pair(prevI, j));
                }
            }
        }

        for (int i = centerX; i < centerX * 2; i++) {
            for (int j = centerY + dimensions[1] - 1; j > -1; j--) {
                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                if (dist > sqr(distance)) {
                    continue;
                }

                if ((i + (dimensions[0] - diffX) * 2) < centerX * 2) {
                    map[i + (dimensions[0] - diffX) * 2][j] = map[i][j];
                }

                if ((j - diffY * 2) > -1) {
                    map[i][j - diffY * 2] = map[i][j];
                }
            }
        }

        for (int i = centerX + dimensions[0] - 1; i > -1; i--) {
            for (int j = centerY; j < centerY * 2; j++) {
                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    continue;
                }

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                if ((i - diffX * 2) > -1) {
                    map[i - diffX * 2][j] = map[i][j];
                }

                if ((j + (dimensions[1] - diffY) * 2) < centerY * 2) {
                    map[i][j + (dimensions[1] - diffY) * 2] = map[i][j];
                }
            }
        }

        for (int i = centerX + dimensions[0] - 1; i > -1; i--) {
            for (int j = centerY + dimensions[1] - 1; j > -1; j--) {
                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    continue;
                }

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                if ((i - diffX * 2) > -1) {
                    map[i - diffX * 2][j] = map[i][j];
                }

                if ((j - diffY * 2) > -1) {
                    map[i][j - diffY * 2] = map[i][j];
                }
            }
        }

        int ans = 0;

        for (int i = 0; i < centerX * 2; i++) {
            for (int j = 0; j < centerY * 2; j++) {
                if (map[i][j] != 1) {
                    continue;
                }

                if (i == myPos[0] && j == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) j - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (dists[longAngle] == -1L || dists[longAngle] > dist) {
                    dists[longAngle] = dist;
                }
            }
        }

        for (int i = 0; i < centerX * 2; i++) {
            for (int j = 0; j < centerY * 2; j++) {
                if (map[i][j] != 2) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) j - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (dists[longAngle] == -1L || (dists[longAngle] > dist && !was[longAngle])) {
                    dists[longAngle] = dist;
                    was[longAngle] = true;
                    ans++;
                }
            }
        }

        return ans;
    }

    // 10006, w = 5 -> 10014

    // 1 -> up, right
    // 2 -> up, left
    // 3 -> down, right
    // 4 -> down, left
    private static void dfs(int currX, int currY, int val, int w, int h, int to) {
        if (currX > 10009 || currX < 0 || currY > 10009 || currY < 0) {
            return;
        }

//        System.out.println(++depth);
        map[currX][currY] = val;
        int toX;
        int toY;
        int diffX = (currX - 5005) % w;
        int diffY = (currY - 5005) % h;

        if (diffX < 0) {
            diffX += w;
        }

        if (diffY < 0) {
            diffY += h;
        }

        if (to < 3) {
            toY = currY + (h - diffY) * 2;
        } else {
            toY = currY - diffY * 2;
        }

        if (to % 2 == 0) {
            toX = currX - diffX * 2;
        } else {
            toX = currX + (w - diffX) * 2;
        }

        dfs(toX, currY, val, w, h, to);
        dfs(currX, toY, val, w, h, to);
        depth--;
    }

    public static int solution(int[] dimensions, int[] your_position, int[] guard_position, int distance) {
        return constructMap(dimensions, your_position, guard_position, distance);
    }

    public static void main(String[] args) {
        System.out.println(solution(new int[]{3, 2}, new int[]{1, 1}, new int[]{2, 1}, 4));
//        System.out.println(solution(new int[]{300, 275}, new int[]{150, 150}, new int[]{185, 100}, 10000));
    }
}
