package org.programlife.investment.stock.data;

import java.util.ArrayList;
import java.util.List;

public class KLineDataUtils {
    public static List<KLineData> filter(List<KLineData> list, long startTimeMills, long endTimeMills) {
        int startIdx = search(list, startTimeMills);
        int endIdx = search(list, endTimeMills);

        List<KLineData> res = new ArrayList();
        for (int i = startIdx; i <= endIdx; i ++) {
            res.add(list.get(i));
        }
        return res;
    }

    private static int search(List<KLineData> list, long timeMills) {
        String dateStr = DateUtils.parseDateStr(timeMills);
        dateStr = dateStr.substring(0, list.get(0).getTime().length());
        //2023-02-02 00:00:00 -> 2023-02-02

        KLineData data = null;
        int left = 0;
        int right = list.size() - 1;
        while(left < right) {
            int middle = (left + right) / 2;
            data = list.get(middle);
            int flag = data.getTime().compareTo(dateStr);
            if (flag > 0) {
                right = middle - 1;
            } else if (flag < 0) {
                left = middle + 1;
            } else {
                left = middle;
                break;
            }
        }
        return left;
    }
}

