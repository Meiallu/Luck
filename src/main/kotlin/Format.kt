package me.meiallu.luck

class Format : Function() {

    override fun call(objects: Array<Any?>): Any {
        require(objects.size == 1)
        require(objects[0] is String)

        var string = objects[0] as String
        val chars = string.toCharArray()

        var builder = StringBuilder()
        var reading = false

        for (character in chars) {
            if (character == '{')
                reading = true
            else if (character == '}') {
                val value = builder.toString()
                builder = StringBuilder()

                reading = false
                string = string.replace("{$value}", variables[value].toString())
            } else if (reading)
                builder.append(character)
        }

        return string
    }
}