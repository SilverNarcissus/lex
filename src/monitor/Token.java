package monitor;

/**
 * 定义了token的格式
 */
class Token {
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
