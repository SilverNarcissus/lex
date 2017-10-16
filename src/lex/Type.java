package lex;

import java.util.ArrayList;

/**
 * Created by SilverNarcissus on 2017/10/16.
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

}
