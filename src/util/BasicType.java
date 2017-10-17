package util;

/**
 * 该类表征了.l文件中规定的基本类型
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

        if ((input == ' ') || (input == '\t') || (input == '\n') || (input == ';')) {
            return DELIMITER;
        }

        if ((input == '+') || (input == '-') ||(input == '*') ||(input == '/') ||
             (input == '%') ||(input == '(') ||(input == ')') ||(input == '[') ||
                (input == ']') ||(input == '{') || (input == '}') ||(input == '|')
                 || (input == '=') || (input == '<') || (input == '>')){
            return OPERATOR;
        }

        if(input == '.'){
            return DOT;
        }

        return UNKNOWN;
    }

    /**
     * 将类型转换为数组中的数字
     * @param type 类型
     * @return 数组中的数字
     */
    public static int fromTypeToLoc(BasicType type){
        return type.ordinal();
    }

    /**
     * 将String类型转换为枚举类型
     * @param type String类型
     * @return 枚举类型
     */
    public static BasicType fromStringToType(String type){
        if(type.equals("LETTER")){
            return LETTER;
        }
        if(type.equals("DIGIT")){
            return DIGIT;
        }
        if(type.equals("DELIMITER")){
            return DELIMITER;
        }
        if(type.equals("OPERATOR")){
            return OPERATOR;
        }
        if(type.equals("DOT")){
            return DOT;
        }

        return UNKNOWN;
    }

    /**
     * 得到基础类型的个数
     * @return 基础类型的个数
     */
    public static int getNumber(){
        return 6;
    }
}
