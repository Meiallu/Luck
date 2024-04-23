package me.meiallu.luck

class PrintLine : Function() {

    override fun call(objects: Array<Any?>): Any {
        if (objects.isEmpty()) {
            println()
            return true
        }

        for (value in objects)
            println(value)

        return true
    }
}