package util;

import java.util.HashMap;
import java.util.Stack;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public class StringHelper {
    HashMap<Character, Integer> priority;
    public StringHelper(){
        priority = new HashMap<>();
        priority.put('s', 0);
        priority.put('~', 1);
        priority.put('|', 2);
        priority.put('*', 3);
    }

    /**
     * 中缀表达式转换为后缀表达式
     * @param express 中缀表达式
     * @return 后缀表达式
     */
    public String changeExpress(String express){
        String result = "";
        Stack<Character> op = new Stack<>();
        op.push('s');

        for (char c : express.toCharArray()){
            if(c == '('){
                op.push(c);
            }
            else if(c == ')'){
                while(op.peek() != '('){
                    result += op.pop();
                }
                op.pop();
            }
            else if(priority.containsKey(c)){
                while(op.peek() != '(' && priority.get(op.peek()) > priority.get(c)){
                    result += op.pop();
                }
                op.push(c);
            }
            else{
                result += c;
            }
        }

        while(op.peek() != 's'){
            result += op.pop();
        }

        return result;
    }
}
