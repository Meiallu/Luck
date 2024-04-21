package me.meiallu.luck.function;

import me.meiallu.luck.Main;

public class Format extends Function {

    @Override
    public Object call(Object... objects) {
        if (objects.length != 1)
            throw new IllegalArgumentException();
        if (!(objects[0] instanceof String))
            throw new IllegalArgumentException();

        String string = (String) objects[0];
        char[] chars = string.toCharArray();

        StringBuilder builder = new StringBuilder();
        boolean reading = false;

        for (Character character : chars) {
            if (character == '{')
                reading = true;
            else if (character == '}') {
                String value = builder.toString();
                builder = new StringBuilder();

                reading = false;
                string = string.replace("{" + value + "}", Main.getVariables().get(value).toString());
            } else if (reading)
                builder.append(character);
        }

        return string;
    }
}
