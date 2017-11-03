package monitor;

/**
 * monitor token 生成器的入口
 * 输入：.t文件 & java代码
 * 输出：token序列
 */
public class Main {
    // 输出文件位置
    private static final String OUTPUT_PATH = "out.txt";

    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        IOHelper.writeOutput(OUTPUT_PATH, monitor.parse());
    }
}
