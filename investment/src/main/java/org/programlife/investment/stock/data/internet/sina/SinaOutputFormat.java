package org.programlife.investment.stock.data.internet.sina;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.programlife.investment.stock.data.KLineData;

import java.util.ArrayList;
import java.util.List;

public class SinaOutputFormat {
    public static List<KLineData> convertToSeries(String jsonResponse) {
        Gson gson = new Gson();
        List<SinaKLineDataModel> list = gson.fromJson(jsonResponse, new TypeToken<List<SinaKLineDataModel>>(){}.getType());

        List<KLineData> res = new ArrayList();
        for (SinaKLineDataModel data : list) {
            res.add(convert(data));
        }
        return res;
    }

    public static KLineData convert(SinaKLineDataModel data) {
        KLineData res = new KLineData();
        res.setTime(data.getDay());
        res.setOpen(doubleStr2Int(data.getOpen()));
        res.setLow(doubleStr2Int(data.getLow()));
        res.setHigh(doubleStr2Int(data.getHigh()));
        res.setClose(doubleStr2Int(data.getClose()));
        res.setVolume(Long.parseLong(data.getVolume()));
        return res;
    }

    private static int doubleStr2Int(String data) {
        return (int)Double.parseDouble(data);
    }

    public static void main(String[] args) {
        String jsonResponse = "[{\"day\":\"2024-04-24\",\"open\":\"3029.403\",\"high\":\"3045.640\",\"low\":\"3019.124\",\"close\":\"3044.822\",\"volume\":\"30526409300\"},{\"day\":\"2024-04-25\",\"open\":\"3037.927\",\"high\":\"3060.264\",\"low\":\"3034.650\",\"close\":\"3052.900\",\"volume\":\"29228014800\"},{\"day\":\"2024-04-26\",\"open\":\"3054.979\",\"high\":\"3092.430\",\"low\":\"3054.979\",\"close\":\"3088.636\",\"volume\":\"43938256300\"},{\"day\":\"2024-04-29\",\"open\":\"3086.682\",\"high\":\"3119.686\",\"low\":\"3080.586\",\"close\":\"3113.043\",\"volume\":\"50692544500\"},{\"day\":\"2024-04-30\",\"open\":\"3110.159\",\"high\":\"3123.288\",\"low\":\"3104.294\",\"close\":\"3104.825\",\"volume\":\"44021321300\"},{\"day\":\"2024-05-06\",\"open\":\"3132.512\",\"high\":\"3142.382\",\"low\":\"3126.769\",\"close\":\"3140.720\",\"volume\":\"45065689900\"},{\"day\":\"2024-05-07\",\"open\":\"3139.664\",\"high\":\"3151.219\",\"low\":\"3133.690\",\"close\":\"3147.738\",\"volume\":\"37910256400\"},{\"day\":\"2024-05-08\",\"open\":\"3141.487\",\"high\":\"3144.986\",\"low\":\"3126.173\",\"close\":\"3128.480\",\"volume\":\"33400032000\"},{\"day\":\"2024-05-09\",\"open\":\"3128.164\",\"high\":\"3158.465\",\"low\":\"3128.164\",\"close\":\"3154.320\",\"volume\":\"36088146200\"},{\"day\":\"2024-05-10\",\"open\":\"3158.589\",\"high\":\"3163.144\",\"low\":\"3137.152\",\"close\":\"3154.547\",\"volume\":\"38428749500\"}]";
        List<KLineData> res = new SinaOutputFormat().convertToSeries(jsonResponse);
        String str = new JSONArray(res).toString();
        System.out.println(str);

        Gson gson = new Gson();
        List<KLineData> list = gson.fromJson(str, new TypeToken<List<KLineData>>(){}.getType());
        //System.out.println(list);
    }

}
