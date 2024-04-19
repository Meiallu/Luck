package me.meiallu.luck;

public class Token {
    TokenType type;
    Object value;

    public Token(TokenType type, Object object) {
        this.type = type;
        this.value = object;
    }
}
