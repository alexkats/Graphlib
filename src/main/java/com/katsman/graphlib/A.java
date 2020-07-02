package com.katsman.graphlib;

import java.util.Arrays;

/**
 * @author Alexey Katsman
 * @since 5/15/20
 */

public class A {

    private static class Fraction {
        long a, b;

        public Fraction(long a, long b) {
            this.a = a;
            this.b = b;
            normalize();
        }

        @Override
        public String toString() {
            return a + "/" + b;
        }

        public Fraction copyFraction() {
            return new Fraction(a, b);
        }

        public void normalize() {
            if (a == 0) {
                b = 1;
                return;
            }

            int gcd = gcd((int) a, (int) b);
            a /= gcd;
            b /= gcd;

            int sign = (int) (a / Math.abs(a) * b / Math.abs(b));

            if (sign < 0) {
                a = -Math.abs(a);
            } else {
                a = Math.abs(a);
            }

            b = Math.abs(b);
        }

        public void add(Fraction other) {
            long denominator = lcm(b, other.b);
            long numerator1 = a * (denominator / b);
            long numerator2 = other.a * (denominator / other.b);
            a = numerator1 + numerator2;
            b = denominator;
            normalize();
        }

        public void subtract(Fraction other) {
            long denominator = lcm(b, other.b);
            long numerator1 = a * (denominator / b);
            long numerator2 = other.a * (denominator / other.b);
            a = numerator1 - numerator2;
            b = denominator;
            normalize();
        }

        public void multiply(Fraction other) {
            a *= other.a;
            b *= other.b;
            normalize();
        }

        public void divide(Fraction other) {
            a *= other.b;
            b *= other.a;
            normalize();
        }
    }

    private static int gcd(int a, int b) {
        return (b != 0) ? gcd(b, a % b) : a;
    }

    private static long lcm(long a, long b) {
        return a / gcd((int) a, (int) b) * b;
    }

    private static class Matrix {
        Fraction[][] cells;

        public Matrix(Fraction[][] cells) {
            this.cells = cells;
        }

