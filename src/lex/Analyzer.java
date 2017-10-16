package lex;

import util.ReadHelper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * 分析器，作用是将RE转换为DFA table
 */
public class Analyzer {
    // .l文件位置
    private static final String CONFIG_PATH = "re.l";

    // 记录边的图
    HashMap<Integer, List<Side>> sides;

    public Analyzer(){
        sides = new HashMap<>();
    }

    /**
     * 将.l中的正则表达式转换为一个NFA
     */
    public void reToNFA(){
        List<String> express = ReadHelper.readLFile(CONFIG_PATH);
        System.out.println(express);
        for (int i = 1; i <= 5; i++) {
            System.out.println(Type.intToType(i));
        }
    }


    //向图中加入边的辅助方法
    private void putSide(int from, int to, BasicType type){
        if(sides.containsKey(from)){
            sides.get(to).add(new Side(to, type));
        }
        else {
            List<Side> list = new LinkedList<>();
            list.add(new Side(to, type));
            sides.put(from,list);
        }
    }
}
