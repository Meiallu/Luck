package me.meiallu.luck

val variables: HashMap<String, Any> = HashMap()

fun main() {
    val tokenList = Lexer.lex("script.luck")
    Parser.run(tokenList)
}