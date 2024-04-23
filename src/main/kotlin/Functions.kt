package me.meiallu.luck


enum class Functions(val methodName: String, private val function: Function) {

    PRINTLINE("println", PrintLine()),
    PRINT("print", Print()),
    FORMAT("format", Format());

    fun call(objects: Array<Any?>): Any? {
        return function.call(objects)
    }

    companion object {
        fun get(name: String): Functions? {
            for (loopedFunction in entries)
                if (loopedFunction.methodName == name)
                    return loopedFunction

            return null
        }
    }
}