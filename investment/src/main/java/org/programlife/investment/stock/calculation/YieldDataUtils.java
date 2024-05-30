package org.programlife.investment.stock.calculation;

import java.util.List;

public class YieldDataUtils {

    //最大盈利
    public static YieldData findMaxProfit(List<YieldData> list) {
        YieldData res = list.get(0);
        for (YieldData data : list) {
            if (data.profit > res.profit) {
                res = data;
            }
        }
        return res;
    }

    //最大盈利率
    public static YieldData findMaxProfitPercentage(List<YieldData> list) {
        YieldData res = list.get(0);
        for (YieldData data : list) {
            if (data.returnRate > res.returnRate) {
                res = data;
            }
        }
        return res;
    }

    //最小盈利
    public static YieldData findMinProfit(List<YieldData> list) {
        YieldData res = list.get(0);
        for (YieldData data : list) {
            if (data.profit < res.profit) {
                res = data;
            }
        }
        return res;
    }

    //最小盈利率
    public static YieldData findMinProfitPercentage(List<YieldData> list) {
        YieldData res = list.get(0);
        for (YieldData data : list) {
            if (data.returnRate < res.returnRate) {
                res = data;
            }
        }
        return res;
    }

}
