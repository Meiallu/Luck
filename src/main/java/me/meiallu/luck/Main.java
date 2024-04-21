package me.meiallu.luck;

import me.meiallu.luck.data.Token;
import me.meiallu.luck.run.Lexer;
import me.meiallu.luck.run.Parser;

import java.util.HashMap;
import java.util.List;

public class Main {

    private static HashMap<String, Object> variables = new HashMap<>();

    public static void main(String[] args) {
        variables = new HashMap<>();

        List<Token> tokenList = Lexer.lex("script.luck");
        Parser.run(tokenList);
    }

    public static HashMap<String, Object> getVariables() {
        return variables;
    }
}