package me.meiallu.luck.function

class ReadLine : Function() {

    override fun call(objects: Array<Any?>): Any {
        require(objects.isEmpty())
        return readlnOrNull() as Any
    }
}