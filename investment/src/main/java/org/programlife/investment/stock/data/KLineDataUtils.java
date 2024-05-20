package org.programlife.investment.stock.data;

import java.util.ArrayList;
import java.util.List;

public class KLineDataUtils {
    public static List<KLineData> filter(List<KLineData> list, String startTime, String endTime) {
        int startIdx = searchNearestIndex(list, startTime, true);
        int endIdx = searchNearestIndex(list, endTime, false);

        if (startIdx > endIdx) {
            return new ArrayList<>();
        }

        return list.subList(startIdx, endIdx + 1);
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

    public static KLineData search(List<KLineData> list, String dateStr) {
        dateStr = dateStr.substring(0, list.get(0).getTime().length()); //2023-02-02 00:00:00 -> 2023-02-02

        int idx = searchNearestIndex(list, dateStr, true);
        KLineData data = list.get(idx);
        if (data.getTime().equals(dateStr)) {
            return data;
        }

        return null;
    }

    /*
    如果list中有等于dateStr，则直接返回
    如果list中没有等于dateStr
        biggerThanTargetDate = [true] 要求查询结果大于dateStr
        biggerThanTargetDate = [false] 要求查询结果小于dateStr
     */
    public static int searchNearestIndex(List<KLineData> list, String dateStr, boolean biggerThanTargetDate) {
        dateStr = dateStr.substring(0, list.get(0).getTime().length()); //2023-02-02 00:00:00 -> 2023-02-02

        KLineData data = null;
        int left = 0;
        int right = list.size() - 1;
        while(left <= right) {
            int middle = (left + right) / 2;
            data = list.get(middle);
            int flag = data.getTime().compareTo(dateStr);
            if (flag > 0) {
                right = middle - 1;
            } else if (flag < 0) {
                left = middle + 1;
            } else {
                return middle;
            }
        }

        if (left == list.size()) {
            return left - 1;
        }

        // left target right
        if (biggerThanTargetDate) {
            return Math.max(left, right);
        } else {
            return Math.min(left, right);
        }
    }

}

