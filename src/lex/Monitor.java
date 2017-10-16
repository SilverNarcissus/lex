package lex;

import util.ReadHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public class Monitor {
    // 转换表文件位置
    private static final String TABLE_PATH = "table.t";

    // 输入文件位置
    private static final String INPUT_PATH = "input.txt";

    // 转换表
    private int[][] table;

    public Monitor() {
        table = ReadHelper.readTable(TABLE_PATH);
    }

    public List<Token> parse() {
        String sequence = ReadHelper.readInput(INPUT_PATH);
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

            System.out.println(state);

            //如果进入终态或出现错误
            if (state < 0) {
                Type finalType = Type.fromIntToType(state);
                if (finalType == Type.ERROR) {
                    errorHandling(line, loc);
                    return null;
                }
                result.add(new Token(now,finalType));
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
        Type finalType = Type.fromIntToType(state);
        if (finalType == Type.ERROR) {
            errorHandling(line, loc);
            return null;
        }
        result.add(new Token(now,finalType));
        //

        return result;
    }

    /**
     * 错误处理子程序
     * @param line 错误出现的行号
     * @param loc 错误出现的列号
     */
    private void errorHandling(int line, int loc) {
        System.out.println("Error in the code at line " + line + " cross " + loc);
    }
}
