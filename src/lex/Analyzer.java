package lex;

import util.BasicType;
import util.Type;

import java.util.*;

/**
 * 分析器，作用是将RE转换为DFA table
 */
class Analyzer {
    //table的列数
    private static final int CROSS = BasicType.getNumber();

    // .l文件位置
    private static final String CONFIG_PATH = "re.l";

    // 转换表文件位置
    private static final String TABLE_PATH = "table.t";

    // 错误状态码
    private static int ERROR_CODE;

    // table
    private int[][] table;

    // table行的大小
    private int line;

    // 记录边的图
    private HashMap<Integer, List<Side>> sides;

    // 记录最终状态的图
    // key ----- 最终状态号
    // value ----- 最终状态表征的token type
    private HashMap<Integer, Integer> finalStates;

    //状态数
    private int stateCount;

    //删除掉的行
    private List<Integer> deletedLine;

    public Analyzer() {
        sides = new HashMap<>();
        finalStates = new HashMap<>();
        deletedLine = new ArrayList<>();
        stateCount = 1;
        line = 0;
    }

    /**
     * 主方法，分析.l文件，生成.t文件
     */
    public void parse() {
        reToNFA();
        NFAToDFA();
        DFAToDFAO();
        IOHelper.buildTableFile(table, line, CROSS, TABLE_PATH, deletedLine);
    }

    /**
     * 优化 DFA 生成 DFAO
     */
    private void DFAToDFAO() {
        //初始化分组
        Set<Integer> start = new HashSet<>();
        Set<Integer> end = new HashSet<>();
        List<Group> level = new ArrayList<>();
        for (int i = 0; i < line; i++) {
            if (table[i][CROSS - 1] == ERROR_CODE) {
                start.add(i);
            } else {
                end.add(i);
            }
        }
        level.add(new Group(start, false));
        level.add(new Group(end, false));
        //递归进行分组
        List<Group> newLevel = new ArrayList<>(level);
        do {
            level = newLevel;
            newLevel = new ArrayList<>();
            for (Group group : level) {
                if (group.isStrong) {
                    newLevel.add(new Group(group.states, true));
                }
                //对非强关联组做分解
                else {
                    Set<Integer> left = new HashSet<>();
                    Set<Integer> right = new HashSet<>();
                    divideGroup(level, group, left, right);
                    //检查分组是否为强关联，如果是，下次可以不做检查
                    boolean isStrong;
                    isStrong = checkStrong(left);
                    newLevel.add(new Group(left, isStrong));

                    if (!right.isEmpty()) {
                        isStrong = checkStrong(right);
                        newLevel.add(new Group(right, isStrong));
                    }
                }
            }
        }
        while (newLevel.size() != level.size());

        optimizeTable(level);
    }

    /**
     * 根据分组优化生成的DFA （DFA->DFAO）
     *
     * @param level 状态分组
     */
    private void optimizeTable(List<Group> level) {
        for (Group finalGroup : level) {
            if (finalGroup.states.size() > 1) {
                int before = -1;
                //取Set的第一个元素
                for (int state : finalGroup.states) {
                    if (before == -1) {
                        before = state;
                        continue;
                    }
                    deletedLine.add(state);
                }
                for (int i = 0; i < line; i++) {
                    for (int j = 0; j < CROSS; j++) {
                        if (finalGroup.states.contains(table[i][j])) {
                            table[i][j] = before;
                        }
                    }
                }
            }
        }
        Collections.sort(deletedLine);

        //System.out.println(deletedLine);
        HashMap<Integer, Integer> map = new HashMap<>();
        int loc = 0;
        for (int i = 0; i < line; i++) {
            if (i == deletedLine.get(loc)) {
                loc++;
                continue;
            }
            map.put(i, i - loc);
        }

        for (int i = 0; i < line; i++) {
            if (!deletedLine.contains(i)) {
                for (int j = 0; j < CROSS; j++) {
                    if (map.containsKey(table[i][j])) {
                        table[i][j] = map.get(table[i][j]);
                    }
                }
            }
        }
    }

    /**
     * 检查输入的分组是否是强连接分组
     *
     * @param group 输入的分组
     * @return 是否是强连接分组
     */
    private boolean checkStrong(Set<Integer> group) {
        boolean isStrong = true;
        int before = -1;
        for (int now : group) {
            if (before == -1) {
                before = now;
                continue;
            }
            for (int i = 0; i < CROSS; i++) {
                if (table[before][i] != table[now][i]) {
                    isStrong = false;
                    break;
                }
            }
        }
        return isStrong;
    }

    /**
     * 对给定分组层及层中的一个分组进行下一步分组
     *
     * @param level 给定分组层
     * @param group 层中的一个分组
     * @param left  分出的第一个组
     * @param right 分出的第二个组
     */
    private void divideGroup(List<Group> level, Group group, Set<Integer> left, Set<Integer> right) {
        int first = -1;
        for (int state : group.states) {
            if (first == -1) {
                first = state;
                left.add(first);
                continue;
            }
            //判断是否和第一个元素是关联元素
            boolean flag = true;
            for (int i = 0; i < CROSS; i++) {
                if (table[first][i] == table[state][i]) {
                    left.add(state);
                    continue;
                }
                boolean isWake = false;
                for (Group up : level) {
                    if (up.states.contains(table[first][i]) && up.states.contains(table[state][i])) {
                        isWake = true;
                        break;
                    }
                }
                flag = isWake;
                if (!flag) {
                    break;
                }
            }
            if (flag) {
                left.add(state);
            } else {
                right.add(state);
            }
        }
    }

    /**
     * 将生成的NFA换为一个DFA
     */
    private void NFAToDFA() {
        //初始化变量
        Set<Integer> start = new HashSet<>();
        start.add(0);
        start = findClosure(start);
        ArrayList<Set<Integer>> total = new ArrayList<>();
        total.add(start);

        Queue<Set<Integer>> stateQueue = new LinkedList<>();
        stateQueue.add(start);

        //递归寻找所有DFA状态
        while (!stateQueue.isEmpty()) {
            Set<Integer> state = stateQueue.remove();
            //对各种发出边做到达闭包
            for (BasicType type : BasicType.values()) {
                if (type == BasicType.EMPTY) {
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

            table[line][CROSS - 1] = ERROR_CODE;
            //判断是否是结束点
            for (int point : state) {
                if (finalStates.containsKey(point)) {
                    for (int i = 0; i < CROSS; i++) {
                        if (table[line][i] == ERROR_CODE) {
                            table[line][i] = finalStates.get(point);
                        }
                    }
                }
            }
            line++;
        }
    }

    /**
     * 寻找状态集合的 ε 闭包
     *
     * @param original 状态集合
     * @return 态集合的 ε 闭包
     */
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
        for (int i = 0; i < 4; i++) {
            putSide(start, reToNFASingle(express.get(i), -i - 1), BasicType.EMPTY);
        }

        table = new int[stateCount][CROSS];
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
