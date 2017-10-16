package lex;

public class Main {

    public static void main(String[] args) {
//        Monitor monitor = new Monitor();
//        System.out.println(monitor.parse());
        Analyzer analyzer =new Analyzer();
        analyzer.reToNFA();
    }
}
