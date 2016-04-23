package com.zhanghangdsgmail.zhh.moneygowhere;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 先用File文件保存数据，每行代表一条数据
 */
public class DataStorage {

    public final static String TAG = "WRITE_WRONG";

    // 指向存储文件
    private static File file = new File(Environment.getExternalStorageDirectory()
            + File.separator + "myData.txt");

    /**
     * 向存储文件写入一行记录
     *
     * @param line 所要写入的记录
     */
    public static void writeLine(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            Log.d(TAG, "写入一行错误");
        }
        Log.i("FilePath", file.toString());

    }

    /**
     * 将一行记录分解成键值对。
     *
     * @param line 要解析的一行存储记录
     * @return 返回各项键值
     */
    public static Map<String, String> parseLine(String line) {
        Map<String, String> map = new HashMap<>();
        for (String i : line.split(" ")) {
            String[] strings = i.split("=");
            if (strings.length == 2) {
                map.put(strings[0], strings[1]);
            } else {
                map.put(strings[0], "");
            }
        }
        return map;
    }

    public static LinkedList<String> getLines() {
        LinkedList<String> list = new LinkedList<>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(
                    new FileReader(file))) {
                String string;
                while ((string = reader.readLine()) != null)
                    list.add(string);
            } catch (IOException e) {
                Log.d(TAG, "读入整个文件失败");
            }
        }

        return list;
    }
}
