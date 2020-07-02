package com.katsman.graphlib;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Alexey Katsman
 * @since 5/30/20
 */

public class E {

    private static final int SCALE = 10001;

    public static BigDecimal sqrt(BigDecimal number) {
        BigDecimal result;
        if (number.compareTo(BigDecimal.ZERO) == 0 ||
                number.compareTo(BigDecimal.ONE) == 0) {
            result = number;
        } else if (number.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException();
        } else {
            BigInteger integerValue = number.movePointRight(SCALE << 1).toBigInteger();
            int bits = (integerValue.bitLength() + 1) >> 1;
            BigInteger firstVar = integerValue.shiftRight(bits);
            BigInteger secondVar;
            do {
                secondVar = firstVar;
                firstVar = firstVar.add(integerValue.divide(firstVar))
                        .shiftRight(1);
            } while (firstVar.compareTo(secondVar) != 0);
            result = new BigDecimal(firstVar, SCALE);
        }
        return result;
    }

    private BigInteger solve(BigInteger x) {
        BigInteger ans = BigInteger.ZERO;
        int sign = 1;

        while (!x.equals(BigInteger.ZERO)) {
            BigInteger newCurr = sqrt(new BigDecimal(2)).subtract(BigDecimal.ONE).multiply(new BigDecimal(x)).toBigInteger();
            BigInteger tmp = BigInteger.ZERO;
            tmp = tmp.add(x.multiply(newCurr));
            tmp = tmp.add(x.multiply(x.add(BigInteger.ONE)).divide(BigInteger.valueOf(2)));
            tmp = tmp.subtract(newCurr.multiply(newCurr.add(BigInteger.ONE)).divide(BigInteger.valueOf(2)));

            if (sign == 1) {
                ans = ans.add(tmp);
            } else {
                ans = ans.subtract(tmp);
            }

            sign = -sign;
            x = newCurr;
        }

        return ans;
    }

    public String solution(String s) {
        return solve(new BigInteger(s)).toString();
    }

    public static void main(String[] args) {
        E e = new E();
        System.out.println(e.solution("77"));
    }
}
