package lex;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public class Token {
    private String text;
    private String type;

    public Token(String text, String type) {
        this.text = text;
        this.type = type;
    }

    @Override
    public String toString() {
        return "<" + type + ", " + text + ">";
    }
}
