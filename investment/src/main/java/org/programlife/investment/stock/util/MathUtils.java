package org.programlife.investment.stock.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    public static float formatFloat(float num, int length) {
        float f = (float) Math.pow(10f, length);
        return Math.round(num * f) / f;
    }

    public static double formatDouble(double num, int length) {
        double d = Math.pow(10f, length);
        return Math.round(num * d) / d;
    }

    public static double doubleDivide (double dividend, double divisor, int scale) {
        BigDecimal bd1 = new BigDecimal(Double.toString(dividend));
        BigDecimal bd2 = new BigDecimal(Double.toString(divisor));

        // 进行除法运算，并设置保留2位小数和舍入模式
        BigDecimal result = bd1.divide(bd2, scale, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    @Test
    public void test() {
        System.out.println(formatFloat(0.2333f, 2));

        double num = 999.999999d;
        System.out.println(num);
        System.out.println(formatDouble(num, 3));

        System.out.println(doubleDivide(1d, 3d, 3));

        double num2 = 1.299d + 1.569d;
        System.out.println(num2);
    }
}
