package org.programlife.investment.stock.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LineChartExample {

    public static void main(String[] args) {
        // 创建一个数据集
        DefaultCategoryDataset dataset = createDataset();

        // 使用 ChartFactory 创建折线图·
        JFreeChart chart = ChartFactory.createLineChart(
                "Line Chart Example", // 图表标题
                "X-Axis", // X 轴标签
                "Y-Axis", // Y 轴标签
                dataset // 数据集
        );

        // 保存图表为 PNG 图片
        saveChartAsPNG(chart, "line_chart_example.png", 800, 600);
    }

    // 创建一个示例数据集
    private static DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // 添加第一条折线的数据
        dataset.addValue(1.0, "Series1", "Category1");
        dataset.addValue(4.0, "Series1", "Category2");
        dataset.addValue(3.0, "Series1", "Category3");
        dataset.addValue(5.0, "Series1", "Category4");
        dataset.addValue(2.0, "Series1", "Category5");

        // 添加第二条折线的数据
        dataset.addValue(3.0, "Series2", "Category1");
        dataset.addValue(2.0, "Series2", "Category2");
        dataset.addValue(5.0, "Series2", "Category3");
        dataset.addValue(2.0, "Series2", "Category4");
        dataset.addValue(0.0, "Series2", "Category5");

        return dataset;
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

    public static void saveChartAsPNG(Map<Integer, int[]> dataMap) {
        // 创建一个数据集
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for(Integer key : dataMap.keySet()) {
            int[] values = dataMap.get(key);
            for (int i = 0; i < values.length; i ++) {
                dataset.addValue(values[i], key.toString() + "Month", "percentile " + i);
            }
        }

        // 使用 ChartFactory 创建折线图
        JFreeChart chart = ChartFactory.createLineChart(
                "Line Chart Example", // 图表标题
                "percentile", // X 轴标签
                "profit", // Y 轴标签
                dataset // 数据集
        );

        // 保存图表为 PNG 图片
        saveChartAsPNG(chart, "line_chart_example.png", 1000, 800);
    }
}