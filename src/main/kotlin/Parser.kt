package me.meiallu.luck

class Parser {

    companion object {
        fun run(tokenList: ArrayList<Token>) {
            var i = 0

            while (i < tokenList.size) {
                val token = tokenList[i]

                when (token.type) {
                    TokenType.TYPE -> {
                        val name = tokenList[++i].value as String
                        var value: Any? = null

                        if (tokenList[i + 1].type == TokenType.ASSIGNMENT_OPERATOR) {
                            i += 2
                            value = evaluate(tokenList, i)
                        }

                        variables[name] = value as Any
                    }

                    TokenType.IDENTIFIER -> {
                        val identifier = token.value as String

                        if (variables.containsKey(identifier) && tokenList[i + 1].type == TokenType.ASSIGNMENT_OPERATOR) {
                            i += 2
                            val identifierValue = evaluate(tokenList, i)

                            variables[identifier] = identifierValue
                        }
                    }

                    TokenType.FUNCTION -> runFunction(tokenList, token.value as String, i)
                    else -> {}
                }

                i++
            }
        }

        private fun runFunction(tokenList: MutableList<Token>, functionName: String, currentIndex: Int): Any? {
            var index: Int = currentIndex

            val arguments: ArrayList<ArrayList<Token>> = ArrayList()
            var loopedToken = tokenList[++index]

            while (loopedToken.type != TokenType.FUNCTION_END && loopedToken.type != TokenType.NEW_LINE) {
                val currentArgument: ArrayList<Token> = ArrayList()

                while (loopedToken.type != TokenType.SEPARATOR && loopedToken.type != TokenType.FUNCTION_END) {
                    currentArgument += loopedToken
                    loopedToken = tokenList[++index]
                }

                if (index < tokenList.size - 2)
                    loopedToken = tokenList[++index]
                if (currentArgument.isNotEmpty())
                    arguments += currentArgument
            }

            val objects = arrayOfNulls<Any>(arguments.size)

            for (i in 0..<arguments.size)
                objects[i] = evaluate(arguments[i], 0)

            return Functions.get(functionName)?.call(objects)
        }

        private fun evaluate(tokenList: ArrayList<Token>, currentIndex: Int): Any {
            var assignmentList: MutableList<Token> = tokenList

            if (currentIndex != 0) {
                var type = tokenList[currentIndex].type
                var lastIndex = currentIndex

                while (type != TokenType.NEW_LINE && type != TokenType.SEPARATOR && type != TokenType.FUNCTION_END)
                    type = tokenList[++lastIndex].type

                assignmentList = tokenList.subList(currentIndex, lastIndex)
            }

            if (isExpression(assignmentList)) {
                val iterator = assignmentList.iterator()
                var token = iterator.next()

                var value = when (token.type) {
                    TokenType.NUMBER -> token.value as Double
                    TokenType.IDENTIFIER -> variables[token.value as String] as Double
                    else -> throw IllegalArgumentException("Unexpected type: " + token.type)
                }

                while (iterator.hasNext()) {
                    token = iterator.next()

                    if (token.type == TokenType.ARITHMETIC_OPERATOR) {
                        val nextToken = iterator.next()

                        val nextDouble = when (nextToken.type) {
                            TokenType.NUMBER -> nextToken.value as Double
                            TokenType.IDENTIFIER -> variables[nextToken.value as String] as Double
                            else -> throw IllegalStateException("Unexpected type: " + nextToken.type)
                        }

                        value = when (token.value as Char) {
                            '+' -> value + nextDouble
                            '-' -> value - nextDouble
                            '*' -> value * nextDouble
                            '/' -> value / nextDouble
                            '%' -> value % nextDouble
                            else -> throw IllegalStateException("Unexpected operator: " + token.value as Char)
                        }
                    }
                }

                return value
            }

            return when (assignmentList[0].type) {
                TokenType.IDENTIFIER -> variables[assignmentList[0].value as String]
                TokenType.FUNCTION -> {
                    assignmentList += Token(TokenType.FUNCTION_END, null)
                    runFunction(assignmentList, assignmentList[0].value as String, 0)
                }

                TokenType.STRING, TokenType.NUMBER -> assignmentList[0].value
                else -> null
            } ?: "null"
        }

        private fun isExpression(tokenList: MutableList<Token>): Boolean {
            for (token in tokenList)
                if (token.type == TokenType.ARITHMETIC_OPERATOR)
                    return true

            return false
        }
    }
}