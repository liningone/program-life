package org.programlife.investment.stock.data.local;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FileExample {
    public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
        lines.add("123");
        try {
            FileUtils.writeLines(new File("D:/test3.json"), "UTF-8", lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
