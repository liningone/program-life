package org.programlife.investment.test.stock;

import org.junit.Assert;
import org.junit.Test;
import org.programlife.investment.stock.data.KLineData;
import org.programlife.investment.stock.data.KLineDataUtils;

import java.util.ArrayList;
import java.util.List;

public class KLineDataUtilsTest {
    @Test
    public void testSearchNearestIndex() {
        List<KLineData> list = new ArrayList<>();
        KLineData data = new KLineData();

        data = new KLineData();
        data.setTime("2023-05-10");
        list.add(data);

        data = new KLineData();
        data.setTime("2023-05-12");
        list.add(data);

        data = new KLineData();
        data.setTime("2023-05-14");
        list.add(data);

        Assert.assertEquals(2, KLineDataUtils.searchNearestIndex(list, "2023-05-13", true));
        Assert.assertEquals(1, KLineDataUtils.searchNearestIndex(list, "2023-05-13", false));

        Assert.assertEquals(0, KLineDataUtils.searchNearestIndex(list, "2023-04-08", true));
        Assert.assertEquals(0, KLineDataUtils.searchNearestIndex(list, "2023-05-08", true));
        Assert.assertEquals(0, KLineDataUtils.searchNearestIndex(list, "2023-05-10", true));
        Assert.assertEquals(1, KLineDataUtils.searchNearestIndex(list, "2023-05-11", true));
        Assert.assertEquals(1, KLineDataUtils.searchNearestIndex(list, "2023-05-12", true));
        Assert.assertEquals(2, KLineDataUtils.searchNearestIndex(list, "2023-05-15", true));

        List<KLineData> result = KLineDataUtils.filter(list,"2023-05-13", "2023-05-13");
        Assert.assertEquals(0, result.size());

        result = KLineDataUtils.filter(list,"2023-05-11", "2023-05-13");
        Assert.assertEquals(1, result.size());
        data = result.get(0);
        Assert.assertEquals("2023-05-12", data.getTime());

        result = KLineDataUtils.filter(list,"2023-05-11", "2023-05-15");
        Assert.assertEquals(2, result.size());
        data = result.get(0);
        Assert.assertEquals("2023-05-12", data.getTime());
        data = result.get(1);
        Assert.assertEquals("2023-05-14", data.getTime());

        result = KLineDataUtils.filter(list,"2023-05-11", "2023-05-13");
        Assert.assertEquals(1, result.size());
        data = result.get(0);
        Assert.assertEquals("2023-05-12", data.getTime());
    }
}
