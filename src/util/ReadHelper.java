package util;

import lex.Type;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public class ReadHelper {
    public static List<String> readLFile(String fileName){
        StringHelper stringHelper = new StringHelper();
        ArrayList<String> result = new ArrayList<>();
        try {
            BufferedReader reader =new BufferedReader(new FileReader(new File(fileName)));
            String re = "";
            while((re = reader.readLine()) != null){
                if(re.startsWith("$")){
                    String[] express = re.substring(1).split("=");
                    Type.putRe(express[0].trim());
                    result.add(stringHelper.changeExpress(express[1].trim()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

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

    public static int[][] readTable(String fileName){
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return table;
    }

}