        public void subtract(Matrix other) {
            int n = cells.length;

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    cells[i][j].subtract(other.cells[i][j]);
                }
            }
        }

        public Matrix getSubmatrix(int startI, int startJ, int endI, int endJ) {
            Fraction[][] result = new Fraction[endI - startI][endJ - startJ];

            for (int i = startI; i < endI; i++) {
                for (int j = startJ; j < endJ; j++) {
                    result[i - startI][j - startJ] = cells[i][j].copyFraction();
                }
            }

            return new Matrix(result);
        }

        public Matrix copyMatrix() {
            int n = cells.length;
            Fraction[][] result = new Fraction[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    result[i][j] = cells[i][j].copyFraction();
                }
            }

            return new Matrix(result);
        }
    }

    public static int[] solution(int[][] m) {
        int n = m.length;
        int absorbedStatesCount = countAndMoveAbsorbedStatesToTheEnd(m);
        Matrix matrix = convertMatrix(m, absorbedStatesCount);
        Matrix matrixQ = matrix.getSubmatrix(0, 0, n - absorbedStatesCount, n - absorbedStatesCount);
        Matrix identityMinusQ = getIdentityMatrix(n - absorbedStatesCount);
        identityMinusQ.subtract(matrixQ);
        Matrix inverseIndentityMinusQ = calcInverse(identityMinusQ);
        Matrix matrixR = matrix.getSubmatrix(0, n - absorbedStatesCount, n - absorbedStatesCount, n);
        Matrix result;

        if (inverseIndentityMinusQ.cells.length == 0) {
            Fraction[][] tmp = new Fraction[1][n];
            tmp[0][0] = new Fraction(1, 1);

            for (int i = 1; i < n; i++) {
                tmp[0][i] = new Fraction(0, 1);
            }

            result = new Matrix(tmp);
        } else {
            result = multiply(inverseIndentityMinusQ, matrixR);
        }

        Fraction[] resultRow = result.cells[0];
        int resultRowSize = resultRow.length;
        int lcm = (int) getLcm(resultRow);
        int[] finalResult = new int[resultRowSize + 1];

        for (int i = 0; i < resultRowSize; i++) {
            finalResult[i] = (int) (resultRow[i].a * (lcm / resultRow[i].b));
        }

        finalResult[resultRowSize] = lcm;
        return finalResult;
    }

    private static long getLcm(Fraction[] row) {
        int n = row.length;
        long lcm = row[0].b;

        for (int i = 1; i < n; i++) {
            lcm = lcm(lcm, row[i].b);
        }

        return lcm;
    }

    private static Matrix multiply(Matrix matrix1, Matrix matrix2) {
        int s1 = matrix1.cells.length;
        int s2 = matrix2.cells[0].length;

        Fraction[][] result = new Fraction[s1][s2];

        for (int i = 0; i < s1; i++) {
            for (int j = 0; j < s2; j++) {
                result[i][j] = new Fraction(0, 1);

                for (int k = 0; k < s1; k++) {
                    Fraction tmp = multiply(matrix1.cells[i][k], matrix2.cells[k][j]);
                    result[i][j].add(tmp);
                }
            }
        }

        return new Matrix(result);
    }

    private static Matrix calcInverse(Matrix matrix) {
        int n = matrix.cells.length;
        Fraction determinant = calcDeterminant(matrix, n);
        Matrix adjMatrix = getAdjMatrix(matrix);
        Fraction[][] result = new Fraction[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = adjMatrix.cells[i][j].copyFraction();
                result[i][j].divide(determinant);
            }
        }

        return new Matrix(result);
    }

    private static Matrix getAdjMatrix(Matrix matrix) {
        int n = matrix.cells.length;
        Fraction[][] result = new Fraction[n][n];

        if (n == 1) {
            result[0][0] = new Fraction(1, 1);
            return new Matrix(result);
        }

        int sign;
        Fraction[][] tmpFractions = new Fraction[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(tmpFractions[i], new Fraction(1, 1));
        }

        Matrix tmp = new Matrix(tmpFractions);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                calcCofactor(matrix, tmp, i, j, n);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                result[j][i] = calcDeterminant(tmp, n - 1);
                result[j][i].multiply(new Fraction(sign, 1));
            }
        }

        return new Matrix(result);
    }

    private static void calcCofactor(Matrix matrix, Matrix tmp, int i, int j, int n) {
        int curr = 0;

        for (int ii = 0; ii < n; ii++) {
            for (int jj = 0; jj < n; jj++) {
                if (ii == i || jj == j) {
                    continue;
                }

                tmp.cells[curr / (n - 1)][curr % (n - 1)] = matrix.cells[ii][jj].copyFraction();
                curr++;
            }
        }
    }

    private static Fraction multiply(Fraction a, Fraction b) {
        Fraction aCopy = new Fraction(a.a, a.b);
        aCopy.multiply(b);
        return aCopy;
    }

    private static Fraction calcDeterminant(Matrix matrix, int n) {
        if (n == 1) {
            return matrix.cells[0][0].copyFraction();
        }

        Fraction result = new Fraction(1, 1);
        Matrix tmp = matrix.copyMatrix();

        for (int i = 0; i < n; i++) {
            int k = i;

            for (int j = i + 1; j < n; j++) {
                Fraction cellJ = tmp.cells[j][i].copyFraction();
                Fraction cellK = tmp.cells[k][i].copyFraction();

                if (Math.abs(cellJ.a / 1.0 / cellJ.b) > Math.abs(cellK.a / 1.0 / cellK.b)) {
                    k = j;
                }
            }

            if (tmp.cells[k][i].a == 0) {
                result = new Fraction(0, 1);
                break;
            }

            swap(tmp.cells, i, k);

            if (i != k) {
                result.a = -result.a;
            }

            result.multiply(tmp.cells[i][i]);

            for (int j = i + 1; j < n; j++) {
                tmp.cells[i][j].divide(tmp.cells[i][i]);
            }

            for (int j = 0; j < n; j++) {
                if (i != j && tmp.cells[j][i].a != 0) {
                    for (int kk = i + 1; kk < n; kk++) {
                        Fraction tmpFraction = multiply(tmp.cells[i][kk], tmp.cells[j][i]);
                        tmp.cells[j][kk].subtract(tmpFraction);
                    }
                }
            }
        }

        return result;
    }

    private static Matrix getIdentityMatrix(int n) {
        Fraction[][] result = new Fraction[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int a = (i == j) ? 1 : 0;
                result[i][j] = new Fraction(a, 1);
            }
        }

        return new Matrix(result);
    }

    private static Matrix convertMatrix(int[][] m, int absorbedStatesCount) {
        int n = m.length;
        Fraction[][] result = new Fraction[n][n];

        for (int i = 0; i < n - absorbedStatesCount; i++) {
            int sum = sum(m[i]);

            for (int j = 0; j < n; j++) {
                result[i][j] = new Fraction(m[i][j], sum);
            }
        }

        for (int i = n - absorbedStatesCount; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = new Fraction(m[i][j], 1);
            }
        }

        return new Matrix(result);
    }

    private static int sum(int[] row) {
        int res = 0;

        for (int x : row) {
            res += x;
        }

        return res;
    }

    private static int countAndMoveAbsorbedStatesToTheEnd(int[][] m) {
        int n = m.length;
        int curr = 0;

        for (int i = 0; i < n; i++) {
            if (!isAbsorbed(m, i)) {
                if (curr != i) {
                    for (int j = i; j > curr; j--) {
                        swap(m, j, j - 1);

                        for (int[] row : m) {
                            swap(row, j, j - 1);
                        }
                    }
                }

                curr++;
            }
        }

        return n - curr;
    }

    private static boolean isAbsorbed(int[][] m, int i) {
        boolean absorbed = true;

        for (int x : m[i]) {
            if (x != 0) {
                absorbed = false;
                break;
            }
        }

        if (absorbed) {
            m[i][i] = 1;
        }

        return absorbed;
    }

    private static void swap(int[][] m, int i, int j) {
        int[] tmp = m[i];
        m[i] = m[j];
        m[j] = tmp;
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }

    private static <T> void swap(T[][] m, int i, int j) {
        T[] tmp = m[i];
        m[i] = m[j];
        m[j] = tmp;
    }

    public static void main(String[] args) {
        int[][] m = new int[][]{
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 1, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

/*
        int[][] m = new int[][]{
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 1, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
*/

/*
        int[][] m = new int[][]{
                {0, 7, 0, 17, 0, 1, 0, 5, 0, 2},
                {0, 0, 29, 0, 28, 0, 3, 0, 16, 0},
                {0, 3, 0, 0, 0, 1, 0, 0, 0, 0},
                {48, 0, 3, 0, 0, 0, 17, 0, 0, 0},
                {0, 6, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        };
*/

/*
        int[][] m = new int[][]{
                {0, 0, 12, 0, 15, 0, 0, 0, 1, 8},
                {0, 0, 60, 0, 0, 7, 13, 0, 0, 0},
                {0, 15, 0, 8, 7, 0, 0, 1, 9, 0},
                {23, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {37, 35, 0, 0, 0, 0, 3, 21, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
*/

//        int[][] m = new int[][]{{0}};

/*
        int[][] m = new int[][]{
                {1, 2, 3, 0, 0, 0},
                {4, 5, 6, 0, 0, 0},
                {7, 8, 9, 1, 0, 0},
                {0, 0, 0, 0, 1, 2},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        };
*/

/*
        int[][] m = new int[][]{
                {0, 2, 1, 0, 0},
                {0, 0, 0, 3, 4},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}
        };
*/
        int[] result = solution(m);
        System.out.println(Arrays.toString(result));

//        countAndMoveAbsorbedStatesToTheEnd(m);
//        System.out.println(Arrays.toString(Arrays.stream(m).map(Arrays::toString).toArray()));
    }

/*
    private static int countAndMoveAbsorbedStatesToTheEnd(int[][] m) {
        int n = m.length;
        int curr = 0;
        int[] indices = new int[n];

        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        for (int i = 0; i < n; i++) {
            if (!isAbsorbed(m, i)) {
                swap(m, curr, i);
                swap(indices, curr, i);

                if (curr != i) {
                    for (int[] row : m) {
                        swap(row, curr, i);
                    }
                }

                curr++;
            }
        }

        return n - curr;
    }
*/
}
