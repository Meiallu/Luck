package me.meiallu.luck.data

enum class TokenType {
    BLOCK_START,
    BLOCK_END,
    IDENTIFIER,
    FUNCTION,
    FUNCTION_END,
    ASSIGNMENT_OPERATOR,
    ARITHMETIC_OPERATOR,
    COMPARISON_OPERATOR,
    KEYWORD,
    SEPARATOR,
    STRING,
    NUMBER,
    TYPE,
    NEW_LINE
}