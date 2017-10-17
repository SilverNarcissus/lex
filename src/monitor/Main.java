package monitor;

public class Main {
    public static void main(String[] args) {
//        Analyzer analyzer =new Analyzer();
//        analyzer.reToNFA();

        Monitor monitor = new Monitor();
        System.out.println(monitor.parse());

    }
}
