package org.programlife.investment.stock.calculation;

import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.programlife.investment.stock.util.DateUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

//https://www.codeproject.com/Tips/461049/Internal-Rate-of-Return-IRR-Calculation
public class IRRCalculator {
    /*
   cashFlows
       正数表示卖出的钱
       负数表示买入的钱
   dates
       对应的时间
       示例：2023-05-07
    */
    public static double calculateAnnualIRR(double[] cashFlows, String[] dates) {
        double[] times = new double[dates.length];
        LocalDate startTime = DateUtils.parseLocalDate(dates[0], DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
        for (int i = 0; i < dates.length; i ++) {
            LocalDate targetTime = DateUtils.parseLocalDate(dates[i], DateUtils.DEFAULT_SIMPLE_DATE_FORMAT);
            long daysBetween = ChronoUnit.DAYS.between(startTime, targetTime);
            times[i] = daysBetween / 365d;
        }

        return calculateAnnualIRR(cashFlows, times);
    }

    /*
    cashFlows
        正数表示卖出的钱
        负数表示买入的钱
    times
        单位：年
     */
    public static double calculateAnnualIRR(double[] cashFlows, double[] times) {
        // 使用BrentSolver求解IRR
        BrentSolver solver = new BrentSolver();
        IRRFunction irrFunction = new IRRFunction(cashFlows, times);

        // 假设IRR在-100%到100%之间
        double irr = solver.solve(10000, irrFunction, -0.9999, 100);

        return irr;
    }
}

class IRRFunction implements org.apache.commons.math3.analysis.UnivariateFunction {
    private final double[] cashFlows;
    private final double[] times;

    public IRRFunction(double[] cashFlows, double[] times) {
        this.cashFlows = cashFlows;
        this.times = times;
    }

    @Override
    public double value(double rate) {
        double npv = 0.0;
        for (int i = 0; i < cashFlows.length; i++) {
            npv += cashFlows[i] / Math.pow(1 + rate, times[i]);
        }
        return npv;
    }
}
