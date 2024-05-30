package org.programlife.investment.test.stock.calculation;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.junit.Test;

public class PercentileTest {
    @Test
    public void testPercentile() {
        double[] arr = new double[10];
        for (int i = 0; i < arr.length; i ++) {
            arr[i] = i + 1;
        }
        Percentile percentile = new Percentile();
        System.out.println(percentile.evaluate(arr, 25));
        //System.out.println(percentile.evaluate(arr, 10));
        //System.out.println(percentile.evaluate(arr, 20));
        System.out.println(percentile.evaluate(arr, 50));
        //System.out.println(percentile.evaluate(arr, 80));
        //System.out.println(percentile.evaluate(arr, 90));
        System.out.println(percentile.evaluate(arr, 75));
    }

    @Test
    public void testDescriptiveStatistics () {
        double[] arr = new double[10];
        for (int i = 0; i < arr.length; i ++) {
            arr[i] = i + 1;
        }
        DescriptiveStatistics stats = new DescriptiveStatistics(arr);

        /*
        for (double value : arr) {
            stats.addValue(value);
        }
         */
        double percentile25 = stats.getPercentile(25);
        double percentile50 = stats.getPercentile(50); // 中位数
        double percentile75 = stats.getPercentile(75);
    }
}
