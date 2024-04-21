package me.meiallu.luck.run;

import me.meiallu.luck.Main;
import me.meiallu.luck.data.Token;
import me.meiallu.luck.data.TokenType;
import me.meiallu.luck.function.Functions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser {

    public static void run(List<Token> tokenList) {
        for (int i = 0; i < tokenList.size(); i++) {
            Token token = tokenList.get(i);

            switch (token.type) {
                case TYPE:
                    String name = tokenList.get(++i).value.toString();
                    Object value = null;

                    if (tokenList.get(i + 1).type == TokenType.ASSIGNMENT_OPERATOR)
                        value = evaluate(tokenList, i += 2);

                    Main.getVariables().put(name, value);
                    break;
                case IDENTIFIER:
                    String identifier = token.value.toString();

                    if (Main.getVariables().containsKey(identifier) && tokenList.get(i + 1).type == TokenType.ASSIGNMENT_OPERATOR) {
                        Object identifierValue = evaluate(tokenList, i += 2);
                        Main.getVariables().put(identifier, identifierValue);
                    }
                    break;
                case FUNCTION:
                    runFunction(tokenList, (String) token.value, i);
                    break;
            }
        }
    }

    private static Object runFunction(List<Token> tokenList, String functionName, int currentIndex) {
        List<List<Token>> arguments = new ArrayList<>();
        Token loopedToken = tokenList.get(++currentIndex);

        while (loopedToken.type != TokenType.FUNCTION_END && loopedToken.type != TokenType.NEW_LINE) {
            List<Token> currentArgument = new ArrayList<>();

            while (loopedToken.type != TokenType.SEPARATOR && loopedToken.type != TokenType.FUNCTION_END) {
                currentArgument.add(loopedToken);
                loopedToken = tokenList.get(++currentIndex);
            }

            if (currentIndex < tokenList.size() - 2)
                loopedToken = tokenList.get(++currentIndex);
            if (!currentArgument.isEmpty())
                arguments.add(currentArgument);
        }

        Object[] objects = new Object[arguments.size()];
        int index = 0;

        for (List<Token> tokens : arguments) {
            objects[index] = evaluate(tokens, 0);
            index++;
        }

        return Functions.get(functionName).call(objects);
    }

    private static Object evaluate(List<Token> tokenList, int currentIndex) {
        if (currentIndex != 0) {
            TokenType type = tokenList.get(currentIndex).type;
            int lastIndex = currentIndex;

            while (type != TokenType.NEW_LINE && type != TokenType.SEPARATOR && type != TokenType.FUNCTION_END)
                type = tokenList.get(++lastIndex).type;

            tokenList = tokenList.subList(currentIndex, lastIndex);
        }

        if (isExpression(tokenList)) {
            Iterator<Token> iterator = tokenList.iterator();
            Token token = iterator.next();

            Object value = switch (token.type) {
                case NUMBER -> (Double) token.value;
                case IDENTIFIER -> (Double) Main.getVariables().get((String) token.value);
                default -> throw new IllegalStateException("Unexpected value: " + token.type);
            };

            while (iterator.hasNext()) {
                token = iterator.next();

                if (token.type == TokenType.ARITHMETIC_OPERATOR) {
                    Token nextToken = iterator.next();

                    Double nextDouble = switch (nextToken.type) {
                        case NUMBER -> (Double) nextToken.value;
                        case IDENTIFIER -> (Double) Main.getVariables().get((String) nextToken.value);
                        default -> throw new IllegalStateException("Unexpected type: " + nextToken.type);
                    };

                    value = switch ((char) token.value) {
                        case '+' -> (Double) value + nextDouble;
                        case '-' -> (Double) value - nextDouble;
                        case '*' -> (Double) value * nextDouble;
                        case '/' -> (Double) value / nextDouble;
                        case '%' -> (Double) value % nextDouble;
                        default -> throw new IllegalStateException("Unexpected operator: " + (char) token.value);
                    };
                }
            }

            return value;
        }

        return switch (tokenList.get(0).type) {
            case IDENTIFIER -> Main.getVariables().get(tokenList.get(0).value.toString());
            case FUNCTION -> {
                tokenList.add(new Token(TokenType.FUNCTION_END, null));
                yield runFunction(tokenList, (String) tokenList.get(0).value, 0);
            }
            case STRING, NUMBER -> tokenList.get(0).value;
            default -> null;
        };
    }

    private static boolean isExpression(List<Token> tokenList) {
        for (Token token : tokenList)
            if (token.type == TokenType.ARITHMETIC_OPERATOR)
                return true;

        return false;
    }
}
