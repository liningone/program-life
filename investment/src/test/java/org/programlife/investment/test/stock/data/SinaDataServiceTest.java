package org.programlife.investment.test.stock.data;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.data.local.LocalDataService;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.internet.sina.SinaDataService;

import java.util.List;

public class SinaDataServiceTest {
    private SinaDataService dataService = new SinaDataService();

    @Test
    public void testQueryClosingPrice() {
        String startTime = "2024-01-01 00:00:00";
        String endTime = "2024-02-01 00:00:00";

        List<KLineData> sinaRes = dataService.queryKLineData("399989.SZ", 240, 0, startTime, endTime);
        Assert.assertEquals(23, sinaRes.size());

        LocalDataService localDataService = new LocalDataService();
        List<KLineData> localRes = localDataService.queryKLineData("399989.SZ", 240, 0, startTime, endTime);
        Assert.assertEquals(23, localRes.size());

        for (int i = 0; i < sinaRes.size(); i ++) {
            KLineData source = sinaRes.get(i);
            KLineData target = localRes.get(i);
            Assert.assertEquals(source.getTime(), target.getTime());
            Assert.assertEquals(source.getClose(), target.getClose(), 0);
            Assert.assertEquals(source.getOpen(), target.getOpen(), 0);
            Assert.assertEquals(source.getHigh(), target.getHigh(), 0);
            Assert.assertEquals(source.getLow(), target.getLow(), 0);
        }
    }
}
