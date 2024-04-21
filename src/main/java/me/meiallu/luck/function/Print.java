package me.meiallu.luck.function;

public class Print extends Function {

    @Override
    public Object call(Object... objects) {
        if (objects.length == 0)
            throw new IllegalArgumentException("Print called with no objects");

        for (Object object : objects)
            System.out.print(object);

        return true;
    }
}
