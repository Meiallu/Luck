package me.meiallu.luck.run;

import me.meiallu.luck.data.Token;
import me.meiallu.luck.data.TokenType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public static List<Token> lex(String path) {
        try {
            List<Token> tokenList = new ArrayList<>();
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
                        case '+', '-', '*', '/', '%':
                            tokenList.add(new Token(TokenType.ARITHMETIC_OPERATOR, character));
                            break;
                        case '=':
                            tokenList.add(new Token(TokenType.ASSIGNMENT_OPERATOR, null));
                            break;
                        case ' ', ',', '(', ')':
                            if (builder != null) {
                                Token token = getToken(builder);
                                tokenList.add(token);

                                if (character == '(')
                                    token.type = TokenType.FUNCTION;

                                builder = null;
                            }

                            switch (character) {
                                case ')':
                                    tokenList.add(new Token(TokenType.FUNCTION_END, null));
                                    break;
                                case ',':
                                    tokenList.add(new Token(TokenType.SEPARATOR, null));
                                    break;
                            }
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
            return tokenList;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Token getToken(StringBuilder builder) {
        String toString = builder.toString();

        if (toString.equals("var")) {
            return new Token(TokenType.TYPE, null);
        } else {
            try {
                double parsedDouble = Double.parseDouble(toString);
                return new Token(TokenType.NUMBER, parsedDouble);
            } catch (NumberFormatException ignored) {
                return new Token(TokenType.IDENTIFIER, toString);
            }
        }
    }
}
