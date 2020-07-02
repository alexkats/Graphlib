package com.katsman.graphlib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexey Katsman
 * @since 5/17/20
 */

public class D {

    //    private static int[][] map = new int[20010][20010];
//    private static long[] dists = new long[800000];
    private static Map<Double, Long> dists = new HashMap<>();

    private static int centerX = 12005;
    private static int centerY = 12005;

    private static long sqr(int x) {
        return (long) x * (long) x;
    }

    private static int constructMap(int[] dimensions, int[] myPos, int[] guardPos, int distance) {
        myPos[0] += centerX;
        myPos[1] += centerY;
        guardPos[0] += centerX;
        guardPos[1] += centerY;

//        map[myPos[0]][myPos[1]] = 1;
//        map[guardPos[0]][guardPos[1]] = 2;
//        Arrays.fill(dists, -1L);
        int res = 0;

        for (int i = myPos[0]; i < centerX * 2; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = myPos[1]; j < centerY * 2; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j += (dimensions[1] - diffY) * 2;

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                }
            }

            i += (dimensions[0] - diffX) * 2;
        }

        for (int i = myPos[0]; i < centerX * 2; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = myPos[1]; j > -1; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j -= (diffY * 2);

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                }
            }

            i += (dimensions[0] - diffX) * 2;
        }

        for (int i = myPos[0]; i > -1; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = myPos[1]; j < centerY * 2; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j += (dimensions[1] - diffY) * 2;

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                }
            }

            i -= (diffX * 2);
        }

        for (int i = myPos[0]; i > -1; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = myPos[1]; j > -1; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j -= (diffY * 2);

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                }
            }

            i -= (diffX * 2);
        }

        for (int i = guardPos[0]; i < centerX * 2; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = guardPos[1]; j < centerY * 2; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j += (dimensions[1] - diffY) * 2;

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                    res++;
                }
            }

            i += (dimensions[0] - diffX) * 2;
        }

        for (int i = guardPos[0]; i < centerX * 2; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = guardPos[1]; j > -1; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j -= (diffY * 2);

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                    res++;
                }
            }

            i += (dimensions[0] - diffX) * 2;
        }

        for (int i = guardPos[0]; i > -1; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = guardPos[1]; j < centerY * 2; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j += (dimensions[1] - diffY) * 2;

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                    res++;
                }
            }

            i -= (diffX * 2);
        }

        for (int i = guardPos[0]; i > -1; ) {
            int diffX = (i - centerX) % dimensions[0];

            if (diffX < 0) {
                diffX += dimensions[0];
            }

            for (int j = guardPos[1]; j > -1; ) {
                int diffY = (j - centerY) % dimensions[1];

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int prevJ = j;
                j -= (diffY * 2);

                if (i == myPos[0] && prevJ == myPos[1]) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - prevJ);

                if (dist > sqr(distance)) {
                    continue;
                }

                double angle = Math.atan2((double) prevJ - myPos[1], (double) i - myPos[0]);
                int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                if (!dists.containsKey(angle) || dists.get(angle) > dist) {
                    dists.put(angle, dist);
                    res++;
                }
            }

            i -= (diffX * 2);
        }

/*
        for (int i = centerX; i < centerX * 2; i++) {
            for (int j = centerY; j < centerY * 2; j++) {
                if (map[i][j] == 0) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    break;
                }

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int toX = i + (dimensions[0] - diffX) * 2;
                int toY = j + (dimensions[1] - diffY) * 2;

                if (i != myPos[0] || j != myPos[1]) {
                    double angle = Math.atan2((double) j - myPos[1], (double) i - myPos[0]);
                    int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                    if (dists[longAngle] == -1L || dists[longAngle] > dist) {
//                    if (!dists.containsKey(longAngle) || dists.get(longAngle) > dist) {
                        dists[longAngle] = dist;
//                        dists.put(longAngle, dist);

                        if (map[i][j] == 2) {
                            res++;
                        }
                    }
                }

                if (toX < centerX * 2) {
                    map[toX][j] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - toX) + sqr(myPos[1] - j);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) j - myPos[1], (double) toX - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }

                if (toY < centerY * 2) {
                    map[i][toY] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - i) + sqr(myPos[1] - toY);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) toY - myPos[1], (double) i - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }
            }
        }
*/

/*
        for (int i = centerX; i < centerX * 2; i++) {
            for (int j = centerY + dimensions[1] - 1; j > -1; j--) {
                if (map[i][j] == 0) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    break;
                }

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int toX = i + (dimensions[0] - diffX) * 2;
                int toY = j - diffY * 2;

                if (i != myPos[0] || j != myPos[1]) {
                    double angle = Math.atan2((double) j - myPos[1], (double) i - myPos[0]);
                    int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                    if (dists[longAngle] == -1L || dists[longAngle] > dist) {
//                    if (!dists.containsKey(longAngle) || dists.get(longAngle) > dist) {
                        dists[longAngle] = dist;
//                        dists.put(longAngle, dist);

                        if (map[i][j] == 2) {
                            res++;
                        }
                    }
                }

                if (toX < centerX * 2) {
                    map[toX][j] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - toX) + sqr(myPos[1] - j);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) j - myPos[1], (double) toX - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }

                if (toY > -1) {
                    map[i][toY] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - i) + sqr(myPos[1] - toY);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) toY - myPos[1], (double) i - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }
            }
        }
*/

