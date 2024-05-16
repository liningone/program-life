package org.programlife.investment.stock.data;

import java.util.ArrayList;
import java.util.List;

public class KLineDataUtils {
    public static List<KLineData> filter(List<KLineData> list, String startTime, String endTime) {
        int startIdx = search(list, startTime);
        int endIdx = search(list, endTime);

        List<KLineData> res = new ArrayList();
        for (int i = startIdx; i <= endIdx; i ++) {
            res.add(list.get(i));
        }
        return res;
    }

    private static int search(List<KLineData> list, String dateStr) {
        //String dateStr = DateUtils.parseDateStr(time);
        dateStr = dateStr.substring(0, list.get(0).getTime().length()); //2023-02-02 00:00:00 -> 2023-02-02


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

    public static List<KLineData> filter(List<KLineData> list, List<String> dates) {
        List<KLineData> res = new ArrayList();
        int idx = 0;
        for (KLineData data : list) {
            String time = dates.get(idx).substring(0, list.get(0).getTime().length());
            if (!time.equals(data.getTime())) {
                continue;
            }
            idx ++;
            res.add(data);
        }
        return res;
    }
}

