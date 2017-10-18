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
        if (isLetter(input)) {
            return LETTER;
        }

        if (isDigit(input)) {
            return DIGIT;
        }

        if (isDelimiter(input)) {
            return DELIMITER;
        }

        if (isOperator(input)){
            return OPERATOR;
        }

        if(isDot(input)){
            return DOT;
        }

        return UNKNOWN;
    }

    /**
     * 判断一个字符是否是点
     * @param input 字符
     * @return 该字符是否是点
     */
    private static boolean isDot(char input) {
        return input == '.';
    }

    /**
     * 判断一个字符是否是分隔符
     * @param input 字符
     * @return 该字符是否是分隔符
     */
    private static boolean isDelimiter(char input) {
        return (input == ' ') || (input == '\t') || (input == '\n') || (input == ';');
    }

    /**
     * 判断一个字符是否是数字
     * @param input 字符
     * @return 该字符是否是数字
     */
    private static boolean isDigit(char input) {
        return '0' <= input && input <= '9';
    }

    /**
     * 判断一个字符是否是字母
     * @param input 字符
     * @return 该字符是否是字母
     */
    private static boolean isLetter(char input) {
        return ('a' <= input && input <= 'z')
                || ('A' <= input && input <= 'Z');
    }

    /**
     * 判断一个字符是否是操作符
     * @param input 字符
     * @return 该字符是否是操作符
     */
    private static boolean isOperator(char input) {
        return (input == '+') || (input == '-') ||(input == '*') ||(input == '/') ||
             (input == '%') ||(input == '(') ||(input == ')') ||(input == '[') ||
                (input == ']') ||(input == '{') || (input == '}') ||(input == '|')
                 || (input == '=') || (input == '<') || (input == '>');
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