/*
        for (int i = centerX + dimensions[0] - 1; i > -1; i--) {
            for (int j = centerY; j < centerY * 2; j++) {
                if (map[i][j] == 0) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    break;
                }

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int toX = i - diffX * 2;
                int toY = j + (dimensions[1] - diffY) * 2;

                if (i != myPos[0] || j != myPos[1]) {
                    double angle = Math.atan2((double) j - myPos[1], (double) i - myPos[0]);
                    int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                    if (dists[longAngle] == -1L || dists[longAngle] > dist) {
//                    if (!dists.containsKey(longAngle) || dists.get(longAngle) > dist) {
                        dists[longAngle] = dist;
//                        dists.put(longAngle, dist);

                        if (map[i][j] == 2) {
                            res++;
                        }
                    }
                }

                if (toX > -1) {
                    map[toX][j] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - toX) + sqr(myPos[1] - j);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) j - myPos[1], (double) toX - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }

                if (toY < centerY * 2) {
                    map[i][toY] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - i) + sqr(myPos[1] - toY);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) toY - myPos[1], (double) i - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }
            }
        }
*/

/*
        for (int i = centerX + dimensions[0] - 1; i > -1; i--) {
            for (int j = centerY + dimensions[1] - 1; j > -1; j--) {
                if (map[i][j] == 0) {
                    continue;
                }

                long dist = sqr(myPos[0] - i) + sqr(myPos[1] - j);

                if (dist > sqr(distance)) {
                    break;
                }

                int diffX = (i - centerX) % dimensions[0];
                int diffY = (j - centerY) % dimensions[1];

                if (diffX < 0) {
                    diffX += dimensions[0];
                }

                if (diffY < 0) {
                    diffY += dimensions[1];
                }

                int toX = i - diffX * 2;
                int toY = j - diffY * 2;

                if (i != myPos[0] || j != myPos[1]) {
                    double angle = Math.atan2((double) j - myPos[1], (double) i - myPos[0]);
                    int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                    if (dists[longAngle] == -1L || dists[longAngle] > dist) {
//                    if (!dists.containsKey(longAngle) || dists.get(longAngle) > dist) {
                        dists[longAngle] = dist;
//                        dists.put(longAngle, dist);

                        if (map[i][j] == 2) {
                            res++;
                        }
                    }
                }

                if (toX > -1) {
                    map[toX][j] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - toX) + sqr(myPos[1] - j);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) j - myPos[1], (double) toX - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }

                if (toY > -1) {
                    map[i][toY] = map[i][j];
                    */
/*long dist2 = sqr(myPos[0] - i) + sqr(myPos[1] - toY);

                    if (dist2 <= sqr(distance)) {
                        double angle = Math.atan2((double) toY - myPos[1], (double) i - myPos[0]);
                        int longAngle = (int) (angle * (int) (1e4)) + (int) (4e4);

                        if (dists[longAngle] == -1L || dists[longAngle] > dist2) {
                            dists[longAngle] = dist2;

                            if (map[i][j] == 2) {
                                res++;
                            }
                        }
                    }*//*

                }
            }
        }
*/

        return res;
    }

    public static int solution(int[] dimensions, int[] your_position, int[] guard_position, int distance) {
        return constructMap(dimensions, your_position, guard_position, distance);
    }

    public static void main(String[] args) {
//        System.out.println(solution(new int[]{3, 2}, new int[]{1, 1}, new int[]{2, 1}, 4));
//        System.out.println(solution(new int[]{300, 275}, new int[]{150, 150}, new int[]{185, 100}, 500));
//        System.out.println(solution(new int[]{2, 5}, new int[]{1, 2}, new int[]{1, 4}, 11));
//        System.out.println(solution(new int[]{23, 10}, new int[]{6, 4}, new int[]{3, 2}, 23));
//        System.out.println(solution(new int[]{1250, 1250}, new int[]{1000, 1000}, new int[]{500, 400}, 10000));
//        System.out.println(solution(new int[]{2, 5}, new int[]{1, 2}, new int[]{1, 4}, 11));
        System.out.println(solution(new int[]{10, 10}, new int[]{4, 4}, new int[]{3, 3}, 5000));
//        System.out.println(solution(new int[]{23, 10}, new int[]{6, 4}, new int[]{3, 2}, 23));
//        System.out.println(solution(new int[]{3, 2}, new int[]{1, 1}, new int[]{2, 1}, 400));
    }
}
