package lex;

/**
 * Created by SilverNarcissus on 2017/10/16.
 */
public enum BasicType {
    LETTER,
    DIGIT,
    DELIMITER,
    OPERATOR,
    DOT,
    EMPTY,
    UNKNOWN;

    /**
     * 将字符转换为基本类型
     *
     * @param input 输入的字符
     * @return 基本类型
     */
    public static BasicType fromCharToType(char input) {
        if (('a' <= input && input <= 'z')
                || ('A' <= input && input <= 'Z')) {
            return LETTER;
        }

        if ('0' <= input && input <= '9') {
            return DIGIT;
        }

        if ((input == ' ') || (input == '\t') || (input == '\n')) {
            return DELIMITER;
        }

        if ((input == '+') || (input == '-') ||(input == '*') ||(input == '/') ||
             (input == '%') ||(input == '(') ||(input == ')') ||(input == '[') ||
                (input == ']') ||(input == '{') ||(input == '|')){
            return OPERATOR;
        }

        if(input == '.'){
            return DOT;
        }

        return UNKNOWN;
    }

    public static int fromTypeToLoc(BasicType type){
        return type.ordinal();
    }
}
