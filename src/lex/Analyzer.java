package lex;

import util.BasicType;
import util.Type;

import java.util.*;

/**
 * 分析器，作用是将RE转换为DFA table
 */
class Analyzer {
    // .l文件位置
    private static final String CONFIG_PATH = "re.l";
    // 转换表文件位置
    private static final String TABLE_PATH = "table.t";
    // table
    private int[][] table;
    // line
    private int line;
    // 记录边的图
    private HashMap<Integer, List<Side>> sides;

    private HashMap<Integer, Integer> finalStates;

    //状态数
    private int stateCount;

    private static int ERROR_CODE;


    public Analyzer() {
        sides = new HashMap<>();
        finalStates = new HashMap<>();
        stateCount = 1;
        line = 0;
    }

    public void parse(){
        reToNFA();
        NFAToDFA();
        IOHelper.buildTableFile(table, line, BasicType.getNumber(), TABLE_PATH);
    }

    /**
     * 将生成的NFA换为一个DFA
     */
    private void NFAToDFA() {
        Set<Integer> start = new HashSet<>();
        start.add(0);
        start = findClosure(start);
        ArrayList<Set<Integer>> total = new ArrayList<>();
        total.add(start);

        Queue<Set<Integer>> stateQueue = new LinkedList<>();
        stateQueue.add(start);

        while (!stateQueue.isEmpty()) {
            Set<Integer> state = stateQueue.remove();
            //对各种发出边做到达闭包
            for (BasicType type : BasicType.values()) {
                if(type == BasicType.EMPTY){
                    break;
                }
                Set<Integer> newState = new HashSet<>();
                for (int i : state) {
                    if (sides.containsKey(i)) {
                        for (Side side : sides.get(i)) {
                            if (side.type == type) {
                                newState.add(side.to);
                            }
                        }
                    }
                }

                //如果该状态沿着该边没有到达点，则置为错误，等待查看该状态是否是终态
                if (newState.isEmpty()) {
                    table[line][type.ordinal()] = ERROR_CODE;
                    continue;
                }

                newState = findClosure(newState);

                boolean isSame = false;
                for (int i = 0; i < total.size(); i++) {
                    if (newState.equals(total.get(i))) {
                        table[line][type.ordinal()] = i;
                        isSame = true;
                        break;
                    }
                }
                if (!isSame) {
                    total.add(newState);
                    stateQueue.add(newState);
                    table[line][type.ordinal()] = total.size() - 1;
                }
            }

            table[line][BasicType.getNumber() - 1] = ERROR_CODE;
            //判断是否是结束点
            for (int point : state) {
                if (finalStates.containsKey(point)) {
                    for (int i = 0; i < BasicType.getNumber(); i++) {
                        if(table[line][i] == ERROR_CODE){
                            System.out.println(finalStates.get(point));
                            table[line][i] = finalStates.get(point);
                        }
                    }
                }
            }
            line++;
        }

        for (int i = 0; i < line; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
    }

    private Set<Integer> findClosure(Set<Integer> original) {
        Set<Integer> result = new HashSet<>(original);
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int state : original) {
                if (sides.containsKey(state)) {
                    for (Side side : sides.get(state)) {
                        if (side.type == BasicType.EMPTY) {
                            if (result.add(side.to)) {
                                flag = true;
                            }
                        }
                    }
                }
            }
            original = new HashSet<>(result);
        }

        return result;
    }

    /**
     * 将.l中的正则表达式转换为一个NFA
     */
    private void reToNFA() {
        List<String> express = IOHelper.readLFile(CONFIG_PATH);
        ERROR_CODE = -Type.getNumber() - 1;
        int start = 0;
        System.out.println(express);
        for (int i = 0; i < 4; i++) {
            putSide(start, reToNFASingle(express.get(i), - i - 1), BasicType.EMPTY);
        }

        //System.out.println(sides);
        table = new int[stateCount][BasicType.getNumber()];
    }

    private int reToNFASingle(String express, int finalType) {
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
                    putSide(start, end, BasicType.fromStringToType(now));
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

        start = nfa.pop();
        finalStates.put(nfa.pop(), finalType);
        return start;
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
        start2 = nfa.pop();
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
        putSide(newStart, start1, BasicType.EMPTY);
        putSide(end1, newEnd, BasicType.EMPTY);
        putSide(newStart, newEnd, BasicType.EMPTY);
        putSide(end1, start1, BasicType.EMPTY);

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
        start2 = nfa.pop();
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
            sides.get(from).add(new Side(to, type));
        } else {
            List<Side> line = new LinkedList<>();
            line.add(new Side(to, type));
            sides.put(from, line);
        }
    }
}
