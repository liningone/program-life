package org.programlife.investment.test.stock.calculation;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.calculation.IRRCalculator;

public class IRRCalculatorTest {
    @Test
    public void test() {
        /*
        投资和现金流数据
        第0年买入100
        第3年卖出收入110
         */
        double[] cashFlows = {-100, 110}; // 示例现金流
        double[] times = {0, 3}; // 对应时间，以年为单位

        double annualIRR = IRRCalculator.calculateAnnualIRR(cashFlows, times);
        Assert.assertEquals(0.032, annualIRR, 0.01);

        double res = Math.abs(cashFlows[0]);
        for (int i = 1; i <= times[1]; i ++) {
            res = res + annualIRR * res;
        }

        Assert.assertEquals(cashFlows[1], res, 1);
    }

    @Test
    public void test2() {
        /*
        投资和现金流数据
        第0年买入100，第2年买入100，第三年买入100
        第3年卖出收入330
         */
        double[] cashFlows = {-100, -100, -100, 330}; // 示例现金流
        double[] times = {0, 2, 3, 4}; // 对应时间，以年为单位

        double annualIRR = IRRCalculator.calculateAnnualIRR(cashFlows, times);

        double[] arr = new double[cashFlows.length - 1];
        double res = 0;
        for (int i = 0; i < cashFlows.length - 1; i ++) {
            arr[i] = Math.abs(cashFlows[i]);
            res += arr[i];
        }
        for (int i = 1; i <= times[times.length - 1]; i ++) {
            for (int j = 0; j < arr.length; j ++) {
                if (times[j] >= i) {
                    break;
                }
                double profit = arr[j] * annualIRR;
                arr[j] += profit;
                res += profit;
            }
        }

        Assert.assertEquals(cashFlows[cashFlows.length - 1], res, 1);
    }

    @Test
    public void test3() {
        /*
        投资和现金流数据
        第0年买入100，
        第6个月卖出收入110
         */
        double[] cashFlows = {-100, 110}; // 示例现金流
        double[] times = {0, 0.5}; // 对应时间，以年为单位

        double annualIRR = IRRCalculator.calculateAnnualIRR(cashFlows, times);
        Assert.assertEquals(0.21, annualIRR, 0.01d);
    }

    @Test
    public void test4() {
        /*
        投资和现金流数据
        第0年买入100，
        第1年卖出收入110
         */
        double[] cashFlows = {-100, 110};
        String[] times = {"2023-01-01", "2024-01-01"};
        double annualIRR = IRRCalculator.calculateAnnualIRR(cashFlows, times);
        Assert.assertEquals(0.10, annualIRR, 0.01d);

        cashFlows = new double[]{-100, 90};
        times = new String[]{"2023-01-01", "2024-01-01"};
        annualIRR = IRRCalculator.calculateAnnualIRR(cashFlows, times);
        Assert.assertEquals(-0.10, annualIRR, 0.01d);
    }
}
