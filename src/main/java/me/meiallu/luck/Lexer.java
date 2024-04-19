package me.meiallu.luck;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public static List<Token> lex(String path) {
        List<Token> tokenList = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
            String line = bufferedReader.readLine();

            while (line != null) {
                StringBuilder builder = null;
                char[] chars = line.toCharArray();

                boolean readingString = false;

                for (Character character : chars) {
                    if (character == '"') {
                        if (!readingString) {
                            builder = new StringBuilder();
                            readingString = true;
                        } else {
                            tokenList.add(new Token(TokenType.STRING, builder.toString()));
                            builder = null;
                            readingString = false;
                        }
                        continue;
                    } else if (readingString) {
                        builder.append(character);
                        continue;
                    }

                    switch (character) {
                        case '+', '-', '%', '*':
                            tokenList.add(new Token(TokenType.ARITHMETIC_OPERATOR, character));
                            break;
                        case '!':
                            tokenList.add(new Token(TokenType.LOGICAL_OPERATOR, character));
                            break;
                        case '=':
                            Token lastToken = tokenList.get(tokenList.size() - 1);
                            char lastChar = lastToken.value instanceof Character ? (char) lastToken.value : 'a';

                            if (lastToken.type == TokenType.LOGICAL_OPERATOR && lastChar == '!') {
                                tokenList.remove(lastToken);
                                tokenList.add(new Token(TokenType.COMPARISON_OPERATOR, "!="));
                            } else if (lastToken.type == TokenType.ASSIGNMENT_OPERATOR && lastChar == '=') {
                                tokenList.remove(lastToken);
                                tokenList.add(new Token(TokenType.COMPARISON_OPERATOR, "=="));
                            } else {
                                tokenList.add(new Token(TokenType.ASSIGNMENT_OPERATOR, character));
                            }
                            break;
                        case '<', '>':
                            tokenList.add(new Token(TokenType.COMPARISON_OPERATOR, character));
                            break;
                        case ' ', '(', ')', ':':
                            if (builder != null) {
                                tokenList.add(getToken(builder));
                                builder = null;
                            }

                            if (character == ':')
                                tokenList.add(new Token(TokenType.BLOCK_START, character));

                            break;
                        default:
                            if (builder == null)
                                builder = new StringBuilder();

                            builder.append(character);
                            break;
                    }
                }

                if (builder != null)
                    tokenList.add(getToken(builder));

                if (tokenList.get(tokenList.size() - 1).type != TokenType.NEW_LINE)
                    tokenList.add(new Token(TokenType.NEW_LINE, null));

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return tokenList;
    }

    public static Token getToken(StringBuilder builder) {
        String toString = builder.toString();

        switch (toString) {
            case "&&", "||":
                return new Token(TokenType.LOGICAL_OPERATOR, toString);
            case "fun", "while", "if", "elif", "else":
                return new Token(TokenType.KEYWORD, toString);
            case "byte", "short", "int", "long", "float", "double", "char", "boolean":
                return new Token(TokenType.TYPE, toString);
            case "true", "false":
                return new Token(TokenType.BOOLEAN, toString);
            default:
                try {
                    double parsedDouble = Double.parseDouble(toString);
                    return new Token(TokenType.NUMBER, parsedDouble);
                } catch (NumberFormatException ignored) {
                    return new Token(TokenType.IDENTIFIER, toString);
                }
        }
    }
}
