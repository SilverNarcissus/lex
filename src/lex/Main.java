package lex;

/**
 * lex 分析器的入口
 * 输入：.l文件
 * 输出：.t文件
 */
public class Main {

    public static void main(String[] args) {
        Analyzer analyzer =new Analyzer();
        analyzer.parse();
    }
}
