package org.programlife.investment.test.stock;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.util.DateUtils;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.local.LocalDataService;

import java.util.ArrayList;
import java.util.List;

public class LocalDataServiceTest {
    private LocalDataService dataService = new LocalDataService();

    @Test
    public void testQueryClosingPrice() {
        String startTime = "2023-01-01 00:00:00";
        String endTime = "2023-12-30 00:00:00";

        List<KLineData> res = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);
        Assert.assertEquals(242, res.size());
        KLineData data = res.get(0);
        Assert.assertEquals("2023-01-03", data.getTime());
        Assert.assertEquals(3887, data.getClose());
        data = res.get(res.size() - 1);
        Assert.assertEquals("2023-12-29", data.getTime());
        Assert.assertEquals(3431, data.getClose());

        startTime = "2022-01-01 00:00:00";
        endTime = "2023-12-30 00:00:00";
        res = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);
        Assert.assertEquals(484, res.size());
        data = res.get(0);
        Assert.assertEquals("2022-01-04", data.getTime());
        Assert.assertEquals(4917, data.getClose());

        startTime = "2023-02-02 00:00:00";
        endTime = "2024-02-01 00:00:00";
        res = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);
        Assert.assertEquals(248, res.size());
        data = res.get(0);
        Assert.assertEquals("2023-02-02", data.getTime());
        Assert.assertEquals(4181, data.getClose());
        data = res.get(res.size() - 1);
        Assert.assertEquals("2024-02-01", data.getTime());
        Assert.assertEquals(3217, data.getClose());
    }

    @Test
    public void testQueryPriceForSpecifiedTime() {
        List<String> dates = new ArrayList<>();
        dates.add(DateUtils.completeTime("2023-05-12"));
        dates.add(DateUtils.completeTime("2023-06-12"));
        dates.add(DateUtils.completeTime("2023-07-14"));

        List<KLineData> res = dataService.queryKLineData("000300.SH", 240, 0, dates);
        Assert.assertEquals(3, res.size());

        KLineData data = res.get(0);
        Assert.assertEquals("2023-05-12", data.getTime());
        Assert.assertEquals(3937, data.getClose());

        data = res.get(2);
        Assert.assertEquals("2023-07-14", data.getTime());
        Assert.assertEquals(3899, data.getClose());
    }
}
