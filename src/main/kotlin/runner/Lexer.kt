package me.meiallu.luck.runner

import me.meiallu.luck.data.Token
import me.meiallu.luck.data.TokenType
import java.io.File

class Lexer {

    companion object {
        fun lex(path: String): ArrayList<Token> {
            val tokenList: ArrayList<Token> = ArrayList()

            File(path).forEachLine {
                var builder: StringBuilder? = null
                val chars = it.toCharArray()
                var readingString = false

                for (character in chars) {

                    if (character == '"') {
                        if (!readingString) {
                            builder = StringBuilder()
                            readingString = true
                        } else {
                            tokenList += Token(TokenType.STRING, builder.toString())
                            builder = null
                            readingString = false
                        }
                        continue
                    } else if (readingString) {
                        builder?.append(character)
                        continue
                    }

                    when (character) {
                        '+', '-', '*', '/', '%' -> tokenList += Token(TokenType.ARITHMETIC_OPERATOR, character)
                        '=' -> tokenList += Token(TokenType.ASSIGNMENT_OPERATOR, null)
                        '{' -> tokenList += Token(TokenType.BLOCK_START, null)
                        '}' -> tokenList += Token(TokenType.BLOCK_END, null)
                        '<', '>' -> tokenList += Token(TokenType.COMPARISON_OPERATOR, character)
                        ' ', ',', '(', ')' -> {
                            if (builder != null) {
                                val token = getToken(builder)
                                tokenList += token

                                if (character == '(' && token.type == TokenType.IDENTIFIER)
                                    token.type = TokenType.FUNCTION

                                builder = null
                            }

                            when (character) {
                                ')' -> tokenList += Token(TokenType.FUNCTION_END, null)
                                ',' -> tokenList += Token(TokenType.SEPARATOR, null)
                            }
                        }
                        else -> {
                            if (builder == null)
                                builder = StringBuilder()

                            builder.append(character)
                        }
                    }
                }

                if (builder != null)
                    tokenList += getToken(builder)

                if (tokenList[tokenList.size - 1].type != TokenType.NEW_LINE)
                    tokenList += Token(TokenType.NEW_LINE, null)
            }
            return tokenList
        }

        private fun getToken(builder: java.lang.StringBuilder): Token {
            val toString = builder.toString()

            when (toString) {
                "var", "const" -> return Token(TokenType.TYPE, toString)
                "if" -> return Token(TokenType.KEYWORD, toString)
            }

            return if (toString.toDoubleOrNull() != null)
                Token(TokenType.NUMBER, toString.toDouble())
            else
                Token(TokenType.IDENTIFIER, toString)
        }
    }
}