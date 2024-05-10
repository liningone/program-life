package org.programlife.investment.test.stock;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.data.DateUtils;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.local.LocalDataService;

import java.sql.Timestamp;
import java.util.List;

public class LocalDataServiceTest {
    private LocalDataService dataService = new LocalDataService();

    @Test
    public void testQueryClosingPrice() {
        long startTimeMills = DateUtils.parseMills("2023-01-01 00:00:00");
        long endTimeMills = DateUtils.parseMills("2023-12-30 00:00:00");

        List<KLineData> res = dataService.queryKLineData("000300.SH", 240, 0, startTimeMills, endTimeMills);
        Assert.assertEquals(242, res.size());
        KLineData data = res.get(0);
        Assert.assertEquals("2023-01-03", data.getTime());
        Assert.assertEquals(3887, data.getClose());
        data = res.get(res.size() - 1);
        Assert.assertEquals("2023-12-29", data.getTime());
        Assert.assertEquals(3431, data.getClose());

        startTimeMills = DateUtils.parseMills("2022-01-01 00:00:00");
        endTimeMills = DateUtils.parseMills("2023-12-30 00:00:00");
        res = dataService.queryKLineData("000300.SH", 240, 0, startTimeMills, endTimeMills);
        Assert.assertEquals(484, res.size());
        data = res.get(0);
        Assert.assertEquals("2022-01-04", data.getTime());
        Assert.assertEquals(4917, data.getClose());

        startTimeMills = DateUtils.parseMills("2023-02-02 00:00:00");
        endTimeMills = DateUtils.parseMills("2024-02-01 00:00:00");
        res = dataService.queryKLineData("000300.SH", 240, 0, startTimeMills, endTimeMills);
        Assert.assertEquals(248, res.size());
        data = res.get(0);
        Assert.assertEquals("2023-02-02", data.getTime());
        Assert.assertEquals(4181, data.getClose());
        data = res.get(res.size() - 1);
        Assert.assertEquals("2024-02-01", data.getTime());
        Assert.assertEquals(3217, data.getClose());
    }

    @Test
    public void testQueryPrice() {
        LocalDataService dataService = new LocalDataService();
        long startTimeMills = DateUtils.parseMills("2021-09-13");
        long endTimeMills = DateUtils.parseMills("2024-05-10");
        //long endTimeMills = DateUtils.parseMills("2021-11-01");

        List<KLineData> res = dataService.queryKLineData("000001.SH", 240, 0, startTimeMills, endTimeMills);
        int start = 0;
        int cost = 0;
        int times = 0;
        long sum = 0;
        for (int i = 0; i < res.size(); i ++) {
            times ++;
            sum += res.get(i).getClose();
        }
        System.out.println("平均=" + sum / times);

        cost = 0;
        times = 0;
        sum = 0;
        for (int i = 0; i < res.size() - 5; i += 5) {
            cost += 100;
            times ++;
            sum += res.get(i).getClose();
        }
        long ava = sum / times;
        System.out.println("成本=" + ava);
        int end = res.get(res.size() - 1).getClose();
        System.out.println("当前=" + end);
        double d = (end - ava) / (1.0 * ava);
        d = (double) Math.round(d * 1000) / 1000;
        System.out.println(d + ",lost=" + d * cost);
        System.out.println(cost);
    }
}
