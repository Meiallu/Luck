package me.meiallu.luck;

import java.util.Iterator;
import java.util.List;

public class Parser {

    public static Node parse(List<Token> tokenList) {
        Node mainNode = new Node(TokenType.CODE, null);
        Iterator<Token> iterator = tokenList.iterator();

        while (iterator.hasNext()) {
            Token token = iterator.next();

            switch (token.type) {
                case KEYWORD:
                    Node keywordNode = new Node(token.type, token.value);
                    mainNode.childNodes.add(keywordNode);

                    parseFunction(iterator, keywordNode);
                    break;
            }
        }

        return mainNode;
    }

    public static void parseFunction(Iterator<Token> iterator, Node node) {
        Node identifierNode = new Node(TokenType.IDENTIFIER, iterator.next().value);
        node.childNodes.add(identifierNode);

        if (iterator.next().type == TokenType.BLOCK_START) {
            while (iterator.hasNext()) {
                Token token = iterator.next();

                switch (token.type) {
                    case IDENTIFIER:
                        break;
                    case BLOCK_STOP:
                        return;
                    case KEYWORD:
                        Node keywordNode = new Node(token.type, token.value);
                        node.childNodes.add(keywordNode);

                        parseStatement(iterator, keywordNode);
                        break;
                    case TYPE:
                        Node typeNode = new Node(token.type, token.value);
                        node.childNodes.add(typeNode);

                        parseVariableInit(iterator, typeNode);
                        break;
                }
            }
        }
    }

    public static void parseStatement(Iterator<Token> iterator, Node node) {
        switch (node.object.toString()) {
            case "while", "if", "elif":
                break;
            case "else":
                if (iterator.next().type != TokenType.BLOCK_START)
                    return;

                break;
        }

        while (iterator.hasNext()) {
            Token token = iterator.next();

            switch (token.type) {
                case IDENTIFIER:
                    break;
                case BLOCK_STOP:
                    return;
                case KEYWORD:
                    Node keywordNode = new Node(token.type, token.value);
                    node.childNodes.add(keywordNode);

                    parseStatement(iterator, keywordNode);
                    break;
                case TYPE:
                    Node typeNode = new Node(token.type, token.value);
                    node.childNodes.add(typeNode);

                    parseVariableInit(iterator, typeNode);
                    break;
            }
        }
    }

    public static void parseFunctionCall(Iterator<Token> iterator, Node node) {

    }

    public static void parseVariableInit(Iterator<Token> iterator, Node node) {

    }
}
