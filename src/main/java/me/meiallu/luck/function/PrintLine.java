package me.meiallu.luck.function;

public class PrintLine extends Function {

    @Override
    public void call(Object... objects) {
        if (objects.length == 0) {
            System.out.println();
            return;
        }

        for (Object object : objects)
            System.out.println(object);
    }
}
