package me.meiallu.luck;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Token> tokenList = Lexer.lex("script.luck");
        Node node = Parser.parse(tokenList);

        for (Token token : tokenList)
            System.out.println(token.type.name() + (token.value == null ? "" : "(" + token.value + ")"));

        String treeNode = node.toString();
        System.out.println();
        System.out.println(treeNode);
    }
}