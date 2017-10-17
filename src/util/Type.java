package util;

import java.util.ArrayList;

/**
 * 该类表征了.l文件中规定的所有正则字符串的名称
 */
public class Type {
    private static ArrayList<String> typeList = new ArrayList<>();

    private static int count = 1;

    public static void putRe(String name){
        typeList.add(name);
        count++;
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
