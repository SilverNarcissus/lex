package lex;

import util.ReadHelper;

import java.util.*;

/**
 * 分析器，作用是将RE转换为DFA table
 */
public class Analyzer {
    // .l文件位置
    private static final String CONFIG_PATH = "re.l";

    // 记录边的图
    HashMap<Integer, List<Side>> sides;

    //
    int stateCount;


    public Analyzer() {
        sides = new HashMap<>();
        stateCount = 1;
    }

    /**
     * 将.l中的正则表达式转换为一个NFA
     */
    public void reToNFA() {
        List<String> express = ReadHelper.readLFile(CONFIG_PATH);
        int start = 0;
        System.out.println(express);
        for (int i = 0; i < 4; i++) {
            putSide(start, reToNFASingle(express.get(i)), BasicType.EMPTY);
        }

        System.out.println(sides);
    }

    private int reToNFASingle(String express) {
        Stack<Integer> nfa = new Stack<>();
        int start;
        int end;
        for (int i = 0; i < express.length(); i++) {
            switch (express.charAt(i)) {
                case '{':
                    i++;
                    String now = "";
                    while (express.charAt(i) != '}') {
                        now = now + express.charAt(i);
                        i++;
                    }
                    start = stateCount++;
                    end = stateCount++;
                    putSide(start,end,BasicType.fromStringToType(now));
                    nfa.push(end);
                    nfa.push(start);
                    break;
                case '*':
                    handleStar(nfa);
                    break;
                case '|':
                    handleOr(nfa);
                    break;
                case '~':
                    handleConnect(nfa);
                    break;
                default:
                    System.err.println("Error in RE!" + express.charAt(i));
            }
        }
        return nfa.pop();
    }

    /**
     * 处理~
     */
    private void handleConnect(Stack<Integer> nfa) {
        int start1;
        int end1;
        int start2;
        int end2;

        start1 = nfa.pop();
        end1 = nfa.pop();
        start2 =nfa.pop();
        end2 = nfa.pop();
        sides.put(end2, sides.get(start1));
        sides.remove(start1);

        nfa.push(end1);
        nfa.push(start2);
    }

    /**
     * 处理*
     */
    private void handleStar(Stack<Integer> nfa) {
        int start1;
        int end1;
        int newStart;
        int newEnd;

        start1 = nfa.pop();
        end1 = nfa.pop();
        newStart = stateCount++;
        newEnd = stateCount++;
        putSide(newStart,start1, BasicType.EMPTY);
        putSide(end1,newEnd,BasicType.EMPTY);
        putSide(newStart,newEnd,BasicType.EMPTY);
        putSide(end1,start1,BasicType.EMPTY);

        nfa.push(newEnd);
        nfa.push(newStart);
    }

    /**
     * 处理|
     */
    private void handleOr(Stack<Integer> nfa) {
        int start1;
        int end1;
        int start2;
        int end2;
        int newStart;
        int newEnd;

        start1 = nfa.pop();
        end1 = nfa.pop();
        start2 =nfa.pop();
        end2 = nfa.pop();
        newStart = stateCount++;
        newEnd = stateCount++;

        putSide(newStart, start1, BasicType.EMPTY);
        putSide(newStart, start2, BasicType.EMPTY);
        putSide(end1, newEnd, BasicType.EMPTY);
        putSide(end2, newEnd, BasicType.EMPTY);

        nfa.push(newEnd);
        nfa.push(newStart);
    }

    //向图中加入边的辅助方法
    private void putSide(int from, int to, BasicType type) {
        if (sides.containsKey(from)) {
            sides.get(from).add(new Side(to,type));
        } else {
            List<Side> line = new LinkedList<>();
            line.add(new Side(to, type));
            sides.put(from, line);
        }
    }
}
