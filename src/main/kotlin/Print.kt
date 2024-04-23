package me.meiallu.luck

class Print : Function() {

    override fun call(objects: Array<Any?>): Any {
        require(objects.isNotEmpty())

        for (value in objects)
            print(value)

        return true
    }
}