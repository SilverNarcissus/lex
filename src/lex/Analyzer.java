package lex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public class Analyzer {
    HashMap<Integer, List<Side>> sides;

    public Analyzer(){
        sides = new HashMap<>();
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
