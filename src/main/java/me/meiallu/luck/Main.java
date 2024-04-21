package me.meiallu.luck;

import me.meiallu.luck.data.Token;
import me.meiallu.luck.run.Lexer;
import me.meiallu.luck.run.Parser;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Token> tokenList = Lexer.lex("script.luck");
        Parser.run(tokenList);
    }
}