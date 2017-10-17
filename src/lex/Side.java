package lex;

import util.BasicType;

/**
 * 该类表征了NFA中的一条边
 */
class Side {
    /**
     * 边的终点
     */
    public int to;

    /**
     * 边的转换类型
     */
    public BasicType type;

    public Side(int to, BasicType type) {
        this.to = to;
        this.type = type;
    }

    @Override
    public String toString(){
        return "number: " + to + " type: " + type;
    }
}
