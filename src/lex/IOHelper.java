package lex;

import util.Type;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 帮助读取文件和生成.t文件的工具类
 */
class IOHelper {
    static String[] keywords;

    /**
     * 读取.l文件
     *
     * @param fileName .l文件路径
     * @return 后缀正则表达式列表
     */
    public static List<String> readLFile(String fileName) {
        StringHelper stringHelper = new StringHelper();
        ArrayList<String> result = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
            String re = "";
            while ((re = reader.readLine()) != null) {
                if (re.startsWith("$")) {
                    String[] express = re.substring(1).split("=");
                    Type.putRe(express[0].trim());
                    result.add(stringHelper.changeExpress(express[1].trim()));
                }
                if (re.startsWith("%")) {
                    keywords = re.substring(1).split(",");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    /**
     * 创建.t表文件
     *
     * @param table 表数组
     * @param line  表的行数
     * @param cross 表的列数
     * @param path  .t表文件的路径
     */
    public static void buildTableFile(int[][] table, int line, int cross, String path, List<Integer> deletedLine) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(path));
            writer.write((line - deletedLine.size()) + " " + cross + "\n");
            for (int i = 0; i < line; i++) {
                if (!deletedLine.contains(i)) {
                    for (int j = 0; j < cross; j++) {
                        writer.write(table[i][j] + " ");
                    }
                    writer.write("\n");
                }
            }

            for (int i = 0; i < Type.getNumber(); i++) {
                writer.write(Type.intToType(-i - 1) + " ");
            }
            writer.write('\n');
            writer.write("@ ");
            for (String keyword : keywords) {
                writer.write(keyword.trim() + " ");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }
}
