package monitor;

import util.BasicType;
import util.Type;

import java.util.LinkedList;
import java.util.List;

/**
 * 给定转换表和输入，生成token序列 （含有错误处理部分）
 */
class Monitor {
    //关键字名称
    private static final String KEYWORD_NAME = "KEYWORD";

    // 转换表文件位置
    private static final String TABLE_PATH = "table.t";

    // 输入文件位置
    private static final String INPUT_PATH = "input.txt";

    // 转换表
    private int[][] table;


    public Monitor() {
        table = Reader.readTable(TABLE_PATH);
    }

    /**
     * 给定转换表和输入文件位置，生成token序列
     *
     * @return token序列
     */
    public List<Token> parse() {
        String sequence = Reader.readInput(INPUT_PATH);
        //System.out.println(sequence);
        int pointer = 0;
        int state = 0;

        // for error handling
        int line = 1;
        int loc = 1;
        //

        List<Token> result = new LinkedList<>();
        String now = "";

        while (pointer < sequence.length()) {
            char c = sequence.charAt(pointer);
            BasicType basicType = BasicType.fromCharToType(c);
            state = table[state][BasicType.fromTypeToLoc(basicType)];

            //System.out.println(c);

            //如果进入终态或出现错误
            if (state < 0) {
                String finalType = Type.intToType(state);

                if (basicType == BasicType.DELIMITER) {
                    state = 0;
                    if (!finalType.equals("ERROR")) {
                        addToken(result, now, finalType);
                        now = "";
                    }
                    pointer++;
                    continue;
                }

                if (finalType.equals("ERROR")) {
                    errorHandling(line, loc);
                    //System.out.println(c);
                    return null;
                }
                addToken(result, now, finalType);
                now = "";

                state = 0;
            }
            // 继续在DFA中运行
            else {
                now = now + c;
                pointer++;
                loc++;
            }

            if (c == '\n') {
                line++;
                loc = 1;
            }
        }

        //处理结尾的一个字符
        state = table[state][BasicType.EMPTY.ordinal()];
        String finalType = Type.intToType(state);
        if (finalType.equals("ERROR")) {
            errorHandling(line, loc);
            //System.out.println("!");
            return null;
        }
        addToken(result, now, finalType);
        //

        return result;
    }

    /**
     * 向结果集中添加一个token
     * @param result 结果集
     * @param now 当前字符串
     * @param finalType 判定的类型
     */
    private void addToken(List<Token> result, String now, String finalType) {
        if (Type.isKeyword(now)) {
            result.add(new Token(now, KEYWORD_NAME));
        } else {
            result.add(new Token(now, finalType));
        }
    }

    /**
     * 错误处理子程序
     *
     * @param line 错误出现的行号
     * @param loc  错误出现的列号
     */
    private void errorHandling(int line, int loc) {
        System.out.println("Error in the code at line " + line + " cross " + loc);
    }
}
