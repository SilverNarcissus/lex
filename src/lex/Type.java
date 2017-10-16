package lex;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public enum Type {
    ID,
    INTEGER,
    REAL,
    OPERATOR,
    ERROR;

    /**
     * 将转换表中的状态码转换为类型
     * @param mark 状态码
     * @return 类型
     */
    public static Type fromIntToType(int mark){
        switch (mark){
            case -1:
                return ID;
            case -2:
                return INTEGER;
            case -3:
                return REAL;
            case -4:
                return OPERATOR;
        }
        return ERROR;
    }

    /**
     * 将类型转换为状态码，便于存储
     * @param type 类型
     * @return 状态码
     */
    public static int fromTypeToInt(Type type){
        switch (type){
            case ID:
                return -1;
            case INTEGER:
                return -2;
            case REAL:
                return -3;
            case OPERATOR:
                return -4;
            case ERROR:
                return -5;
        }
        return -5;
    }
}
