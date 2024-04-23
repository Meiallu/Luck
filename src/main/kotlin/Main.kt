package me.meiallu.luck

import me.meiallu.luck.data.Variable
import me.meiallu.luck.runner.Lexer
import me.meiallu.luck.runner.Parser

val variables: HashMap<String, Variable> = HashMap()

fun main() {
    val tokenList = Lexer.lex("script.luck")
    Parser.run(tokenList)
}