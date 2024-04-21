package me.meiallu.luck.run;

import me.meiallu.luck.data.Token;
import me.meiallu.luck.data.TokenType;
import me.meiallu.luck.function.Functions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Parser {

    private static HashMap<String, Object> variables = new HashMap<>();

    public static void run(List<Token> tokenList) {
        for (int i = 0; i < tokenList.size(); ) {
            Token token = tokenList.get(i);

            switch (token.type) {
                case TYPE:
                    String name = tokenList.get(++i).value.toString();
                    Object value = null;

                    if (tokenList.get(i + 1).type == TokenType.ASSIGNMENT_OPERATOR)
                        value = evaluate(tokenList, i += 2);

                    variables.put(name, value);
                    break;
                case FUNCTION:
                    List<List<Token>> arguments = new ArrayList<>();
                    Token loopedToken = tokenList.get(++i);

                    while (loopedToken.type != TokenType.FUNCTION_END && loopedToken.type != TokenType.NEW_LINE) {
                        List<Token> currentArgument = new ArrayList<>();

                        while (loopedToken.type != TokenType.SEPARATOR && loopedToken.type != TokenType.FUNCTION_END) {
                            currentArgument.add(loopedToken);
                            loopedToken = tokenList.get(++i);
                        }

                        if (i < tokenList.size() - 2)
                            loopedToken = tokenList.get(++i);
                        if (!currentArgument.isEmpty())
                            arguments.add(currentArgument);
                    }

                    Object[] objects = new Object[arguments.size()];
                    int index = 0;

                    for (List<Token> tokens : arguments) {
                        objects[index] = evaluate(tokens, 0);
                        index++;
                    }

                    Functions.get((String) token.value).call(objects);
                    break;
            }

            i++;
        }
    }

    private static Object evaluate(List<Token> tokenList, int currentIndex) {
        Object value = null;

        TokenType type = tokenList.get(currentIndex).type;
        List<Token> assignment;

        if (currentIndex == 0) {
            assignment = tokenList;
        } else {
            int lastIndex = currentIndex;

            while (lastIndex < tokenList.size() - 1 && type != TokenType.NEW_LINE && type != TokenType.SEPARATOR && type != TokenType.FUNCTION_END)
                type = tokenList.get(++lastIndex).type;

            assignment = tokenList.subList(currentIndex, lastIndex);
        }

        if (isExpression(assignment)) {
            Iterator<Token> iterator = assignment.iterator();

            while (iterator.hasNext()) {
                Token token = iterator.next();

                if (value == null) {
                    value = switch (token.type) {
                        case NUMBER -> (Double) token.value;
                        case IDENTIFIER -> (Double) variables.get((String) token.value);
                        default -> throw new IllegalStateException("Unexpected value: " + token.type);
                    };
                    continue;
                }

                if (token.type == TokenType.ARITHMETIC_OPERATOR) {
                    Token nextToken = iterator.next();

                    Double nextDouble = switch (nextToken.type) {
                        case NUMBER -> (Double) nextToken.value;
                        case IDENTIFIER -> (Double) variables.get((String) nextToken.value);
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
        } else if (assignment.get(0).type == TokenType.IDENTIFIER) {
            value = variables.get(assignment.get(0).value.toString());
        } else {
            value = assignment.get(0).value;
        }

        return value;
    }

    private static boolean isExpression(List<Token> tokenList) {
        for (Token token : tokenList)
            if (token.type == TokenType.ARITHMETIC_OPERATOR)
                return true;

        return false;
    }
}
