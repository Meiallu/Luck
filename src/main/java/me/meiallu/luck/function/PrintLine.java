package me.meiallu.luck.function;

public class PrintLine extends Function {

    @Override
    public Object call(Object... objects) {
        if (objects.length == 0) {
            System.out.println();
            return true;
        }

        for (Object object : objects)
            System.out.println(object);

        return true;
    }
}
