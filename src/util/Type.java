package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 该类表征了.l文件中规定的所有正则字符串的名称
 */
public class Type {
    private static Set<String> keywords = new HashSet<>();

    private static ArrayList<String> typeList = new ArrayList<>();

    private static int count = 1;

    /**
     * 添加正则表达式名
     * @param name 正则表达式名
     */
    public static void putRe(String name){
        typeList.add(name);
        count++;
    }

    /**
     * 添加keyword
     * @param keyword 关键字
     */
    public static void putKeywords(String keyword){
        keywords.add(keyword);
    }

    /**
     * 判断字符串是否是keyword
     * @param text 字符串
     * @return 字符串是否是keyword
     */
    public static boolean isKeyword(String text){
        return keywords.contains(text);
    }

    /**
     * 将类型码转换为类型字段
     * @param mark 类型码
     * @return 类型字段
     */
    public static String intToType(int mark){
        mark = Math.abs(mark);
        if(mark == count){
            return "ERROR";
        }
        return typeList.get(mark - 1);
    }



    public static int getNumber(){
        return count - 1;
    }

}
