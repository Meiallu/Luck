package me.meiallu.luck.function

class ReadNumber : Function() {

    override fun call(objects: Array<Any?>): Any {
        require(objects.isEmpty())
        return readlnOrNull()?.toDoubleOrNull() as Any
    }
}