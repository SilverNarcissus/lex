package lex;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public class Side {
    public int to;
    public BasicType type;

    public Side(int to, BasicType type) {
        this.to = to;
        this.type = type;
    }

    @Override
    public String toString(){
        return "to: " + to + " type: " + type;
    }
}
