package monitor;

import util.Type;

import java.io.*;
import java.util.List;
import java.util.Scanner;

/**
 * 帮助读取文件的工具类
 */
class IOHelper {
    public static String readInput(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return "";
        }
    }

    public static int[][] readTable(String fileName) {
        File file = new File(fileName);
        int n, m;
        int[][] table = new int[1][1];

        try {
            Scanner scanner = new Scanner(file);
            n = scanner.nextInt();
            m = scanner.nextInt();
            table = new int[n][m];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    table[i][j] = scanner.nextInt();
                }
            }

            while (scanner.hasNext()) {
                String next = scanner.next();
                if (next.equals("@")) {
                    break;
                }
                Type.putRe(next);
            }

            while (scanner.hasNext()) {
                Type.putKeywords(scanner.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return table;
    }

    /**
     * 将生成的token序列写入.output文件
     *
     * @param outputPath .output文件路径
     * @param tokens     token序列
     */
    public static void writeOutput(String outputPath, List<Token> tokens) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(new File(outputPath));
            for (Token token : tokens) {
                writer.write(token.toString() + '\n');
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
