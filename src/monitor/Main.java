package monitor;

/**
 * monitor token 生成器的入口
 * 输入：.t文件 & java代码
 * 输出：token序列
 */
public class Main {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        System.out.println(monitor.parse());
    }
}
