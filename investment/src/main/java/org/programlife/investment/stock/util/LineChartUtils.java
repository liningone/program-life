package org.programlife.investment.stock.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LineChartUtils {

    public static void saveChartAsPNG(Map<Integer, int[]> dataMap) {
        // 创建一个数据集
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(Integer key : dataMap.keySet()) {
            int[] values = dataMap.get(key);
            for (int i = 0; i < values.length; i ++) {
                dataset.addValue(values[i], key.toString() + "个月",  i * 5 + "%");
            }
        }

        StandardChartTheme theme = createChartTheme(null);
        ChartFactory.setChartTheme(theme);

        // 使用 ChartFactory 创建折线图
        JFreeChart chart = ChartFactory.createLineChart(
                "定投收益百分位", // 图表标题
                "百分位", // X 轴标签
                "收益", // Y 轴标签
                dataset // 数据集
        );

        // 设置抗锯齿，防止字体显示不清楚
        chart.setTextAntiAlias(false);

        // 保存图表为 PNG 图片
        saveChartAsPNG(chart, "line_chart_example.png", 1200, 800);
    }

    /**
     * 生成主题
     *
     * @param fontName 字体名称（默认为宋体）
     * @return
     */
    private static StandardChartTheme createChartTheme(String fontName) {
        StandardChartTheme theme = new StandardChartTheme("unicode") {
            public void apply(JFreeChart chart) {
                chart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                super.apply(chart);
            }
        };
        fontName = fontName == null ? "宋体" : fontName;
        theme.setExtraLargeFont(new Font(fontName, Font.PLAIN, 20));
        theme.setLargeFont(new Font(fontName, Font.PLAIN, 14));
        theme.setRegularFont(new Font(fontName, Font.PLAIN, 12));
        theme.setSmallFont(new Font(fontName, Font.PLAIN, 10));
        return theme;
    }

    // 保存图表为 PNG 图片
    private static void saveChartAsPNG(JFreeChart chart, String fileName, int width, int height) {
        File file = new File(fileName);
        try {
            ChartUtils.saveChartAsPNG(file, chart, width, height);
            System.out.println("Chart saved as: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}