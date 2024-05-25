package org.programlife.investment.test.stock;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.util.DateUtils;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.internet.sina.SinaDataService;

import java.util.List;

public class SinaDataServiceTest {
    private SinaDataService dataService = new SinaDataService();

    @Test
    public void testQueryClosingPrice() {
        String startTime = "2024-01-01 00:00:00";
        String endTime = "2024-12-30 00:00:00";

        //List<KLineData> res = dataService.queryKLineData("000300.SH", 240, 0, startTime, endTime);

        List<KLineData> res = dataService.queryKLineData("399989.SZ", 240, 0, startTime, endTime);
        Assert.assertEquals(260, res.size());
    }
}
