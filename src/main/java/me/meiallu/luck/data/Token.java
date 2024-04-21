package me.meiallu.luck.data;

public class Token {

    public TokenType type;
    public Object value;

    public Token(TokenType type, Object object) {
        this.type = type;
        this.value = object;
    }
}
